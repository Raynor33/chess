package chess.core.board

import chess.core.{Black, Checkmate, Piece, Square, White}
import org.mockito.Mockito.reset
import org.mockito.Mockito.when
import org.mockito.ArgumentMatchers._
import org.scalatest.{BeforeAndAfter, Matchers, WordSpec}
import org.scalatest.mock.MockitoSugar

class StandardBoardSpec extends WordSpec with Matchers with MockitoSugar with BeforeAndAfter {

  val previousMock = mock[Board]
  val whitePiece = mock[Piece]
  val whiteKing = mock[Piece]
  val blackPiece = mock[Piece]
  val blackKing = mock[Piece]
  val whitePieceSquare = Square(1, 4)
  val whiteKingSquare = Square(3, 2)
  val blackPieceSquare = Square(5, 5)
  val blackKingSquare = Square(4, 6)
  val emptySquare = Square(6, 6)
  val emptySquare1 = Square(7, 7)
  val previousPositions = Map(
    whitePieceSquare -> whitePiece,
    whiteKingSquare -> whiteKing,
    blackPieceSquare -> blackPiece,
    blackKingSquare -> blackKing
  )

  before {
    reset(previousMock)
    when(whitePiece.colour).thenReturn(White)
    when(whiteKing.colour).thenReturn(White)
    when(whiteKing.isKing).thenReturn(true)
    when(blackPiece.colour).thenReturn(Black)
    when(blackKing.colour).thenReturn(Black)
    when(blackKing.isKing).thenReturn(true)

    when(whiteKing.pathFor(any(), any(), any())).thenReturn(None)
    when(whitePiece.pathFor(any(), any(), any())).thenReturn(None)
    when(blackKing.pathFor(any(), any(), any())).thenReturn(None)
    when(blackPiece.pathFor(any(), any(), any())).thenReturn(None)

    when(previousMock.currentPositions).thenReturn(previousPositions)
    when(previousMock.toMove).thenReturn(White)
    when(previousMock.valid).thenReturn(true)
    when(previousMock.result).thenReturn(None)
  }

