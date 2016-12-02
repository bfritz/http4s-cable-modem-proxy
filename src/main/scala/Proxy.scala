import org.http4s.client.blaze._

object Proxy {

  def main(args: Array[String]) {
    val httpClient = PooledHttp1Client()
    val request = httpClient.expect[String]("http://localhost:8080/index.html")
    val out = request.run
    print(s"Page: $out")
    httpClient.shutdownNow()
  }
}
