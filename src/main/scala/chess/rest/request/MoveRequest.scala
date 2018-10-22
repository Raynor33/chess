package chess.rest.request

import chess.domain.{Piece, Square}
import play.api.libs.json.Json

case class MoveRequest(from: Square, to: Square, promotion: Option[Piece])

object MoveRequest {
  implicit val jsonFormat = Json.format[MoveRequest]
}