package chess.domain

import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

class NilBoardSpec extends WordSpec with Matchers with MockitoSugar {

  "The Nil Board" should {
    "have None fromOption" in {
      NilBoard.lastFrom shouldBe None
    }
    "have None toOption" in {
      NilBoard.lastTo shouldBe None
    }
    "have the correct positions for everything" in {
      val pieceMap = NilBoard.positions
      pieceMap.size shouldBe 32
      pieceMap(Square(0,0)) shouldBe WhiteRook
      pieceMap(Square(1,0)) shouldBe WhiteKnight
      pieceMap(Square(2,0)) shouldBe WhiteBishop
      pieceMap(Square(3,0)) shouldBe WhiteQueen
      pieceMap(Square(4,0)) shouldBe WhiteKing
      pieceMap(Square(5,0)) shouldBe WhiteBishop
      pieceMap(Square(6,0)) shouldBe WhiteKnight
      pieceMap(Square(7,0)) shouldBe WhiteRook
      pieceMap(Square(0,7)) shouldBe BlackRook
      pieceMap(Square(1,7)) shouldBe BlackKnight
      pieceMap(Square(2,7)) shouldBe BlackBishop
      pieceMap(Square(3,7)) shouldBe BlackQueen
      pieceMap(Square(4,7)) shouldBe BlackKing
      pieceMap(Square(5,7)) shouldBe BlackBishop
      pieceMap(Square(6,7)) shouldBe BlackKnight
      pieceMap(Square(7,7)) shouldBe BlackRook
      (0 to 7).foreach(x => {
        pieceMap(Square(x, 1)) should be(WhitePawn)
        pieceMap(Square(x, 6)) should be(BlackPawn)
      })
    }
    "say hasNeverMoved is true" in {
      Square.allSquares.foreach(
        NilBoard.neverMoved(_) shouldBe true
      )
    }
    "say that it's white's move" in {
      NilBoard.toMove shouldBe Some(White)
    }
    "not have a result" in {
      NilBoard.result shouldBe None
    }
    "be valid" in {
      NilBoard.valid shouldBe true
    }
  }

}
