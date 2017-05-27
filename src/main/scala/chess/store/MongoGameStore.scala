package chess.store

import javax.inject.{Inject, Singleton}

import chess.core._
import chess.service.{GameServiceResponse, Missing, Success}
import chess.store.MongoGame._
import com.google.inject.name.Named
import play.api.Configuration
import play.api.libs.json._
import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection
import scala.collection.JavaConverters._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class MongoGame(_id: BSONObjectID, game: Game)

object MongoGame {
  implicit val mongoGameFormat = Json.format[MongoGame]
}

@Singleton
class MongoGameStore @Inject() (configuration: Configuration) extends GameStore {
  private val driver = new reactivemongo.api.MongoDriver()
  private val mongos = configuration.getStringList("mongo.uri").map(_.asScala).getOrElse(List("localhost:27017"))
  private val connection = driver.connection(mongos)

  def gamesCollection = connection.database("chess")
    .map(_.collection[JSONCollection]("game"))

  override def getGame(id: String): Future[Option[Game]] = {
    Future.successful(BSONObjectID.parse(id).toOption).flatMap(bid =>
      gamesCollection.flatMap(col =>
        col.find(Json.obj("_id" -> bid))
          .one[MongoGame]
          .map(_.map(_.game))
      )
    )
  }

  override def insertGame(game: Game): Future[String] = {
    val mongoGame = MongoGame(BSONObjectID.generate, game)
    gamesCollection.flatMap(_.insert(mongoGame).map(_ => mongoGame._id.stringify))
  }

  override def saveGame(id: String, game: Game): Future[GameServiceResponse] = {
    BSONObjectID.parse(id).toOption match {
        case Some(bid) => gamesCollection.flatMap(_.update(Json.obj("_id" -> bid), MongoGame(bid, game)).map(r => if (r.n > 0) Success(id, game) else Missing))
        case None => Future.successful(Missing)
    }
  }
}
