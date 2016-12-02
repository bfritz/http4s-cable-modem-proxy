import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.text

import org.http4s.client.blaze._

object Proxy {

  def main(args: Array[String]) {
    val httpClient = PooledHttp1Client()
    val request = httpClient.expect[String]("http://localhost:8080/index.html")
    val out = request.run
    println(s"Page: $out")

    val browser = JsoupBrowser()
    val doc = browser.parseString(out)
    val paraText = doc >> text("p")

    println(s"Text: $paraText")

    httpClient.shutdownNow()
  }
}
