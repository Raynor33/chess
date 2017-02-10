package chess

import org.scalamock.scalatest.MockFactory
import org.scalatest._

class PieceSpec extends WordSpec with Matchers with OneInstancePerTest with MockFactory {

  "A WhitePiece" should {
    "be white" in {
      val whitePiece = new WhiteColoured {}
      whitePiece.colour should be (White)
    }
  }

  "A BlackPiece" should {
    "be black" in {
      val blackPiece = new BlackColoured {}
      blackPiece.colour should be (Black)
    }
  }

  class MockSquare extends Square(0,0)

  val fromMock = mock[MockSquare]
  val toSquare = Square(1,2)

  "A King" should {
    val king = new King {}
    "allow a flat move of one" in {
      (fromMock.diagonalLineTo _) expects (toSquare, 1) anyNumberOfTimes() returns (None)
      (fromMock.flatLineTo _) expects (toSquare, 1) twice() returns (Some(Set.empty[Square]))
      king.pathFor(fromMock, toSquare, false) should be(Some(Set.empty[Square]))
      king.pathFor(fromMock, toSquare, true) should be(Some(Set.empty[Square]))
    }
    "allow a diagonal move of one" in {
      (fromMock.flatLineTo _) expects (toSquare, 1) anyNumberOfTimes() returns (None)
      (fromMock.diagonalLineTo _) expects (toSquare, 1) twice() returns (Some(Set.empty[Square]))
      king.pathFor(fromMock, toSquare, false) should be(Some(Set.empty[Square]))
      king.pathFor(fromMock, toSquare, true) should be(Some(Set.empty[Square]))
    }
    "not allow a move if neither flat nor diagonal is possible" in {
      (fromMock.flatLineTo _) expects (toSquare, 1) twice() returns (None)
      (fromMock.diagonalLineTo _) expects (toSquare, 1) twice() returns (None)
      king.pathFor(fromMock, toSquare, false) should be(None)
      king.pathFor(fromMock, toSquare, true) should be(None)
    }
  }

  "A Queen" should {
    val queen = new Queen {}
    "allow a flat move of eight" in {
      (fromMock.diagonalLineTo _) expects (toSquare, 8) anyNumberOfTimes() returns (None)
      (fromMock.flatLineTo _) expects (toSquare, 8) twice() returns (Some(Set.empty[Square]))
      queen.pathFor(fromMock, toSquare, false) should be(Some(Set.empty[Square]))
      queen.pathFor(fromMock, toSquare, true) should be(Some(Set.empty[Square]))
    }
    "allow a diagonal move of eight" in {
      (fromMock.flatLineTo _) expects (toSquare, 8) anyNumberOfTimes() returns (None)
      (fromMock.diagonalLineTo _) expects (toSquare, 8) twice() returns (Some(Set.empty[Square]))
      queen.pathFor(fromMock, toSquare, false) should be(Some(Set.empty[Square]))
      queen.pathFor(fromMock, toSquare, true) should be(Some(Set.empty[Square]))
    }
    "not allow a move if neither flat nor diagonal is possible" in {
      (fromMock.flatLineTo _) expects (toSquare, 8) twice() returns (None)
      (fromMock.diagonalLineTo _) expects (toSquare, 8) twice() returns (None)
      queen.pathFor(fromMock, toSquare, false) should be(None)
      queen.pathFor(fromMock, toSquare, true) should be(None)
    }
  }

  "A Bishop" should {
    val bishop = new Bishop {}
    "allow a diagonal move of eight" in {
      (fromMock.diagonalLineTo _) expects (toSquare, 8) twice() returns (Some(Set.empty[Square]))
      bishop.pathFor(fromMock, toSquare, false) should be(Some(Set.empty[Square]))
      bishop.pathFor(fromMock, toSquare, true) should be(Some(Set.empty[Square]))
    }
    "not allow a move if a diagonal is not possible" in {
      (fromMock.diagonalLineTo _) expects (toSquare, 8) twice() returns (None)
      (fromMock.flatLineTo _) expects (*, *) never()
      bishop.pathFor(fromMock, toSquare, false) should be(None)
      bishop.pathFor(fromMock, toSquare, true) should be(None)
    }
  }

