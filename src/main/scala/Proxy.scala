import argonaut._, Argonaut._
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._, Extract.text

import org.http4s.HttpService
import org.http4s.argonaut._
import org.http4s.client.Client
import org.http4s.client.blaze._
import org.http4s.dsl._
import org.http4s.server.{Server, ServerApp}
import org.http4s.server.blaze.BlazeBuilder

import scalaz._

case class Status(currentStatus: Option[String], uptime: Option[String])

object Status {
  implicit def StatusCodecJson: CodecJson[Status] =
    casecodec2(Status.apply, Status.unapply)("status", "uptime")
}

object Proxy extends ServerApp {

  private val ProxyPort = 8090
  private val StatusCellSelector = "table:eq(0) tr:eq(7) td:eq(1)"
  private val UptimeCellSelector = "table:eq(1) tr:eq(2) td:eq(1)"
  private val StatusPage = "http://localhost:8080/indexData.htm"

  lazy val httpClient = PooledHttp1Client()

  def jsonProxy(client: Client) = HttpService {
    case request @ GET -> Root / "status" => {
      getStatus(client) match {
        case \/-(status) => Ok(status.asJson)
        case -\/(error) => BadGateway(error)
      }
    }
  }

  def server(args: List[String]) = {
    BlazeBuilder.bindLocal(ProxyPort)
      .mountService(jsonProxy(httpClient), "/api")
      .start
  }

  override def shutdown(server: Server) = {
    httpClient.shutdownNow()
    super.shutdown(server)
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
