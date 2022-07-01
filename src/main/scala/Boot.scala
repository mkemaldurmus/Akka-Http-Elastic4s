import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route

object Boot extends App with Complements {

  Http()
    .newServerAt("localHost", 8080)
    .bindFlow(Route.seal(routes))
    .map(_ => println(s"Server started at port 8080"))
}

