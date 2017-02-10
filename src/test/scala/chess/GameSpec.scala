package chess

import Game._
import org.scalamock.scalatest.MockFactory
import org.scalatest._

class GameSpec extends WordSpec with Matchers with OneInstancePerTest with MockFactory {

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

  "A NonNilGame" should {
    val previousMock = mock[Game]
    val currentMock = mock[Game]
    val nonNilGame = new NonNilGame {
      val previous = previousMock
      def currentPositions(predicate: (Piece) => Boolean) =
        currentMock.currentPositions(predicate)
      def valid = currentMock.valid
    }

    "have a toMove of White if previous game's toMove is Black" in {
      (previousMock.toMove _) expects () returns (Black)
      nonNilGame.toMove should be(White)
    }
    "have a toMove of Black if previous game's toMove is White" in {
      (previousMock.toMove _) expects () returns (White)
      nonNilGame.toMove should be(Black)
    }

    val square1 = Square(3,4);
    val square2 = Square(4,5);
    val square3 = Square(5,6);
    val attackingPiece = mock[Piece]
    "say black is in check if a white piece is attacking it" in {
      (currentMock.currentPositions _) expects (MatchAll) returns (
        Map(square1 -> attackingPiece, square2 -> BlackKing, square3 -> WhitePawn))
      (attackingPiece.colour _) expects() anyNumberOfTimes() returns (White)
      (attackingPiece.pathFor _) expects(square1, square2, true) returns (Some(Set.empty[Square]))
      nonNilGame.check(Black) should be (true)
    }
    "say black is not in check if no white piece is attacking it" in {
      (currentMock.currentPositions _) expects (MatchAll) returns (
        Map(square1 -> attackingPiece, square2 -> BlackKing, square3 -> WhitePawn))
      (attackingPiece.colour _) expects() anyNumberOfTimes() returns (White)
      (attackingPiece.pathFor _) expects(square1, square2, true) returns (None)
      nonNilGame.check(Black) should be (false)
    }
    "say black is not in check if all white pieces attacking it are blocked" in {
      (currentMock.currentPositions _) expects (MatchAll) returns (
        Map(square1 -> attackingPiece, square2 -> BlackKing, square3 -> WhitePawn))
      (attackingPiece.colour _) expects() anyNumberOfTimes() returns (White)
      (attackingPiece.pathFor _) expects(square1, square2, true) returns (Some(Set(square3)))
      nonNilGame.check(Black) should be (false)
    }
    "say white is in check if a black piece is attacking it" in {
      (currentMock.currentPositions _) expects (MatchAll) returns (
        Map(square1 -> attackingPiece, square2 -> WhiteKing, square3 -> WhitePawn))
      (attackingPiece.colour _) expects() anyNumberOfTimes() returns (Black)
      (attackingPiece.pathFor _) expects(square1, square2, true) returns (Some(Set.empty[Square]))
      nonNilGame.check(White) should be (true)
    }
    "say white is not in check if no black piece is attacking it" in {
      (currentMock.currentPositions _) expects (MatchAll) returns (
        Map(square1 -> attackingPiece, square2 -> WhiteKing, square3 -> WhitePawn))
      (attackingPiece.colour _) expects() anyNumberOfTimes() returns (Black)
      (attackingPiece.pathFor _) expects(square1, square2, true) returns (None)
      nonNilGame.check(White) should be (false)
    }
    "say white is not in check if all black pieces attacking it are blocked" in {
      (currentMock.currentPositions _) expects (MatchAll) returns (
        Map(square1 -> attackingPiece, square2 -> WhiteKing, square3 -> WhitePawn))
      (attackingPiece.colour _) expects() anyNumberOfTimes() returns (Black)
      (attackingPiece.pathFor _) expects(square1, square2, true) returns (Some(Set(square3)))
      nonNilGame.check(White) should be (false)
    }
  }
}
