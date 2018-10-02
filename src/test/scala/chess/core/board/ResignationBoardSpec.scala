package chess.core.board

import chess.core.{Black, Piece, Resignation, Square, White}
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, Matchers, WordSpec}

class ResignationBoardSpec extends WordSpec with Matchers with MockitoSugar with BeforeAndAfter {

  val previousBoard = mock[Board]

  before {
    reset(previousBoard)
  }

  "A ResignationBoard" should {
    "pass through the previous positions" in {
      val previousPositions = Map.empty[Square, Piece]
      when(previousBoard.positions).thenReturn(previousPositions)
      ResignationBoard(White, previousBoard).positions shouldBe previousPositions
    }
    "pass through the previous toMove" in {
      when(previousBoard.toMove).thenReturn(White)
      ResignationBoard(White, previousBoard).toMove shouldBe White
    }
    "pass through the previous neverMoved" in {
      val square = Square(0, 0)
      when(previousBoard.neverMoved(square)).thenReturn(true)
      ResignationBoard(White, previousBoard).neverMoved(square) shouldBe true
    }
    "have no last from" in {
      ResignationBoard(White, previousBoard).lastFrom shouldBe None
    }
    "have no last to" in {
      ResignationBoard(White, previousBoard).lastTo shouldBe None
    }
    "be valid when toMove resigns" in {
      when(previousBoard.toMove).thenReturn(White)
      ResignationBoard(White, previousBoard).valid shouldBe true
    }
    "be valid when not toMove resigns" in {
      ResignationBoard(Black, previousBoard).valid shouldBe true
    }
    "have a result" in {
      ResignationBoard(White, previousBoard).result shouldBe Some(Resignation(White))
    }
  }
}
