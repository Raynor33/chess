package chess.formats

import chess.core._
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json._

class ChessFormatsSpec extends WordSpec with Matchers {

  "ChessFormats" should {
    val testGame = Game(
      whitePlayerId = "asdf",
      blackPlayerId = "qwer",
      board = PawnPromotionMoveBoard(
        from = Square(1,2),
        to = Square(2,3),
        promotion = WhiteQueen,
        previous = EnPassantMoveBoard(
          from = Square(3,4),
          to = Square(4,5),
          previous = CastlingMoveBoard(
            from = Square(5,6),
            to = Square(6,7),
            previous = StandardMoveBoard(
              from = Square(0,1),
              to = Square(1,1),
              previous = NilBoard
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
        |  "type": "PawnPromotionMoveBoard",
        |  "from": {
        |   "x": 1,
        |   "y": 2
        |  },
        |  "to": {
        |   "x": 2,
        |   "y": 3
        |  },
        |  "promotion": "WhiteQueen",
        |  "previous": {
        |   "type": "EnPassantMoveBoard",
        |   "from": {
        |    "x": 3,
        |    "y": 4
        |   },
        |   "to": {
        |    "x": 4,
        |    "y": 5
        |   },
        |   "previous": {
        |    "type": "CastlingMoveBoard",
        |    "from": {
        |     "x": 5,
        |     "y": 6
        |    },
        |    "to": {
        |     "x": 6,
        |     "y": 7
        |    },
        |    "previous": {
        |     "type": "StandardMoveBoard",
        |     "from": {
        |      "x": 0,
        |      "y": 1
        |     },
        |     "to": {
        |      "x": 1,
        |      "y": 1
        |     },
        |     "previous": null
        |    }
        |   }
        |  }
        | }
        |}
      """.stripMargin)
    import chess.formats.ChessFormats._
    "enable a game to be serialised correctly" in {
      Json.writes[Game].writes(testGame) shouldBe testGameJson
    }
    "enable a gamse to be deserialised correctly" in {
      Json.reads[Game].reads(testGameJson).get shouldBe testGame
    }
  }
}
