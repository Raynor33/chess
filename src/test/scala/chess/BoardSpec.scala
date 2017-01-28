package chess

import org.scalatest.{Matchers, WordSpec}

class BoardSpec extends WordSpec with Matchers {

  "An InitalBoardState" should {
    "have the correct positions for everything" in {
      val pieceMap = InitialBoard.currentPositions((p) => true)
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
    "return the correct piece when pieceAt is called" in {
      InitialBoard.pieceAt(Square(0,0)) should be (WhiteRook)
      InitialBoard.pieceAt(Square(1,0)) should be (WhiteKnight)
      InitialBoard.pieceAt(Square(2,0)) should be (WhiteBishop)
      InitialBoard.pieceAt(Square(3,0)) should be (WhiteQueen)
      InitialBoard.pieceAt(Square(4,0)) should be (WhiteKing)
      InitialBoard.pieceAt(Square(5,0)) should be (WhiteBishop)
      InitialBoard.pieceAt(Square(6,0)) should be (WhiteKnight)
      InitialBoard.pieceAt(Square(7,0)) should be (WhiteRook)
      InitialBoard.pieceAt(Square(0,7)) should be (BlackRook)
      InitialBoard.pieceAt(Square(1,7)) should be (BlackKnight)
      InitialBoard.pieceAt(Square(2,7)) should be (BlackBishop)
      InitialBoard.pieceAt(Square(3,7)) should be (BlackQueen)
      InitialBoard.pieceAt(Square(4,7)) should be (BlackKing)
      InitialBoard.pieceAt(Square(5,7)) should be (BlackBishop)
      InitialBoard.pieceAt(Square(6,7)) should be (BlackKnight)
      InitialBoard.pieceAt(Square(7,7)) should be (BlackRook)
      (0 to 7).foreach(x => {
        InitialBoard.pieceAt(Square(x, 1)) should be(WhitePawn)
        InitialBoard.pieceAt(Square(x, 6)) should be(BlackPawn)
      })
    }
    "say that it's white's move" in {
      InitialBoard.toMove should be (White)
    }
    "not be in checkmate" in {
      InitialBoard.checkmate should be (false)
    }
    "not be in check for white" in {
      InitialBoard.check(White) should be (false)
    }
    "not be in check for black" in {
      InitialBoard.check(White) should be (false)
    }
  }
}
