package chess.domain

import org.mockito.Mockito._
import org.scalatest._
import org.scalatest.mock.MockitoSugar

class PieceSpec extends WordSpec with Matchers with OneInstancePerTest with MockitoSugar with BeforeAndAfter {

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
  val toMock = mock[MockSquare]

  before {
    reset(fromMock, toMock)
  }

  "A King" should {
    val king = WhiteKing
    "allow a flat move of one" in {
      when(fromMock.diagonalLineTo(toMock, 1)) thenReturn (None)
      when(fromMock.flatLineTo(toMock, 1)) thenReturn (Some(Set.empty[Square]))
      king.pathFor(fromMock, toMock, false) should be(Some(Set.empty[Square]))
      king.pathFor(fromMock, toMock, true) should be(Some(Set.empty[Square]))
    }
    "allow a diagonal move of one" in {
      when(fromMock.flatLineTo(toMock, 1)) thenReturn (None)
      when(fromMock.diagonalLineTo(toMock, 1)) thenReturn (Some(Set.empty[Square]))
      king.pathFor(fromMock, toMock, false) should be(Some(Set.empty[Square]))
      king.pathFor(fromMock, toMock, true) should be(Some(Set.empty[Square]))
    }
    "not allow a move if neither flat nor diagonal is possible" in {
      when(fromMock.flatLineTo(toMock, 1)) thenReturn (None)
      when(fromMock.diagonalLineTo(toMock, 1)) thenReturn (None)
      king.pathFor(fromMock, toMock, false) should be(None)
      king.pathFor(fromMock, toMock, true) should be(None)
    }
    "be king" in {
      king.isKing should be(true)
    }
  }

  "A Queen" should {
    val queen = WhiteQueen
    "allow a flat move of eight" in {
      when(fromMock.diagonalLineTo(toMock, 8)) thenReturn (None)
      when(fromMock.flatLineTo(toMock, 8)) thenReturn (Some(Set.empty[Square]))
      queen.pathFor(fromMock, toMock, false) should be(Some(Set.empty[Square]))
      queen.pathFor(fromMock, toMock, true) should be(Some(Set.empty[Square]))
    }
    "allow a diagonal move of eight" in {
      when(fromMock.flatLineTo(toMock, 8)) thenReturn (None)
      when(fromMock.diagonalLineTo(toMock, 8)) thenReturn (Some(Set.empty[Square]))
      queen.pathFor(fromMock, toMock, false) should be(Some(Set.empty[Square]))
      queen.pathFor(fromMock, toMock, true) should be(Some(Set.empty[Square]))
    }
    "not allow a move if neither flat nor diagonal is possible" in {
      when(fromMock.flatLineTo(toMock, 8)) thenReturn (None)
      when(fromMock.diagonalLineTo(toMock, 8)) thenReturn (None)
      queen.pathFor(fromMock, toMock, false) should be(None)
      queen.pathFor(fromMock, toMock, true) should be(None)
    }
    "not be king" in {
      queen.isKing should be(false)
    }
  }

  "A Bishop" should {
    val bishop = WhiteBishop
    "allow a diagonal move of eight" in {
      when(fromMock.diagonalLineTo(toMock, 8)) thenReturn (Some(Set.empty[Square]))
      bishop.pathFor(fromMock, toMock, false) should be(Some(Set.empty[Square]))
      bishop.pathFor(fromMock, toMock, true) should be(Some(Set.empty[Square]))
    }
    "not allow a move if a diagonal is not possible" in {
      when(fromMock.diagonalLineTo(toMock, 8)) thenReturn (None)
      bishop.pathFor(fromMock, toMock, false) should be(None)
      bishop.pathFor(fromMock, toMock, true) should be(None)
    }
    "not be king" in {
      bishop.isKing should be(false)
    }
  }

  "A Knight" should {
    val knight = WhiteKnight
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
    "not be king" in {
      knight.isKing should be(false)
    }
  }

