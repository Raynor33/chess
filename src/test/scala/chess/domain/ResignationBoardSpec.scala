package chess.domain

import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, Matchers, WordSpec}

class ResignationBoardSpec extends WordSpec with Matchers with MockitoSugar with BeforeAndAfter {

  val previousBoard = mock[Board]

  before {
    reset(previousBoard)
    when(previousBoard.toMove).thenReturn(Some(White))
  }

  "A ResignationBoard" should {
    "pass through the previous positions" in {
      val previousPositions = Map.empty[Square, Piece]
      when(previousBoard.positions).thenReturn(previousPositions)
      ResignationBoard(White, previousBoard).positions shouldBe previousPositions
    }
    "have no toMove" in {
      ResignationBoard(White, previousBoard).toMove shouldBe None
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
      ResignationBoard(White, previousBoard).valid shouldBe true
    }
    "be valid when not toMove resigns" in {
      ResignationBoard(Black, previousBoard).valid shouldBe true
    }
    "have a result" in {
      val board = ResignationBoard(White, previousBoard)
      board.result shouldBe Some(Resignation(White))
      board.toMove shouldBe None
    }
  }
}
