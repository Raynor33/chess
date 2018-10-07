package chess.rest

import chess.service.{MoveInstruction, StartGameInstruction}
import play.api.libs.json.{Json, OFormat}
import play.api.libs.json._
import julienrf.json.derived.flat._

package object data {

  implicit val positionData = Json.format[PositionData]
  implicit val gameData = Json.format[GameData]
  implicit val moveData = Json.format[MoveData]
}
