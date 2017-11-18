# Testing ROOT I/O 

## Machine
- Macbook Pro 2.7GHz Intel Core i5
- Memory 8GB 1867 MHz DDR3
- Use 1GB for driver and executor for Spark.

## Data
- Next steps describe how to generate the data
- For ROOT file, there is a file on lxplus already `/afs/cern.ch/work/v/vkhriste/public/data/TestIO/test.root`

## Generate ROOT file and analyze with ROOT
- Assume ROOT is installed and `ROOTSYS` is set
- Create a ROOT file with produce.cpp
- 50K Rows with a 100 x 100 matrix of doubles per each => Amounts to __50K x 10K x 8 ~ 4GB of uncompressed data__
```
cd cc
mkdir bin
cd bin
cmake ../
make

time ./produce
time ./analyze test.root
```

- __With ROOT I get on reading and doing a sum of 50K rows of matrix elements__
- __Note: ROOT code without even trying to make it faster! Standard ROOT TTree iteration__
```
real    0m7.363s
user    0m6.129s
sys 0m0.469s
```
- __The sum has been computed with just and a for loop over it:__
```
t->GetEntry(i);
for (auto ii=0; ii<NUM; ii++)
    for (auto jj=0; jj<NUM; jj++)
        totalSum += darr[ii][jj];
```

## Analyze the same file with Apache Spark
- Use both spark-root and parquet data source. Parquet is faster because of the partitioning of a single file. (spark-root has only file level parallelism)
- __With spark-root__:
```
val ds = df.as[Seq[Seq[Double]]]
measure(ds.flatMap({case l => l.flatMap({case v => v})}).reduce(_ + _))

-----------------------------------------
Execution Time = 177137 ms
-----------------------------------------
```
- __With parquet__: _Note parquet was written with 2 partitions and therefore processed with 2 threads! Factor of 2 comes from that, otherwise +/- the same numbers._
```
measure(ds.flatMap({case l => l.flatMap({case v => v})}).reduce(_ + _))

-----------------------------------------
Execution Time = 82434 ms
-----------------------------------------

measure(ds.map({case m => m.flatMap({case v => v}).reduce(_ + _)}).reduce(_ + _))

-----------------------------------------
Execution Time = 54093 ms
-----------------------------------------

measure(df.select(explode($"darr")).select(explode($"col")).select(sum($"col")).collect)

-----------------------------------------
Execution Time = 74708 ms
-----------------------------------------
```

## Comparison
- ROOT (~6s)
- Apache Spark > 100s with 1 thread even for parquet
- What's wrong?

## Trying to understand the numbers: Read/Write a simple binary file with the same arrays
- Read/Write 2d binary arrays 80K per 1 array into a binary file with cc
```
cd cc/bin
cmake ../
make

time ./write_2darray_binary
time ./read_2darray_binary test_2d_binary.bin

real    0m6.534s
user    0m2.140s
sys 0m2.428s
```
- Read 2d binary arrays 80K per 1 array into a binary file with scala/jvm
```
cd sj
sbt
compile
testJVM/run
// in principle after that just use the .class guys in the target folder
time scala Read2DBinary /Users/vk/software/TestIO/cc/bin/test_2d_binary.bin
------------------
Total Sum = 4.95E10
Bye Bye World

real    0m8.811s
user    0m5.334s
sys 0m2.623s
```
- __Scala JVM produces a comparable result with cc/native__
- __So, let's create a simple Apache Spark Data Source__

## Spark Data for Binary 2D Array
- `sp/src/main/scala/package` contains the implementation of a simple array reading from a binary file. 
- 1 read call per each row(array) -> __minimal reading overhead__
- Reading part is quite simple
```
  class BinaryIterator(buffer: ByteBuffer, channel: FileChannel) extends Iterator[Row] {
    private val array = Array.fill[Double](100 * 100)(0)
    private val numRows = 50 * 1000
    private var iRow = 0

    def hasNext: Boolean = iRow < numRows

    def next(): Row = {
      val bytesRead = channel.read(buffer)
      buffer.flip
      buffer.asDoubleBuffer.get(array)
      iRow+=1
      Row(array)
    }
  }
```
- With this data source, the time it takes to execute the same summation queries is:
```
spark.time(ds.flatMap({case l => l}).reduce(_ + _))
Time taken: 41253 ms
res4: Double = 4.95E10

----------------------------------------

spark.time(ds.select(explode($"array")).select(sum($"col")).show)
+--------+
|sum(col)|
+--------+
| 4.95E10|
+--------+

Time taken: 51706 ms


----------------------------------------

scala> spark.time(ds.map(_.sum).agg(sum($"value")).show)
+----------+
|sum(value)|
+----------+
|   4.95E10|
+----------+

Time taken: 19505 ms


----------------------------------------

scala> spark.time(ds.map(_.sum).reduce(_ + _))
Time taken: 17705 ms
res13: Double = 4.95E10

----------------------------------------

scala> spark.time(ds.flatMap({case l => l}).reduce(_ + _))
Time taken: 40415 ms
res15: Double = 4.95E10

----------------------------------------

scala> spark.time(ds.map(_.sum).reduce(_ + _))
Time taken: 16284 ms
res16: Double = 4.95E10
```

- __Seems like that as soon as you try to explode/flatMap or anything that does the explosion of the Dataset[Seq[Double]] into Dataset[Double] blows up the execution time__
