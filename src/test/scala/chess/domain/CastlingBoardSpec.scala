package chess.domain

import org.mockito.ArgumentMatchers._
import org.mockito.Mockito.{reset, when}
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, Matchers, WordSpec}

class CastlingBoardSpec extends WordSpec with Matchers with MockitoSugar with BeforeAndAfter {

  val previousMock = mock[Board]
  val whiteKing = mock[Piece]
  val whiteQueensideRook = mock[Piece]
  val whiteKingsideRook = mock[Piece]
  val blackPiece = mock[Piece]
  val blackKing = mock[Piece]
  val whiteKingSquare = Square(4,0)
  val whiteQueensideKingSquare = Square(2, 0)
  val whiteQueensideRookCastledSquare = Square(3, 0)
  val whiteQueensideRookInitialSquare = Square(0, 0)
  val whiteKingsideKingSquare = Square(6, 0)
  val whiteKingsideRookCastledSquare = Square(5, 0)
  val whiteKingsideRookInitialSquare = Square(7, 0)
  val blackPieceSquare = Square(5, 5)
  val blackKingSquare = Square(4, 6)
  val emptySquare = Square(6, 6)
  val emptySquare1 = Square(7, 7)
  val previousPositions = Map(
    whiteKingsideRookInitialSquare -> whiteKingsideRook,
    whiteQueensideRookInitialSquare -> whiteQueensideRook,
    whiteKingSquare -> whiteKing,
    blackPieceSquare -> blackPiece,
    blackKingSquare -> blackKing
  )

  before {
    reset(previousMock)
    when(whiteQueensideRook.colour).thenReturn(White)
    when(whiteKingsideRook.colour).thenReturn(White)
    when(whiteKing.colour).thenReturn(White)
    when(whiteKing.isKing).thenReturn(true)
    when(blackPiece.colour).thenReturn(Black)
    when(blackKing.colour).thenReturn(Black)
    when(blackKing.isKing).thenReturn(true)

    when(whiteKing.pathFor(any(), any(), any())).thenReturn(None)
    when(whiteKingsideRook.pathFor(any(), any(), any())).thenReturn(None)
    when(whiteQueensideRook.pathFor(any(), any(), any())).thenReturn(None)
    when(blackKing.pathFor(any(), any(), any())).thenReturn(None)
    when(blackPiece.pathFor(any(), any(), any())).thenReturn(None)

    when(previousMock.positions).thenReturn(previousPositions)
    when(previousMock.toMove).thenReturn(Some(White))
    when(previousMock.valid).thenReturn(true)
    when(previousMock.result).thenReturn(None)
    when(previousMock.neverMoved(any())).thenReturn(true)
  }

