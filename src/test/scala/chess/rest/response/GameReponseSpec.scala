package chess.rest.response

import chess.domain.Board
import chess.service.Game
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.{JsNull, Json}

class GameReponseSpec extends WordSpec with Matchers {

  "GameData" should {
    "be writable as json" in {
      Json.toJson(GameResponse("id", Game("a", "b", Board.setup))) shouldBe Json.obj(
        "id" -> "id",
        "whitePlayerId" -> "a",
        "blackPlayerId" -> "b",
        "toMove" -> "white",
        "result" -> JsNull,
        "positions" -> Json.obj(
          "a1" -> "wR",
          "b1" -> "wN",
          "c1" -> "wB",
          "d1" -> "wQ",
          "e1" -> "wK",
          "f1" -> "wB",
          "g1" -> "wN",
          "h1" -> "wR",
          "a2" -> "wP",
          "b2" -> "wP",
          "c2" -> "wP",
          "d2" -> "wP",
          "e2" -> "wP",
          "f2" -> "wP",
          "g2" -> "wP",
          "h2" -> "wP",
          "a8" -> "bR",
          "b8" -> "bN",
          "c8" -> "bB",
          "d8" -> "bQ",
          "e8" -> "bK",
          "f8" -> "bB",
          "g8" -> "bN",
          "h8" -> "bR",
          "a7" -> "bP",
          "b7" -> "bP",
          "c7" -> "bP",
          "d7" -> "bP",
          "e7" -> "bP",
          "f7" -> "bP",
          "g7" -> "bP",
          "h7" -> "bP"
        )
      )
    }
  }
}
