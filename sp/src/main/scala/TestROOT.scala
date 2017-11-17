

import org.dianahep.sparkroot.experimental._

import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

object TestROOT {
  def main(args: Array[String]) {
    val inputFileName = args(0)
    val spark = SparkSession.builder()
      .appName("TestROOT")
      .getOrCreate()

    val df = spark.sqlContext.read.root(inputFileName)

    spark.time(df.count)

    spark.stop
  }
}
