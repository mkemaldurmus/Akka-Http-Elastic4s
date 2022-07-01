package repo

import akka.actor.ActorSystem
import io.getquill.{LowerCase, PostgresAsyncContext}
import model.Config

import scala.concurrent.{ExecutionContext, Future}

class ConfigRepo(implicit val ctx: PostgresAsyncContext[LowerCase.type],
                 system: ActorSystem,
                 ec: ExecutionContext
                ) {
  import ctx._

  private val configQ = quote(querySchema[Config]("config"))

  def getActiveSort(): Future[Option[Config]] =
    ctx.run(configQ.filter(_.status == lift(true))).map(_.headOption)
}