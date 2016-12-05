import scala.io.StdIn.readLine

import scalaz.concurrent.Task
import scalaz.\/-
import scalaz.-\/

import org.http4s._
import org.http4s.dsl._
import org.http4s.server.Server
import org.http4s.server.blaze._

/**
 * Immitates a Motorola SURFboard 6141 cable modem by
 * serving static files scraped from the real thing.
 *
 * Could have extended `ServerApp` to reduce the amount
 * of code, but making things a bit more explicit for
 * discussion purposes.
 */
object Modem {

  private val logger = org.log4s.getLogger

  private val ConnectorPoolSize = 2 // plenty to imitate a cable modem
  private val StaticExtensions = List("htm", "js", "gif")

  def main(args: Array[String]) {
    val fakeModem = HttpService {
      case request @ GET -> path ~ ext if StaticExtensions.contains(ext) =>
        serve(s"$path.$ext", request)
    }

    val serverTask: Task[Server] = BlazeBuilder.bindHttp(8080, "localhost")
      .withConnectorPoolSize(ConnectorPoolSize)
      .mountService(fakeModem, "/")
      .start

    serverTask.unsafePerformSyncAttempt match {
      case \/-(server) => awaitShutdown(server)
      case -\/(ex)     => logger.error(ex)("Failed to start server.")
    }
  }

  def serve(file: String, request: Request): Task[Response] = {
    val path = s"/modem/demo$file"
    logger.debug(s"Serving $path for request $request")

    StaticFile.fromResource(path, Some(request))
      .map(Task.now).getOrElse(NotFound())
  }

  def awaitShutdown(server: Server) = {
    readLine("Press enter to stop server.")
    server.shutdownNow()
  }
}
