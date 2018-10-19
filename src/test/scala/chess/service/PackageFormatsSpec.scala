package chess.service

import chess.domain.{CastlingBoard, EnPassantBoard, NilBoard, PawnPromotionBoard, Square, StandardBoard, WhiteQueen}
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json._

class PackageFormatsSpec extends WordSpec with Matchers {

  "Package formats" should {
    val testGame = Game(
      whitePlayerId = "asdf",
      blackPlayerId = "qwer",
      board = PawnPromotionBoard(
        from = Square(1,2),
        to = Square(2,3),
        promotion = WhiteQueen,
        previousBoard = EnPassantBoard(
          from = Square(3,4),
          to = Square(4,5),
          previousBoard = CastlingBoard(
            from = Square(5,6),
            to = Square(6,7),
            previousBoard = StandardBoard(
              from = Square(0,1),
              to = Square(1,1),
              previousBoard = NilBoard
            )
          )
        )
      )
    )
    val testGameJson = Json.parse(
      """
        |{
        | "whitePlayerId": "asdf",
        | "blackPlayerId": "qwer",
        | "board": {
        |  "type": "PawnPromotionBoard",
        |  "from": {
        |   "x": 1,
        |   "y": 2
        |  },
        |  "to": {
        |   "x": 2,
        |   "y": 3
        |  },
        |  "promotion": "WhiteQueen",
        |  "previousBoard": {
        |   "type": "EnPassantBoard",
        |   "from": {
        |    "x": 3,
        |    "y": 4
        |   },
        |   "to": {
        |    "x": 4,
        |    "y": 5
        |   },
        |   "previousBoard": {
        |    "type": "CastlingBoard",
        |    "from": {
        |     "x": 5,
        |     "y": 6
        |    },
        |    "to": {
        |     "x": 6,
        |     "y": 7
        |    },
        |    "previousBoard": {
        |     "type": "StandardBoard",
        |     "from": {
        |      "x": 0,
        |      "y": 1
        |     },
        |     "to": {
        |      "x": 1,
        |      "y": 1
        |     },
        |     "previousBoard": null
        |    }
        |   }
        |  }
        | }
        |}
      """.stripMargin)
    "enable a game to be serialised correctly" in {
      Json.toJson(testGame) shouldBe testGameJson
    }
    "enable a game to be deserialised correctly" in {
      Json.fromJson[Game](testGameJson) shouldBe JsSuccess(testGame)
    }
  }
}
