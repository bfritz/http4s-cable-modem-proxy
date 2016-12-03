import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.text

import org.http4s.client.blaze._

object Proxy {

  private val StatusCellSelector = "table:eq(0) tr:eq(7) td:eq(1)"
  private val UptimeCellSelector = "table:eq(1) tr:eq(2) td:eq(1)"

  def main(args: Array[String]) {
    val httpClient = PooledHttp1Client()
    val request = httpClient.expect[String]("http://localhost:8080/indexData.htm")
    val pageHtml = request.run

    val browser = JsoupBrowser()
    val doc = browser.parseString(pageHtml)
    val status: Option[String] = doc >?> text(StatusCellSelector)
    val uptimeText: Option[String] = doc >?> text(UptimeCellSelector)

    println(s"status: $status")
    println(s"uptime: $uptimeText")

    httpClient.shutdownNow()
  }
}