  "A Knight" should {
    val knight = new Knight {}
    val from = Square(3, 3)
    "allow a move two right and one up" in {
      knight.pathFor(from, Square(from.x + 2, from.y + 1), false) should be (Some(Set.empty[Square]))
      knight.pathFor(from, Square(from.x + 2, from.y + 1), true) should be (Some(Set.empty[Square]))
    }
    "allow a move two right and one down" in {
      knight.pathFor(from, Square(from.x + 2, from.y - 1), false) should be (Some(Set.empty[Square]))
      knight.pathFor(from, Square(from.x + 2, from.y - 1), true) should be (Some(Set.empty[Square]))

    }
    "allow a move two left and one up" in {
      knight.pathFor(from, Square(from.x - 2, from.y + 1), false) should be (Some(Set.empty[Square]))
      knight.pathFor(from, Square(from.x - 2, from.y + 1), true) should be (Some(Set.empty[Square]))

    }
    "allow a move two left and one down" in {
      knight.pathFor(from, Square(from.x - 2, from.y - 1), false) should be (Some(Set.empty[Square]))
      knight.pathFor(from, Square(from.x - 2, from.y - 1), true) should be (Some(Set.empty[Square]))
    }
    "allow a move two up and one left" in {
      knight.pathFor(from, Square(from.x - 1, from.y + 2), false) should be (Some(Set.empty[Square]))
      knight.pathFor(from, Square(from.x - 1, from.y + 2), true) should be (Some(Set.empty[Square]))

    }
    "allow a move two up and one right" in {
      knight.pathFor(from, Square(from.x + 1, from.y + 2), false) should be (Some(Set.empty[Square]))
      knight.pathFor(from, Square(from.x + 1, from.y + 2), true) should be (Some(Set.empty[Square]))
    }
    "allow a move two down and one left" in {
      knight.pathFor(from, Square(from.x - 1, from.y - 2), false) should be (Some(Set.empty[Square]))
      knight.pathFor(from, Square(from.x - 1, from.y - 2), true) should be (Some(Set.empty[Square]))
    }
    "allow a move two down and one right" in {
      knight.pathFor(from, Square(from.x + 1, from.y - 2), false) should be (Some(Set.empty[Square]))
      knight.pathFor(from, Square(from.x + 1, from.y - 2), true) should be (Some(Set.empty[Square]))
    }
    "not allow a move one diagonally" in {
      knight.pathFor(from, Square(from.x + 1, from.y + 1), false) should be (None)
      knight.pathFor(from, Square(from.x + 1, from.y + 1), true) should be (None)
    }
    "not allow a move two diagonally" in {
      knight.pathFor(from, Square(from.x + 2, from.y + 2), false) should be (None)
      knight.pathFor(from, Square(from.x + 2, from.y + 2), true) should be (None)
    }
    "not allow a move two flat" in {
      knight.pathFor(from, Square(from.x, from.y + 2), false) should be (None)
      knight.pathFor(from, Square(from.x, from.y + 2), true) should be (None)
    }
  }

  "A Rook" should {
    val rook = new Rook {}
    "allow a flat move of eight" in {
      (fromMock.flatLineTo _) expects (toSquare, 8) twice() returns (Some(Set.empty[Square]))
      rook.pathFor(fromMock, toSquare, false) should be(Some(Set.empty[Square]))
      rook.pathFor(fromMock, toSquare, true) should be(Some(Set.empty[Square]))
    }
    "not allow a move if a flat move is not possible" in {
      (fromMock.flatLineTo _) expects (toSquare, 8) twice() returns (None)
      (fromMock.diagonalLineTo _) expects (*,*) never()
      rook.pathFor(fromMock, toSquare, false) should be(None)
      rook.pathFor(fromMock, toSquare, true) should be(None)

    }
  }

