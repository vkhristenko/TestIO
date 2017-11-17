# Testing ROOT I/O 

## Machine
- Macbook Pro 2.7GHz Intel Core i5
- Memory 8GB 1867 MHz DDR3
- Use 1GB for driver and executor for Spark.

## Generate ROOT file and analyze with ROOT
- Assume ROOT is installed and `ROOTSYS` is set
- Create a ROOT file with produce.cpp
- 50K Rows with a 100 x 100 matrix of doubles per each
```
cd cc
mkdir bin
cd bin
cmake ../
make

time ./produce
time ./analyze test.root
```

__With ROOT I get on reading and doing a sum of 50K rows of matrix elements__
```
real    0m7.363s
user    0m6.129s
sys 0m0.469s
```
__The sum has been computed with just and a for loop over it:__
```
t->GetEntry(i);
for (auto ii=0; ii<NUM; ii++)
    for (auto jj=0; jj<NUM; jj++)
        totalSum += darr[ii][jj];
```

## Analyze ROOT file with Apache Spark
- Use both spark-root and parquet data source. Parquet is faster because of the partitioning of a single file. (spark-root has only file level parallelism)
- With spark-root:
```
val ds = df.as[Seq[Seq[Double]]]
measure(ds.flatMap({case l => l.flatMap({case v => v})}).reduce(_ + _))

-----------------------------------------
Execution Time = 177137 ms
-----------------------------------------
```
- With parquet:
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
