import argonaut._, Argonaut._

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
  private val StatusCellSelector = "tr:eq(7) td:eq(1)" // in first table
  private val UptimeCellSelector = "tr:eq(2) td:eq(1)" // in second table
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
    import net.ruippeixotog.scalascraper.browser.JsoupBrowser
    import net.ruippeixotog.scalascraper.dsl.DSL._, Extract.text, Extract.elementList
    import net.ruippeixotog.scalascraper.model.Element

    val request = client.expect[String](StatusPage)
    val pageHtml = request.run

    val browser = JsoupBrowser()
    val doc = browser.parseString(pageHtml)
    val tables: List[Element] = doc >> elementList("table")
    val status: Option[String] = tables.lift(0).flatMap(_ >?> text(StatusCellSelector))
    val uptimeText: Option[String] = tables.lift(1).flatMap(_ >?> text(UptimeCellSelector))

    \/-(Status(status, uptimeText))
  }
}
