package chess.core.board

import chess.core.{BlackBishop, BlackKing, BlackKnight, BlackPawn, BlackQueen, BlackRook, Square, White, WhiteBishop, WhiteKing, WhiteKnight, WhitePawn, WhiteQueen, WhiteRook}
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.mock.MockitoSugar

class NilBoardSpec extends WordSpec with Matchers with MockitoSugar {

  "The Nil Board" should {
    "have None fromOption" in {
      NilBoard.fromOption shouldBe None
    }
    "have None toOption" in {
      NilBoard.toOption shouldBe None
    }
    "have the correct positions for everything" in {
      val pieceMap = NilBoard.currentPositions
      pieceMap.size should be (32)
      pieceMap(Square(0,0)) should be (WhiteRook)
      pieceMap(Square(1,0)) should be (WhiteKnight)
      pieceMap(Square(2,0)) should be (WhiteBishop)
      pieceMap(Square(3,0)) should be (WhiteQueen)
      pieceMap(Square(4,0)) should be (WhiteKing)
      pieceMap(Square(5,0)) should be (WhiteBishop)
      pieceMap(Square(6,0)) should be (WhiteKnight)
      pieceMap(Square(7,0)) should be (WhiteRook)
      pieceMap(Square(0,7)) should be (BlackRook)
      pieceMap(Square(1,7)) should be (BlackKnight)
      pieceMap(Square(2,7)) should be (BlackBishop)
      pieceMap(Square(3,7)) should be (BlackQueen)
      pieceMap(Square(4,7)) should be (BlackKing)
      pieceMap(Square(5,7)) should be (BlackBishop)
      pieceMap(Square(6,7)) should be (BlackKnight)
      pieceMap(Square(7,7)) should be (BlackRook)
      (0 to 7).foreach(x => {
        pieceMap(Square(x, 1)) should be(WhitePawn)
        pieceMap(Square(x, 6)) should be(BlackPawn)
      })
    }
    "say hasNeverMoved is true" in {
      Square.allSquares.foreach(
        NilBoard.hasNeverMoved(_) should be (true)
      )
    }
    "say that it's white's move" in {
      NilBoard.toMove should be (White)
    }
    "not have a result" in {
      NilBoard.result should be (None)
    }
    "be valid" in {
      NilBoard.valid should be (true)
    }
  }

}
