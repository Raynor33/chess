package chess

import org.scalatest.{Matchers, WordSpec}

class GameSpec extends WordSpec with Matchers {

  "The Nil Game" should {
    "have the correct positions for everything" in {
      val pieceMap = Nil.currentPositions((p) => true)
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
    "say that it's white's move" in {
      Nil.toMove should be (White)
    }
    "not be in checkmate" in {
      Nil.checkmate should be (false)
    }
    "not be in check for white" in {
      Nil.check(White) should be (false)
    }
    "not be in check for black" in {
      Nil.check(White) should be (false)
    }
    "be valid" in {
      Nil.valid should be (true)
    }
  }
}