  // TODO should mock squares. Means mocking val needed. Mockito?
  "A white pawn" should {
    "allow a move of one up if not taking" in {
      WhitePawn.pathFor(Square(0, 1), Square(0, 2), false) should be (Some(Set.empty[Square]))
    }
    "allow a move of two up if not taking and in the first row" in {
      WhitePawn.pathFor(Square(0, 1), Square(0, 3), false) should be (Some(Set(Square(0, 2))))
    }
    "not allow a move of one up if taking" in {
      WhitePawn.pathFor(Square(0, 1), Square(0, 2), true) should be (None)
    }
    "not allow a move of two up if taking" in {
      WhitePawn.pathFor(Square(0, 1), Square(0, 3), true) should be (None)
    }
    "not allow a move of one down" in {
      WhitePawn.pathFor(Square(0, 1), Square(0, 0), false) should be (None)
    }
    "allow a move of one diagonally left if taking" in {
      WhitePawn.pathFor(Square(1, 1), Square(0, 2), true) should be (Some(Set.empty[Square]))
    }
    "allow a move of one diagonally right if taking" in {
      WhitePawn.pathFor(Square(1, 1), Square(2, 2), true) should be (Some(Set.empty[Square]))
    }
    "not allow a move of one diagonally left if not taking" in {
      WhitePawn.pathFor(Square(1, 1), Square(0, 2), false) should be (None)
    }
    "not allow a move of one diagonally right if not taking" in {
      WhitePawn.pathFor(Square(1, 1), Square(2, 2), false) should be (None)
    }
    "not allow a move of one diagonally left and down" in {
      WhitePawn.pathFor(Square(1, 1), Square(0, 0), true) should be (None)
    }
    "not allow a move of one diagonally right and down" in {
      WhitePawn.pathFor(Square(1, 1), Square(2, 0), true) should be (None)
    }
  }

  "A black pawn" should {
    "allow a move of one down if not taking" in {
      BlackPawn.pathFor(Square(1, 6), Square(1, 5), false) should be (Some(Set.empty[Square]))
    }
    "allow a move of two down if not taking and in the first row" in {
      BlackPawn.pathFor(Square(1, 6), Square(1, 4), false) should be (Some(Set(Square(1, 5))))
    }
    "not allow a move of one down if taking" in {
      BlackPawn.pathFor(Square(1, 6), Square(1, 5), true) should be (None)
    }
    "not allow a move of two down if taking" in {
      BlackPawn.pathFor(Square(1, 6), Square(1, 4), true) should be (None)
    }
    "not allow a move of one up" in {
      BlackPawn.pathFor(Square(1, 6), Square(1, 7), false) should be (None)
    }
    "allow a move of one diagonally left if taking" in {
      BlackPawn.pathFor(Square(1, 6), Square(0, 5), true) should be (Some(Set.empty[Square]))
    }
    "allow a move of one diagonally right if taking" in {
      BlackPawn.pathFor(Square(1, 6), Square(2, 5), true) should be (Some(Set.empty[Square]))
    }
    "not allow a move of one diagonally left if not taking" in {
      BlackPawn.pathFor(Square(1, 6), Square(0, 5), false) should be (None)
    }
    "not allow a move of one diagonally right if not taking" in {
      BlackPawn.pathFor(Square(1, 6), Square(2, 5), false) should be (None)
    }
    "not allow a move of one diagonally left and up" in {
      BlackPawn.pathFor(Square(1, 6), Square(0, 7), true) should be (None)
    }
    "not allow a move of one diagonally right and up" in {
      BlackPawn.pathFor(Square(1, 6), Square(2, 7), true) should be (None)
    }
  }
}
