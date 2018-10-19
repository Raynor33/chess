package chess.rest.data

import chess.service.MoveInstruction
import play.api.libs.json.Json

case class MoveData(id: String, moveInstruction: MoveInstruction)

object MoveData {
  implicit val jsonFormat = Json.format[MoveData]
}