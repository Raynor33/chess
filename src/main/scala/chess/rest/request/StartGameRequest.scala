package chess.rest.request

import play.api.libs.json.Json

case class StartGameRequest(whitePlayerId: String, blackPlayerId: String)

object StartGameRequest {
  implicit val jsonFormat = Json.format[StartGameRequest]
}