  "A StandardBoard" should {
    "say hasNeverMoved is false if previous hasNeverMoved is false" in {
      val board = StandardBoard(whitePieceSquare, blackPieceSquare, previousMock)
      when(previousMock.hasNeverMoved(emptySquare)).thenReturn(false)
      board.hasNeverMoved(emptySquare) shouldBe false
    }
    "say hasNeverMoved is false if it equals from" in {
      val board = StandardBoard(whitePieceSquare, blackPieceSquare, previousMock)
      when(previousMock.hasNeverMoved(whitePieceSquare)).thenReturn(true)
      board.hasNeverMoved(whitePieceSquare) shouldBe false
    }
    "say hasNeverMoved is true if it doesn't equal from and previous value is true" in {
      val board = StandardBoard(whitePieceSquare, blackPieceSquare, previousMock)
      when(previousMock.hasNeverMoved(emptySquare)).thenReturn(true)
      board.hasNeverMoved(emptySquare) shouldBe true
    }
    "say toMove is White if previous game's toMove is Black" in {
      when(previousMock.toMove).thenReturn(Black)
      StandardBoard(blackPieceSquare, emptySquare, previousMock).toMove shouldBe White
    }
    "say toMove is Black if previous game's toMove is White" in {
      when(previousMock.toMove).thenReturn(White)
      StandardBoard(whitePieceSquare, emptySquare, previousMock).toMove shouldBe Black
    }
    "update the currentPositions correctly for a move into space" in {
      val board = StandardBoard(whitePieceSquare, emptySquare, previousMock)
      board.currentPositions shouldBe Map(
        emptySquare -> whitePiece,
        whiteKingSquare -> whiteKing,
        blackPieceSquare -> blackPiece,
        blackKingSquare -> blackKing
      )
    }
    "update the currentPositions correctly for a move when taking" in {
      val board = StandardBoard(whiteKingSquare, blackPieceSquare, previousMock)
      board.currentPositions shouldBe Map(
        whitePieceSquare -> whitePiece,
        blackPieceSquare -> whiteKing,
        blackKingSquare -> blackKing
      )
    }
    "not be valid if there is no piece at the from square" in {
      val board = StandardBoard(emptySquare, blackPieceSquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if the piece at from square is not toMove colour" in {
      when(blackPiece.pathFor(blackPieceSquare, emptySquare, false)).thenReturn(Some(Set.empty[Square]))
      val board = StandardBoard(blackPieceSquare, emptySquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if the path is blocked by a piece of the same colour" in {
      when(whitePiece.pathFor(whitePieceSquare, emptySquare, false)).thenReturn(Some(Set(whiteKingSquare)));
      val board = StandardBoard(whitePieceSquare, emptySquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if the destination is blocked by a piece of the same colour" in {
      when(whitePiece.pathFor(whitePieceSquare, whiteKingSquare, true)).thenReturn(Some(Set.empty[Square]))
      val board = StandardBoard(whitePieceSquare, whiteKingSquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if the path is blocked by a piece of the opposite colour" in {
      when(whitePiece.pathFor(whitePieceSquare, emptySquare, false)).thenReturn(Some(Set(blackPieceSquare)));
      val board = StandardBoard(whitePieceSquare, emptySquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if there is no path" in {
      when(whitePiece.pathFor(whitePieceSquare, emptySquare, false)).thenReturn(None)
      val board = StandardBoard(whitePieceSquare, emptySquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if it ends in check" in {
      when(blackPiece.pathFor(blackPieceSquare, whiteKingSquare, true)).thenReturn(Some(Set.empty[Square]))
      when(whitePiece.pathFor(whitePieceSquare, emptySquare, false)).thenReturn(Some(Set.empty[Square]))
      val board = StandardBoard(whitePieceSquare, emptySquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if the previous isn't valid" in {
      when(previousMock.valid).thenReturn(false)
      when(whitePiece.pathFor(whitePieceSquare, emptySquare, false)).thenReturn(Some(Set.empty[Square]))
      val board = StandardBoard(whitePieceSquare, emptySquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if the previous is complete" in {
      when(previousMock.result).thenReturn(Some(Checkmate(White)))
      when(whitePiece.pathFor(whitePieceSquare, emptySquare, false)).thenReturn(Some(Set.empty[Square]))
      val board = StandardBoard(whitePieceSquare, emptySquare, previousMock)
      board.valid shouldBe false
    }
    "be valid if there is a clear path and the destination is empty" in {
      when(whitePiece.pathFor(whitePieceSquare, emptySquare, false)).thenReturn(Some(Set.empty[Square]))
      val board = StandardBoard(whitePieceSquare, emptySquare, previousMock)
      board.valid shouldBe true
    }
    "be valid if there is a clear path and the destination has an opponent" in {
      when(whitePiece.pathFor(whitePieceSquare, blackPieceSquare, true)).thenReturn(Some(Set.empty[Square]))
      val board = StandardBoard(whitePieceSquare, blackPieceSquare, previousMock)
      board.valid shouldBe true
    }
    "be valid if potential attack on king is blocked" in {
      when(blackPiece.pathFor(blackPieceSquare, whiteKingSquare, true)).thenReturn(Some(Set(blackPieceSquare)))
      when(whitePiece.pathFor(whitePieceSquare, blackPieceSquare, true)).thenReturn(Some(Set.empty[Square]))
      val board = StandardBoard(whitePieceSquare, blackPieceSquare, previousMock)
      board.valid shouldBe true
    }
    "not have a result when there's no check" in {
      when(whitePiece.pathFor(whitePieceSquare, emptySquare, false)).thenReturn(Some(Set.empty[Square]))
      val board = StandardBoard(whitePieceSquare, emptySquare, previousMock)
      when(whitePiece.pathFor(emptySquare, blackKingSquare, true)).thenReturn(None)
      board.result shouldBe None
    }
    "not have a result when there's check that can be escaped by moving" in {
      when(whitePiece.pathFor(whitePieceSquare, emptySquare, false)).thenReturn(Some(Set.empty[Square]))
      val board = StandardBoard(whitePieceSquare, emptySquare, previousMock)
      when(whitePiece.pathFor(emptySquare, blackKingSquare, true)).thenReturn(None)
      when(blackKing.pathFor(blackKingSquare, emptySquare1, false)).thenReturn(Some(Set.empty[Square]))
      board.result shouldBe None
    }
    "not have a result when there's check that can be escaped by blocking" in {
      when(whitePiece.pathFor(whitePieceSquare, emptySquare, false)).thenReturn(Some(Set.empty[Square]))
      val board = StandardBoard(whitePieceSquare, emptySquare, previousMock)
      when(whitePiece.pathFor(emptySquare, blackKingSquare, true)).thenReturn(Some(Set(emptySquare1)))
      when(blackPiece.pathFor(blackPieceSquare, emptySquare1, false)).thenReturn(Some(Set.empty[Square]))
      board.result shouldBe None
    }
    "not have a result when there's check that can be escaped by taking" in {
      when(whitePiece.pathFor(whitePieceSquare, emptySquare, false)).thenReturn(Some(Set.empty[Square]))
      val board = StandardBoard(whitePieceSquare, emptySquare, previousMock)
      when(whitePiece.pathFor(emptySquare, blackKingSquare, true)).thenReturn(Some(Set.empty[Square]))
      when(blackPiece.pathFor(blackPieceSquare, emptySquare, true)).thenReturn(Some(Set.empty[Square]))
      board.result shouldBe None
    }
    "have a checkmate result when there's check that can't be escaped" in {
      when(whitePiece.pathFor(whitePieceSquare, emptySquare, false)).thenReturn(Some(Set.empty[Square]))
      val board = StandardBoard(whitePieceSquare, emptySquare, previousMock)
      when(whitePiece.pathFor(emptySquare, blackKingSquare, true)).thenReturn(Some(Set.empty[Square]))
      board.result shouldBe Some(Checkmate(Black))
    }
  }
}
