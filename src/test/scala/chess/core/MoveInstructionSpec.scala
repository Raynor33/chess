package chess.core

import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import org.scalatest.{Matchers, OneInstancePerTest, WordSpec}
import org.scalatest.mockito.MockitoSugar

class MoveInstructionSpec extends WordSpec with Matchers with OneInstancePerTest with MockitoSugar {

  "A StandardMoveInstruction" should {
    "apply itself correctly to a game" in {
      val game = mock[Board]
      val from = mock[Square]
      val to = mock[Square]
      StandardMoveInstruction(from, to).applyTo(game) should be (StandardMove(from, to, game))
    }
  }

  "A CastlingMoveInstruction" should {
    "apply itself correctly to a game" in {
      val game = mock[Board]
      val from = mock[Square]
      val to = mock[Square]
      CastlingMoveInstruction(from, to).applyTo(game) should be (CastlingMove(from, to, game))
    }
  }

  "An EnPassantMoveInstruction" should {
    "apply itself correctly to a game" in {
      val game = mock[Board]
      val from = mock[Square]
      val to = mock[Square]
      EnPassantMoveInstruction(from, to).applyTo(game) should be (EnPassantMove(from, to, game))
    }
  }

  "A PawnPromotionMoveInstruction" should {
    "apply itself correctly to a game" in {
      val game = mock[Board]
      val from = mock[Square]
      val to = mock[Square]
      val piece = mock[Piece]
      PawnPromotionMoveInstruction(from, to, piece).applyTo(game) should be (PawnPromotionMove(from, to, piece, game))
    }
  }
}
