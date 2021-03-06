

import org.dianahep.sparkroot.experimental._

import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

object TestROOT {
  def measure[T](f: => T): Unit = {
    val startTime = System.nanoTime
    f;
    val stopTime = System.nanoTime
    val diff = (stopTime - startTime) / 1000000;
    println("-----------------------------------------")
    println(s"Execution Time = $diff ms")
    println("-----------------------------------------")
  }

  def main(args: Array[String]) {
    if (args.length != 2) System.exit(1)
    val inputFileName = args(0)
    val parquetFileName =args(1)
    val spark = SparkSession.builder()
      .appName("TestROOT")
      .getOrCreate()

    val df = spark.sqlContext.read.root(inputFileName)

    measure(spark.time(df.count))

    import spark.implicits._
    val ds = df.as[Seq[Seq[Double]]]
    measure(ds.flatMap({case l => l.flatMap({case v => v})}).reduce(_ + _))

    measure(df.write.parquet(parquetFileName))

    spark.stop
  }
}
