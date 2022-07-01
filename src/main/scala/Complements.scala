import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import com.sksamuel.elastic4s.http.JavaClient
import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties}
import config.AppConfig._
import controller.ProductController
import io.getquill.{LowerCase, PostgresAsyncContext}
import repo.ConfigRepo
import service.{ProductIndexService, ProductService}

import scala.concurrent.ExecutionContextExecutor

trait Complements {

  implicit val system: ActorSystem = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  implicit lazy val postgresCtxLower: PostgresAsyncContext[LowerCase.type] =
    new PostgresAsyncContext(LowerCase, postgresConfig)
  lazy val s3Client: AmazonS3 = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_1).build()
  lazy val elasticClient: ElasticClient = ElasticClient(JavaClient(ElasticProperties("http://localhost:9200")))
  lazy val indexService = new ProductIndexService(s3Client, elasticClient)
  lazy val configRepo = new ConfigRepo()

  lazy val productService = new ProductService(elasticClient, configRepo)
  lazy val productController = new ProductController(indexService, productService)
  lazy val routes: Route = productController.getRoute


}
