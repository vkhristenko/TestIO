object ReadBinary {
  def main(args: Array[String]): Unit = {
    println("Hello World")

    // defs
    val fileName = "/Users/vk/software/TestIO/cc/bin/test_binary.bin"
    val bufferSize = 1024 * 4
    val numBuffers = 1000 * 1000

    // java.io
      javaio(fileName, bufferSize, numBuffers) 

    println("Bye Bye World")
  }

  def javaio(fileName: String, bufferSize: Int, numBuffers: Int): Unit = {
    import java.io._
    val inputStream = new FileInputStream(fileName)
    val buffer = Array.fill[Byte](bufferSize)(0)

    for (i <- 0 until numBuffers) {
      inputStream.read(buffer)
    }

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
