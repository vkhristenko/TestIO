import org.apache.spark.sql._
import org.apache.spark.sql.sources._
import org.apache.spark.sql.execution.datasources._
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.types._

import org.apache.hadoop.fs.{Path, FileSystem, PathFilter, FileStatus}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.mapreduce.Job

import java.io._
import java.nio._
import java.nio.file._
import java.nio.channels._

package object sp2d {
  // implicit dataframe reader
  implicit class BinDataFrameReader(reader: DataFrameReader) {
    def binary(path: String) = reader.format("sp").load(path)
  }
}

package object sp1d {
  // implicit dataframe reader
  implicit class BinDataFrameReader(reader: DataFrameReader) {
    def binary(path: String) = reader.format("sp").load(path)
  }
}

package sp1d {
  // Iterator over arrays
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
      Row(array.grouped(100).toArray)
    }
  }

  // Default Source
  class DefaultSource extends FileFormat {
    override def toString = "binary"
  
    override def prepareWrite(
        spark: SparkSession,
        job: Job,
        options: Map[String, String],
        dataSchema: StructType): OutputWriterFactory = null

    override def isSplitable(
        spark:SparkSession,
        options: Map[String, String],
        path: Path): Boolean = 
      false

    override def inferSchema(
        sparkSession: SparkSession,
        options: Map[String, String],
        files: Seq[FileStatus]): Option[StructType] = 
      Some(StructType(
        Nil 
        :+ StructField("array", ArrayType(DoubleType))
      ))

    override def buildReaderWithPartitionValues(
        spark: SparkSession,
        dataSchema: StructType,
        partitionSchema: StructType,
        requiredSchema: StructType,
        filters: Seq[Filter],
        options: Map[String, String],
        hadoopConf: Configuration): PartitionedFile => Iterator[InternalRow] = {
      // build an iterator to arrays
      (file: PartitionedFile) => {
        val aFile = new RandomAccessFile(file.filePath.stripPrefix("file:"), "r")
        val channel = aFile.getChannel
        val buffer = ByteBuffer.allocate(100 * 100 * 8)
        buffer.order(ByteOrder.nativeOrder)
        val iter = new BinaryIterator(buffer, channel)

        new Iterator[InternalRow] {
          private val encoder = RowEncoder(requiredSchema)
          override def hasNext = iter.hasNext
          override def next(): InternalRow = encoder.toRow(iter.next())
        }
      }
    }
  }
}

package sp2d {
  // Iterator over arrays
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
      Row(array.grouped(100).toArray)
    }
  }

  // Default Source
  class DefaultSource extends FileFormat {
    override def toString = "binary"
  
    override def prepareWrite(
        spark: SparkSession,
        job: Job,
        options: Map[String, String],
        dataSchema: StructType): OutputWriterFactory = null

    override def isSplitable(
        spark:SparkSession,
        options: Map[String, String],
        path: Path): Boolean = 
      false

    override def inferSchema(
        sparkSession: SparkSession,
        options: Map[String, String],
        files: Seq[FileStatus]): Option[StructType] = 
      Some(StructType(
        Nil 
        :+ StructField("array", ArrayType(ArrayType(DoubleType)))
      ))

    override def buildReaderWithPartitionValues(
        spark: SparkSession,
        dataSchema: StructType,
        partitionSchema: StructType,
        requiredSchema: StructType,
        filters: Seq[Filter],
        options: Map[String, String],
        hadoopConf: Configuration): PartitionedFile => Iterator[InternalRow] = {
      // build an iterator to arrays
      (file: PartitionedFile) => {
        val aFile = new RandomAccessFile(file.filePath.stripPrefix("file:"), "r")
        val channel = aFile.getChannel
        val buffer = ByteBuffer.allocate(100 * 100 * 8)
        buffer.order(ByteOrder.nativeOrder)
        val iter = new BinaryIterator(buffer, channel)

        new Iterator[InternalRow] {
          private val encoder = RowEncoder(requiredSchema)
          override def hasNext = iter.hasNext
          override def next(): InternalRow = encoder.toRow(iter.next())
        }
      }
    }
  }
}
