package chess.mongo

import chess.core.{Game, StandardMove}
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json._
import play.modules.reactivemongo.json.collection._

case class MongoGame(_id: BSONObjectID, game: Game)

object MongoGame {
  import chess.formats.ChessFormats._
  implicit val mongoGameFormat = Json.format[MongoGame]
}
