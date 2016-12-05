import argonaut._, Argonaut._
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.text

import org.http4s.client.Client
import org.http4s.client.blaze._

import scalaz._

case class Status(currentStatus: Option[String], uptime: Option[String])

object Status {
  implicit def StatusCodecJson: CodecJson[Status] =
    casecodec2(Status.apply, Status.unapply)("status", "uptime")
}

object Proxy {

  private val StatusCellSelector = "table:eq(0) tr:eq(7) td:eq(1)"
  private val UptimeCellSelector = "table:eq(1) tr:eq(2) td:eq(1)"
  private val StatusPage = "http://localhost:8080/indexData.htm"

  def main(args: Array[String]) {
    val httpClient = PooledHttp1Client()

    for {
      status <- getStatus(httpClient)
    } println(s"status: ${status.asJson.spaces2}")

    httpClient.shutdownNow()
  }

  def getStatus(client: Client): \/[String, Status] = {
    val request = client.expect[String](StatusPage)
    val pageHtml = request.run

    val browser = JsoupBrowser()
    val doc = browser.parseString(pageHtml)
    val status: Option[String] = doc >?> text(StatusCellSelector)
    val uptimeText: Option[String] = doc >?> text(UptimeCellSelector)

    \/-(Status(status, uptimeText))
  }
}
