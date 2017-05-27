package chess

import chess.core._
import play.api.libs.json.{Json, OFormat}
import play.api.libs.json._
import julienrf.json.derived.flat._

package object model {

  implicit val moveInstructionFormats: OFormat[MoveInstruction] = oformat((__ \ "type").format[String])
  implicit val startGameInstruction = Json.format[StartGameInstruction]
  implicit val displaySquare = Json.format[DisplaySquare]
  implicit val displayGame = Json.format[DisplayGame]
  implicit val displayMove = Json.format[DisplayMove]
}
