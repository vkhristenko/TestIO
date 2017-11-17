import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

import org.apache.spark.sql.functions._

object TestParquet {
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
    if (args.length != 1) System.exit(1)
    val inputFileName = args(0)
    val spark = SparkSession.builder()
      .appName("TestParquet")
      .getOrCreate()

    val df = spark.sqlContext.read.parquet(inputFileName)

    measure(spark.time(df.count))

    import spark.implicits._
    val ds = df.as[Seq[Seq[Double]]]
    measure(ds.flatMap({case l => l.flatMap({case v => v})}).reduce(_ + _))

    measure(ds.map({case m => m.flatMap({case v => v}).reduce(_ + _)}).reduce(_ + _))

    measure(df.select(explode($"darr")).select(explode($"col")).select(sum($"col")).collect)

    spark.stop
  }
}
