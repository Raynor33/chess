package chess.mongo

import chess.core._
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.play.json.collection.JSONCollection
import play.api.libs.json._
import reactivemongo.play.json._
import collection._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import chess.formats.ChessFormats._
import chess.mongo.MongoGame._
import reactivemongo.api.MongoConnectionOptions

case class MongoGameStore(uris: List[String]) extends GameStore {
  private val driver = new reactivemongo.api.MongoDriver()
  private val connection = driver.connection(uris)

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
        case Some(bid) => gamesCollection.flatMap(_.update(Json.obj("_id" -> bid), MongoGame(bid, game)).map(r => if (r.n > 0) Success else NotFound))
        case None => Future.successful(NotFound)
    }
  }
}