  "A CastlingBoard" should {
    "have the correct fromOption" in {
      CastlingBoard(whiteKingSquare, whiteKingsideKingSquare, previousMock).lastFrom shouldBe Some(whiteKingSquare)
    }
    "have the correct toOption" in {
      CastlingBoard(whiteKingSquare, whiteKingsideKingSquare, previousMock).lastTo shouldBe Some(whiteKingsideKingSquare)
    }
    "say hasNeverMoved is false if previous hasNeverMoved is false" in {
      val board = CastlingBoard(whiteKingSquare, whiteKingsideKingSquare, previousMock)
      when(previousMock.neverMoved(emptySquare)).thenReturn(false)
      board.neverMoved(emptySquare) shouldBe false
    }
    "say hasNeverMoved is false if it equals from" in {
      val board = CastlingBoard(whiteKingSquare, whiteKingsideKingSquare, previousMock)
      when(previousMock.neverMoved(whiteKingSquare)).thenReturn(true)
      board.neverMoved(whiteKingSquare) shouldBe false
    }
    "say hasNeverMoved is true if it doesn't equal from and previous value is true" in {
      val board = CastlingBoard(whiteKingSquare, whiteKingsideKingSquare, previousMock)
      when(previousMock.neverMoved(emptySquare)).thenReturn(true)
      board.neverMoved(emptySquare) shouldBe true
    }
    "say toMove is White if previous game's toMove is Black" in {
      when(previousMock.toMove).thenReturn(Some(Black))
      CastlingBoard(whiteKingSquare, whiteKingsideKingSquare, previousMock).toMove shouldBe Some(White)
    }
    "say toMove is Black if previous game's toMove is White" in {
      when(previousMock.toMove).thenReturn(Some(White))
      CastlingBoard(whiteKingSquare, whiteKingsideKingSquare, previousMock).toMove shouldBe Some(Black)
    }
    "update the currentPositions correctly kingside" in {
      val board = CastlingBoard(whiteKingSquare, whiteKingsideKingSquare, previousMock)
      board.positions shouldBe Map(
        whiteKingsideKingSquare -> whiteKing,
        whiteKingsideRookCastledSquare -> whiteKingsideRook,
        whiteQueensideRookInitialSquare -> whiteQueensideRook,
        blackPieceSquare -> blackPiece,
        blackKingSquare -> blackKing
      )
    }
    "update the currentPositions correctly queenside" in {
      val board = CastlingBoard(whiteKingSquare, whiteQueensideKingSquare, previousMock)
      board.positions shouldBe Map(
        whiteQueensideKingSquare -> whiteKing,
        whiteQueensideRookCastledSquare -> whiteQueensideRook,
        whiteKingsideRookInitialSquare -> whiteKingsideRook,
        blackPieceSquare -> blackPiece,
        blackKingSquare -> blackKing
      )
    }
    "not be valid if there is no piece at the from square" in {
      when(previousMock.positions).thenReturn(previousPositions - whiteKingSquare)
      val board = CastlingBoard(whiteKingSquare, whiteKingsideKingSquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if the piece at from square is not toMove colour" in {
      when(previousMock.toMove).thenReturn(Some(Black))
      val board = CastlingBoard(whiteKingSquare, whiteKingsideKingSquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if starting in check" in {
      when(blackPiece.pathFor(blackPieceSquare, whiteKingSquare, true)).thenReturn(Some(Set.empty[Square]))
      val board = CastlingBoard(whiteKingSquare, whiteKingsideKingSquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if going through check" in {
      when(blackPiece.pathFor(blackPieceSquare, whiteKingsideRookCastledSquare, true)).thenReturn(Some(Set.empty[Square]))
      val board = CastlingBoard(whiteKingSquare, whiteKingsideKingSquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if ending in check" in {
      when(blackPiece.pathFor(blackPieceSquare, whiteKingsideKingSquare, true)).thenReturn(Some(Set.empty[Square]))
      val board = CastlingBoard(whiteKingSquare, whiteKingsideKingSquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if path not clear" in {
      when(previousMock.positions).thenReturn(previousPositions + (whiteKingsideKingSquare -> blackPiece))
      val board = CastlingBoard(whiteKingSquare, whiteKingsideKingSquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if king has moved" in {
      when(previousMock.neverMoved(whiteKingSquare)).thenReturn(false)
      val board = CastlingBoard(whiteKingSquare, whiteKingsideKingSquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if rook has moved" in {
      when(previousMock.neverMoved(whiteKingsideRookInitialSquare)).thenReturn(false)
      val board = CastlingBoard(whiteKingSquare, whiteKingsideKingSquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if to square wrong" in {
      val board = CastlingBoard(whiteKingSquare, whiteKingsideRookCastledSquare, previousMock)
      board.valid shouldBe false
    }
    "be valid kingside" in {
      val board = CastlingBoard(whiteKingSquare, whiteKingsideKingSquare, previousMock)
      board.valid shouldBe true
    }
    "be valid queenside" in {
      val board = CastlingBoard(whiteKingSquare, whiteQueensideKingSquare, previousMock)
      board.valid shouldBe true
    }
    "not have a result when there's no check" in {
      when(whiteKingsideRook.pathFor(whiteKingsideRookCastledSquare, blackKingSquare, true)).thenReturn(None)
      val board = CastlingBoard(whiteKingSquare, whiteKingsideKingSquare, previousMock)
      board.result shouldBe None
    }
    "not have a result when there's check that can be escaped by moving" in {
      when(whiteKingsideRook.pathFor(whiteKingsideRookCastledSquare, blackKingSquare, true)).thenReturn(Some(Set.empty[Square]))
      val board = CastlingBoard(whiteKingSquare, whiteKingsideKingSquare, previousMock)
      when(blackKing.pathFor(blackKingSquare, emptySquare1, false)).thenReturn(Some(Set.empty[Square]))
      board.result shouldBe None
    }
    "not have a result when there's check that can be escaped by blocking" in {
      when(whiteKingsideRook.pathFor(whiteKingsideRookCastledSquare, blackKingSquare, true)).thenReturn(Some(Set(emptySquare1)))
      val board = CastlingBoard(whiteKingSquare, whiteKingsideKingSquare, previousMock)
      when(blackPiece.pathFor(blackPieceSquare, emptySquare1, false)).thenReturn(Some(Set.empty[Square]))
      board.result shouldBe None
    }
    "not have a result when there's check that can be escaped by taking" in {
      when(whiteKingsideRook.pathFor(whiteKingsideRookCastledSquare, blackKingSquare, true)).thenReturn(Some(Set.empty[Square]))
      val board = CastlingBoard(whiteKingSquare, whiteKingsideKingSquare, previousMock)
      when(blackPiece.pathFor(blackPieceSquare, whiteKingsideRookCastledSquare, true)).thenReturn(Some(Set.empty[Square]))
      board.result shouldBe None
    }
    "have a checkmate result when there's check that can't be escaped" in {
      when(whiteKingsideRook.pathFor(whiteKingsideRookCastledSquare, blackKingSquare, true)).thenReturn(Some(Set.empty[Square]))
      val board = CastlingBoard(whiteKingSquare, whiteKingsideKingSquare, previousMock)
      board.result shouldBe Some(Checkmate(Black))
      board.toMove shouldBe None
    }
  }
}