  "A Rook" should {
    val rook = WhiteRook
    "allow a flat move of eight" in {
      when(fromMock.flatLineTo(toMock, 8)) thenReturn (Some(Set.empty[Square]))
      rook.pathFor(fromMock, toMock, false) should be(Some(Set.empty[Square]))
      rook.pathFor(fromMock, toMock, true) should be(Some(Set.empty[Square]))
    }
    "not allow a move if a flat move is not possible" in {
      when(fromMock.flatLineTo(toMock, 8)) thenReturn (None)
      rook.pathFor(fromMock, toMock, false) should be(None)
      rook.pathFor(fromMock, toMock, true) should be(None)
    }
    "not be king" in {
      rook.isKing should be(false)
    }
  }

  "A white pawn" should {
    "allow a move of one up if not taking" in {
      when(fromMock.y).thenReturn(White.pawnRow)
      when(toMock.y).thenReturn(White.pawnRow + 1)
      when(fromMock.flatLineTo(toMock, 2)).thenReturn(Some(Set.empty[Square]))
      WhitePawn.pathFor(fromMock, toMock, false) should be (Some(Set.empty[Square]))
    }
    "allow a move of two up if not taking and in the first row" in {
      when(fromMock.y).thenReturn(White.pawnRow)
      when(toMock.y).thenReturn(White.pawnRow + 2)
      val jumpedSquare = Square(2,2)
      when(fromMock.flatLineTo(toMock, 2)).thenReturn(Some(Set(jumpedSquare)))
      WhitePawn.pathFor(fromMock, toMock, false) should be (Some(Set(jumpedSquare)))
    }
    "not allow a move of two up if not in the first row" in {
      when(fromMock.y).thenReturn(White.pawnRow + 1)
      when(toMock.y).thenReturn(White.pawnRow + 3)
      val jumpedSquare = Square(2,2)
      when(fromMock.flatLineTo(toMock, 1)).thenReturn(None)
      WhitePawn.pathFor(fromMock, toMock, false) should be (None)
    }
    "not allow a move of one up if taking" in {
      when(fromMock.y).thenReturn(White.pawnRow)
      when(toMock.y).thenReturn(White.pawnRow + 1)
      when(fromMock.diagonalLineTo(toMock, 1)).thenReturn(None)
      when(fromMock.flatLineTo(toMock, 1)).thenReturn(Some(Set.empty[Square]))
      WhitePawn.pathFor(fromMock, toMock, true) should be (None)
    }
    "not allow a move of two up if taking" in {
      when(fromMock.y).thenReturn(White.pawnRow)
      when(toMock.y).thenReturn(White.pawnRow + 2)
      val jumpedSquare = Square(2,2)
      when(fromMock.diagonalLineTo(toMock, 1)).thenReturn(None)
      when(fromMock.flatLineTo(toMock, 2)).thenReturn(Some(Set(jumpedSquare)))
      WhitePawn.pathFor(fromMock, toMock, true) should be (None)
    }
    "not allow a move of one down" in {
      when(fromMock.y).thenReturn(White.pawnRow)
      when(toMock.y).thenReturn(White.pawnRow - 1)
      WhitePawn.pathFor(fromMock, toMock, false) should be (None)
    }
    "allow a move of one diagonally if taking" in {
      when(fromMock.y).thenReturn(White.pawnRow)
      when(toMock.y).thenReturn(White.pawnRow + 1)
      when(fromMock.diagonalLineTo(toMock, 1)).thenReturn(Some(Set.empty[Square]))
      WhitePawn.pathFor(fromMock, toMock, true) should be (Some(Set.empty[Square]))
    }
    "not allow a move of one diagonally if not taking" in {
      when(fromMock.y).thenReturn(White.pawnRow)
      when(toMock.y).thenReturn(White.pawnRow + 1)
      when(fromMock.flatLineTo(toMock, 2)).thenReturn(None)
      when(fromMock.diagonalLineTo(toMock, 1)).thenReturn(Some(Set.empty[Square]))
      WhitePawn.pathFor(fromMock, toMock, false) should be (None)
    }
    "not allow a move of one diagonally back" in {
      when(fromMock.y).thenReturn(White.pawnRow)
      when(toMock.y).thenReturn(White.pawnRow - 1)
      when(fromMock.diagonalLineTo(toMock, 1)).thenReturn(Some(Set.empty[Square]))
      WhitePawn.pathFor(fromMock, toMock, true) should be (None)
    }
    "not be king" in {
      WhitePawn.isKing should be(false)
    }
  }

