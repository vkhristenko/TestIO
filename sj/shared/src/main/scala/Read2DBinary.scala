import java.nio._
import java.nio.file._
import java.io._

object Read2DBinary {
  def main(args: Array[String]): Unit = {
    println("Hello World")

    // defs
    val fileName = args(0)
    val numValues = 100
    val numRows = 50 * 1000

    val aFile = new RandomAccessFile(fileName, "r")
    val channel = aFile.getChannel
    val buffer = ByteBuffer.allocate(numValues * numValues * 8)
    buffer.order(ByteOrder.nativeOrder)
    var i = 0
    val array = Array.fill[Double](numValues * numValues)(0)
    var sum: Double = 0
    while (i < numRows) {
      val bytesRead = channel.read(buffer)
      buffer.flip
      buffer.asDoubleBuffer.get(array)
      sum += array.reduce(_ + _)
      i += 1
    }

    println(s"Total Sum = $sum")

    println("Bye Bye World")
  }
}
