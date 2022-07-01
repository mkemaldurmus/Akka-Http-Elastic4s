package controller

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.{Directives, Route}
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps
import model.RequestParameters
import service.{ProductIndexService, ProductService}

import scala.util.{Failure, Success}

class ProductController(indexService: ProductIndexService, productService: ProductService) extends Directives {

  def getRoute: Route = {
    (path("products") & get) {
      parameters("size".as[Int], "page".as[Int]).as(RequestParameters.apply _) { parameters =>
        onComplete(productService.get(parameters.size, parameters.page)) {
          case Success(res) =>
            complete(StatusCodes.OK -> toHttpEntity(res.toString))
          case Failure(e) => complete(StatusCodes.InternalServerError -> toHttpEntity(e.getMessage)) // neden yanlış
        }
      }
    } ~ (path("indexes") & post) {
      parameter(Symbol("name").as[String]) { indexName =>
        onComplete(indexService.indexProducts(indexName)) {
          case Success(count) if count >= 1 =>
            complete(StatusCodes.OK -> toHttpEntity(count.toString))
          case Success(count) =>
            complete(StatusCodes.NoContent -> toHttpEntity(count.toString))
          case Failure(e) => failWith(e)
        }
      }
    }
  }

  def toHttpEntity(res: String): HttpEntity = (HttpEntity(ContentTypes.`application/json`, res))
}


