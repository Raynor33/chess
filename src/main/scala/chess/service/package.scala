package chess

import julienrf.json.derived.flat.oformat
import play.api.libs.json.{Json, OFormat, __}

package object service {
  implicit val moveInstructionFormats: OFormat[MoveInstruction] = oformat((__ \ "type").format[String])
  implicit val startGameInstruction = Json.format[StartGameInstruction]
  implicit val gameFormat = Json.format[Game]
}
