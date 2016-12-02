import scala.io.StdIn.readLine

import scalaz.concurrent.Task

import org.http4s._
import org.http4s.dsl._
import org.http4s.server.blaze._

object MockModemExplicit {

  private val ConnectorPoolSize = 2 // plenty to imitate a cable modem
  private val StaticExtensions = List(".htm", ".html", ".js", ".gif")

  def main(args: Array[String]) {
    val fakeModem = HttpService {
      case request @ GET -> Root / path if StaticExtensions.exists(path.endsWith) =>
        serve(path, request)
    }

    val builder = BlazeBuilder.bindHttp(8080, "localhost")
      .withConnectorPoolSize(ConnectorPoolSize)
      .mountService(fakeModem, "/")

    val server = builder.run

    readLine("Press enter to stop server.")
    server.shutdownNow()
  }

  def serve(file: String, request: Request): Task[Response] =
    StaticFile.fromResource("/modem/demo/" + file, Some(request))
      .map(Task.now).getOrElse(NotFound())
}