  "A black pawn" should {
    "allow a move of one up if not taking" in {
      when(fromMock.y).thenReturn(Black.pawnRow)
      when(toMock.y).thenReturn(Black.pawnRow - 1)
      when(fromMock.flatLineTo(toMock, 2)).thenReturn(Some(Set.empty[Square]))
      BlackPawn.pathFor(fromMock, toMock, false) should be (Some(Set.empty[Square]))
    }
    "allow a move of two up if not taking and in the first row" in {
      when(fromMock.y).thenReturn(Black.pawnRow)
      when(toMock.y).thenReturn(Black.pawnRow - 2)
      val jumpedSquare = Square(2,2)
      when(fromMock.flatLineTo(toMock, 2)).thenReturn(Some(Set(jumpedSquare)))
      BlackPawn.pathFor(fromMock, toMock, false) should be (Some(Set(jumpedSquare)))
    }
    "not allow a move of two up if not in the first row" in {
      when(fromMock.y).thenReturn(Black.pawnRow - 1)
      when(toMock.y).thenReturn(Black.pawnRow - 3)
      val jumpedSquare = Square(2,2)
      when(fromMock.flatLineTo(toMock, 1)).thenReturn(None)
      BlackPawn.pathFor(fromMock, toMock, false) should be (None)
    }
    "not allow a move of one up if taking" in {
      when(fromMock.y).thenReturn(Black.pawnRow)
      when(toMock.y).thenReturn(Black.pawnRow - 1)
      when(fromMock.diagonalLineTo(toMock, 1)).thenReturn(None)
      when(fromMock.flatLineTo(toMock, 1)).thenReturn(Some(Set.empty[Square]))
      BlackPawn.pathFor(fromMock, toMock, true) should be (None)
    }
    "not allow a move of two up if taking" in {
      when(fromMock.y).thenReturn(Black.pawnRow)
      when(toMock.y).thenReturn(Black.pawnRow - 2)
      val jumpedSquare = Square(2,2)
      when(fromMock.diagonalLineTo(toMock, 1)).thenReturn(None)
      when(fromMock.flatLineTo(toMock, 2)).thenReturn(Some(Set(jumpedSquare)))
      BlackPawn.pathFor(fromMock, toMock, true) should be (None)
    }
    "not allow a move of one down" in {
      when(fromMock.y).thenReturn(Black.pawnRow)
      when(toMock.y).thenReturn(Black.pawnRow + 1)
      BlackPawn.pathFor(fromMock, toMock, false) should be (None)
    }
    "allow a move of one diagonally if taking" in {
      when(fromMock.y).thenReturn(Black.pawnRow)
      when(toMock.y).thenReturn(Black.pawnRow - 1)
      when(fromMock.diagonalLineTo(toMock, 1)).thenReturn(Some(Set.empty[Square]))
      BlackPawn.pathFor(fromMock, toMock, true) should be (Some(Set.empty[Square]))
    }
    "not allow a move of one diagonally if not taking" in {
      when(fromMock.y).thenReturn(Black.pawnRow)
      when(toMock.y).thenReturn(Black.pawnRow - 1)
      when(fromMock.flatLineTo(toMock, 2)).thenReturn(None)
      when(fromMock.diagonalLineTo(toMock, 1)).thenReturn(Some(Set.empty[Square]))
      BlackPawn.pathFor(fromMock, toMock, false) should be (None)
    }
    "not allow a move of one diagonally back" in {
      when(fromMock.y).thenReturn(Black.pawnRow)
      when(toMock.y).thenReturn(Black.pawnRow + 1)
      when(fromMock.diagonalLineTo(toMock, 1)).thenReturn(Some(Set.empty[Square]))
      BlackPawn.pathFor(fromMock, toMock, true) should be (None)
    }
    "not be king" in {
      BlackPawn.isKing should be(false)
    }
  }
}
