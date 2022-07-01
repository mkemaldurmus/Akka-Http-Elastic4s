package service

import com.sksamuel.elastic4s.ElasticApi._
import com.sksamuel.elastic4s.ElasticDsl.SearchHandler
import com.sksamuel.elastic4s.requests.searches.SearchResponse
import com.sksamuel.elastic4s.requests.searches.sort.{FieldSort, SortOrder}
import com.sksamuel.elastic4s.{ElasticClient, RequestFailure, RequestSuccess}
import io.circe.generic.auto._
import io.circe.parser.decode
import model.{Config, Product}
import repo.ConfigRepo

import scala.concurrent.ExecutionContext.Implicits.global

class ProductService(elasticClient: ElasticClient, configRepo: ConfigRepo) {


  def get(size: Int, page: Int) = {

    for {
      maybeSortConfig <- configRepo.getActiveSort()
      maybeSortFiled = strToEsSort(maybeSortConfig)
      sortFiled <- maybeSortFiled
    } yield {

      elasticClient.execute {
        search("ozgor")
          .matchAllQuery()
          .from((page - 1) * size)
          .size(size)
          .sortBy(sortFiled)
      }.map {
        case success: RequestSuccess[SearchResponse] =>
          success.result.hits.hits
            .map(_.sourceAsString)
            .flatMap(decode[List[Product]](_).toOption)
            .toSeq
        case fail: RequestFailure =>
          println("Error when gets products from ES", fail)
          Seq.empty[Product]
      }
    }
  }.flatten

  def strToEsSort(strConf: Option[Config]) = {
    strConf.map { cnf =>
      (cnf.sort.split(" ").toList) match {
        case x :: xs if (xs.size == 1) => {
          if (xs.head == "asc")
            FieldSort(x, order = SortOrder.ASC)
          else if (xs.head == "desc")
            FieldSort(x, order = SortOrder.DESC)
          else
            FieldSort("price", order = SortOrder.DESC)
        }
        case _ =>
          //throw new Exception("Config not found")
          Some(FieldSort("price", order = SortOrder.DESC))
      }
    }
  }

}
