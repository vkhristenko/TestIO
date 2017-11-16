object ReadBinary {
  def main(args: Array[String]): Unit = {
    println("Hello World")

    // defs
    val fileName = "/Users/vk/software/TestIO/cc/bin/test_binary.bin"
    val bufferSize = 100
    val numBuffers = 1000 * 1000
    val which = args(1).asInstanceOf[Int]

    // java.io
    which match {
      case 1 => javaio(fileName, bufferSize, numBuffers)
      case 2 => randomacess(fileName, bufferSize, numBuffers)
      case 3 => buffered(fileName, bufferSize, numBuffers)
      case 4 => bytebuffer(fileName, bufferSize, numBuffers)
      case 5 => memmapped(fileName, bufferSize, numBuffers)
      case _ => ()
    }

    println("Bye Bye World")
  }

  def javaio(fileName: String, bufferSize: Int, numBuffers: Int): Unit = {
    import java.io._
    val inputStream = new FileInputStream(fileName)
    val buffer = Array.fill[Byte](bufferSize)(0)

    for (i <- 0 until numBuffers)
      inputStream.read(buffer)

    inputStream.close
  }

  def randomacess(fileName: String, bufferSize: Int, numBuffers: Int): Unit = {
    import java.io._
    val inputStream = new FileInputStream(fileName)
    val buffer = Array.fill[Byte](bufferSize)(0)

    for (i <- 0 until numBuffers)
      inputStream.read(buffer)

    inputStream.close
  }
  
  def buffered(fileName: String, bufferSize: Int, numBuffers: Int): Unit = {
    import java.io._
    val inputStream = new FileInputStream(fileName)
    val buffer = Array.fill[Byte](bufferSize)(0)

    for (i <- 0 until numBuffers)
      inputStream.read(buffer)

    inputStream.close
  }

  def bytebuffer(fileName: String, bufferSize: Int, numBuffers: Int): Unit = {
    import java.io._
    val inputStream = new FileInputStream(fileName)
    val buffer = Array.fill[Byte](bufferSize)(0)

    for (i <- 0 until numBuffers)
      inputStream.read(buffer)

    inputStream.close
  }

  def memmapped(fileName: String, bufferSize: Int, numBuffers: Int): Unit = {
    import java.io._
    val inputStream = new FileInputStream(fileName)
    val buffer = Array.fill[Byte](bufferSize)(0)

    for (i <- 0 until numBuffers)
      inputStream.read(buffer)

    inputStream.close
  }
}
