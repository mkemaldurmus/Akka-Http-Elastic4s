package service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.S3ObjectInputStream
import com.sksamuel.elastic4s.ElasticApi.{RichFuture, createIndex, properties}
import com.sksamuel.elastic4s.ElasticDsl.{BulkHandler, CreateIndexHandler, bulk, indexInto}
import com.sksamuel.elastic4s.circe.indexableWithCirce
import com.sksamuel.elastic4s.fields.{IntegerField, LongField, TextField}
import com.sksamuel.elastic4s.requests.indexes.CreateIndexResponse
import com.sksamuel.elastic4s.{ElasticClient, RequestFailure, RequestSuccess, Response}
import io.circe.generic.auto._
import io.circe.parser
import model.Product

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ProductIndexService(s3Client: AmazonS3, elasticClient: ElasticClient) {

  val res: S3ObjectInputStream = s3Client.getObject("insider-trial-day", "scala-api-design/sample.json").getObjectContent
  val parsedProducts: String = scala.io.Source.fromInputStream(res).mkString("")


  def indexProducts(indexName: String): Future[Int] = {
    createMapping(indexName)
    parser.parse(parsedProducts).flatMap(_.as[List[Product]]).fold(throw _, identity)
    match {
      case Nil => Future.successful(0)
      case productList =>
        val bulkIndexRequests =
          productList.map(product => indexInto(indexName).doc(product).withId(product.item_id.toString))

        elasticClient
          .execute(bulk(bulkIndexRequests)).map {
          case RequestSuccess(_, _, _, result) => result.successes.size
          case RequestFailure(_, _, _, error) => throw new RuntimeException(error.reason)
        }
    }
  }

  private def createMapping(indexName: String): Response[CreateIndexResponse] = {
    elasticClient.execute {
      createIndex(indexName).mapping(
        properties(
          LongField("item_id"),
          TextField("name"),
          TextField("locale"),
          IntegerField("click"),
          IntegerField("purchase")
        )
      )
    }.await
  }

}
