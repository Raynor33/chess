package chess.model

import chess.core._
import chess.core.board._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, OneInstancePerTest, WordSpec}

class MoveInstructionSpec extends WordSpec with Matchers with OneInstancePerTest with MockitoSugar {

  "A StandardMoveInstruction" should {
    "apply itself correctly to a game" in {
      val game = mock[Board]
      val from = mock[Square]
      val to = mock[Square]
      StandardMoveInstruction(from, to).applyTo(game) should be (StandardBoard(from, to, game))
    }
  }

  "A CastlingMoveInstruction" should {
    "apply itself correctly to a game" in {
      val game = mock[Board]
      val from = mock[Square]
      val to = mock[Square]
      CastlingMoveInstruction(from, to).applyTo(game) should be (CastlingBoard(from, to, game))
    }
  }

  "An EnPassantMoveInstruction" should {
    "apply itself correctly to a game" in {
      val game = mock[Board]
      val from = mock[Square]
      val to = mock[Square]
      EnPassantMoveInstruction(from, to).applyTo(game) should be (EnPassantBoard(from, to, game))
    }
  }

  "A PawnPromotionMoveInstruction" should {
    "apply itself correctly to a game" in {
      val game = mock[Board]
      val from = mock[Square]
      val to = mock[Square]
      val piece = mock[Piece]
      PawnPromotionMoveInstruction(from, to, piece).applyTo(game) should be (PawnPromotionBoard(from, to, piece, game))
    }
  }
}
