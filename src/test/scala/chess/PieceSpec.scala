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
      (fromMock.flatLineTo _) expects (toSquare, 1) returns (Some(Set.empty[Square]))
      king.pathFor(fromMock, toSquare) should be(Some(Set.empty[Square]))
    }
    "allow a diagonal move of one" in {
      (fromMock.flatLineTo _) expects (toSquare, 1) anyNumberOfTimes() returns (None)
      (fromMock.diagonalLineTo _) expects (toSquare, 1) returns (Some(Set.empty[Square]))
      king.pathFor(fromMock, toSquare) should be(Some(Set.empty[Square]))
    }
    "not allow a move if neither flat nor diagonal is possible" in {
      (fromMock.flatLineTo _) expects (toSquare, 1) returns (None)
      (fromMock.diagonalLineTo _) expects (toSquare, 1) returns (None)
      king.pathFor(fromMock, toSquare) should be(None)
    }
  }

  "A Queen" should {
    val queen = new Queen {}
    "allow a flat move of eight" in {
      (fromMock.diagonalLineTo _) expects (toSquare, 8) anyNumberOfTimes() returns (None)
      (fromMock.flatLineTo _) expects (toSquare, 8) returns (Some(Set.empty[Square]))
      queen.pathFor(fromMock, toSquare) should be(Some(Set.empty[Square]))
    }
    "allow a diagonal move of eight" in {
      (fromMock.flatLineTo _) expects (toSquare, 8) anyNumberOfTimes() returns (None)
      (fromMock.diagonalLineTo _) expects (toSquare, 8) returns (Some(Set.empty[Square]))
      queen.pathFor(fromMock, toSquare) should be(Some(Set.empty[Square]))
    }
    "not allow a move if neither flat nor diagonal is possible" in {
      (fromMock.flatLineTo _) expects (toSquare, 8) returns (None)
      (fromMock.diagonalLineTo _) expects (toSquare, 8) returns (None)
      queen.pathFor(fromMock, toSquare) should be(None)
    }
  }

  "A Bishop" should {
    val bishop = new Bishop {}
    "allow a diagonal move of eight" in {
      (fromMock.diagonalLineTo _) expects (toSquare, 8) returns (Some(Set.empty[Square]))
      bishop.pathFor(fromMock, toSquare) should be(Some(Set.empty[Square]))
    }
    "not allow a move if a diagonal is not possible" in {
      (fromMock.diagonalLineTo _) expects (toSquare, 8) returns (None)
      (fromMock.flatLineTo _) expects (*, *) never()
      bishop.pathFor(fromMock, toSquare) should be(None)
    }
  }

  "A Knight" should {
    val knight = new Knight {}
    val from = Square(3, 3)
    "allow a move two right and one up" in {
      knight.pathFor(from, Square(from.x + 2, from.y + 1)) should be (Some(Set.empty[Square]))
    }
    "allow a move two right and one down" in {
      knight.pathFor(from, Square(from.x + 2, from.y - 1)) should be (Some(Set.empty[Square]))

    }
    "allow a move two left and one up" in {
      knight.pathFor(from, Square(from.x - 2, from.y + 1)) should be (Some(Set.empty[Square]))

    }
    "allow a move two left and one down" in {
      knight.pathFor(from, Square(from.x - 2, from.y - 1)) should be (Some(Set.empty[Square]))
    }
    "allow a move two up and one left" in {
      knight.pathFor(from, Square(from.x - 1, from.y + 2)) should be (Some(Set.empty[Square]))

    }
    "allow a move two up and one right" in {
      knight.pathFor(from, Square(from.x + 1, from.y + 2)) should be (Some(Set.empty[Square]))
    }
    "allow a move two down and one left" in {
      knight.pathFor(from, Square(from.x - 1, from.y - 2)) should be (Some(Set.empty[Square]))
    }
    "allow a move two down and one right" in {
      knight.pathFor(from, Square(from.x + 1, from.y - 2)) should be (Some(Set.empty[Square]))
    }
    "not allow a move one diagonally" in {
      knight.pathFor(from, Square(from.x + 1, from.y + 1)) should be (None)
    }
    "not allow a move two diagonally" in {
      knight.pathFor(from, Square(from.x + 2, from.y + 2)) should be (None)
    }
    "not allow a move two flat" in {
      knight.pathFor(from, Square(from.x, from.y + 2)) should be (None)
    }
  }

  "A Rook" should {
    val rook = new Rook {}
    "allow a flat move of eight" in {
      (fromMock.flatLineTo _) expects (toSquare, 8) returns (Some(Set.empty[Square]))
      rook.pathFor(fromMock, toSquare) should be(Some(Set.empty[Square]))
    }
    "not allow a move if a flat move is not possible" in {
      (fromMock.flatLineTo _) expects (toSquare, 8) returns (None)
      (fromMock.diagonalLineTo _) expects (*,*) never()
      rook.pathFor(fromMock, toSquare) should be(None)

    }
  }

  "A white pawn" should {
    "allow a move of one up if not taking" in {

    }
    "allow a move of two up if not taking and in the first row" in {

    }
    "not allow a move of one up if taking" in {

    }
    "not allow a move of two up if taking" in {

    }
    "not allow a move of one down" in {

    }
    "allow a move of one diagonally left if taking" in {

    }
    "allow a move of one diagonally right if taking" in {

    }
    "not allow a move of one diagonally left if not taking" in {

    }
    "not allow a move of one diagonally right if not taking" in {

    }
    "not allow a move of one diagonally left and down" in {

    }
    "not allow a move of one diagonally right and down" in {

    }
  }

  "A black pawn" should {
    "allow a move of one down if not taking" in {

    }
    "allow a move of two down if not taking and in the first row" in {

    }
    "not allow a move of one down if taking" in {

    }
    "not allow a move of two down if taking" in {

    }
    "not allow a move of one up" in {

    }
    "allow a move of one diagonally left if taking" in {

    }
    "allow a move of one diagonally right if taking" in {

    }
    "not allow a move of one diagonally left if not taking" in {

    }
    "not allow a move of one diagonally right if not taking" in {

    }
    "not allow a move of one diagonally left and up" in {

    }
    "not allow a move of one diagonally right and up" in {

    }
  }
}
