import scala.io.StdIn.readLine

import scalaz.concurrent.Task

import org.http4s._
import org.http4s.dsl._
import org.http4s.server.blaze._

object MockModemExplicit {

  private val logger = org.log4s.getLogger

  private val ConnectorPoolSize = 2 // plenty to imitate a cable modem
  private val StaticExtensions = List("htm", "js", "gif")

  def main(args: Array[String]) {
    val fakeModem = HttpService {
      case request @ GET -> path ~ ext if StaticExtensions.contains(ext) =>
        serve(s"$path.$ext", request)
    }

    val builder = BlazeBuilder.bindHttp(8080, "localhost")
      .withConnectorPoolSize(ConnectorPoolSize)
      .mountService(fakeModem, "/")

    val server = builder.run

    readLine("Press enter to stop server.")
    server.shutdownNow()
  }

  def serve(file: String, request: Request): Task[Response] = {
    val path = s"/modem/demo$file"
    logger.debug(s"Serving $path for request $request")

    StaticFile.fromResource(path, Some(request))
      .map(Task.now).getOrElse(NotFound())
  }
}
