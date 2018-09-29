package chess.core.board

import chess.core.{Black, Checkmate, Pawn, Piece, Square, White}
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito.{reset, when}
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, Matchers, WordSpec}

class EnPassantBoardSpec extends WordSpec with Matchers with MockitoSugar with BeforeAndAfter {

  val previousMock = mock[MoveBoard]
  val whitePawn = mock[Pawn]
  val whitePiece = mock[Piece]
  val whiteKing = mock[Piece]
  val blackPawn = mock[Pawn]
  val blackPiece = mock[Piece]
  val blackKing = mock[Piece]
  val whitePawnSquare = Square(4, 4)
  val whitePieceSquare = Square(4, 0)
  val whiteKingSquare = Square(5, 0)
  val blackPawnSquare = Square(3, 4)
  val behindBlackPawnSquare = Square(3, 5)
  val blackPawnStartSquare = Square(3, 6)
  val blackPieceSquare = Square(5, 5)
  val blackKingSquare = Square(4, 6)
  val emptySquare = Square(6, 6)
  val emptySquare1 = Square(7, 7)
  val previousPositions = Map(
    whitePieceSquare -> whitePiece,
    whitePawnSquare -> whitePawn,
    whiteKingSquare -> whiteKing,
    blackPawnSquare -> blackPawn,
    blackPieceSquare -> blackPiece,
    blackKingSquare -> blackKing
  )

  before {
    reset(previousMock)
    when(whitePiece.colour).thenReturn(White)
    when(whitePawn.colour).thenReturn(White)
    when(whiteKing.colour).thenReturn(White)
    when(whiteKing.isKing).thenReturn(true)
    when(blackPawn.colour).thenReturn(Black)
    when(blackPiece.colour).thenReturn(Black)
    when(blackKing.colour).thenReturn(Black)
    when(blackKing.isKing).thenReturn(true)

    when(whiteKing.pathFor(any(), any(), any())).thenReturn(None)
    when(whitePawn.pathFor(any(), any(), any())).thenReturn(None)
    when(whitePiece.pathFor(any(), any(), any())).thenReturn(None)
    when(blackKing.pathFor(any(), any(), any())).thenReturn(None)
    when(blackPawn.pathFor(any(), any(), any())).thenReturn(None)
    when(blackPiece.pathFor(any(), any(), any())).thenReturn(None)

    when(previousMock.currentPositions).thenReturn(previousPositions)
    when(previousMock.toMove).thenReturn(White)
    when(previousMock.valid).thenReturn(true)
    when(previousMock.result).thenReturn(None)
  }

  "An EnPassantBoard" should {
    "say hasNeverMoved is false if previous hasNeverMoved is false" in {
      val board = EnPassantBoard(whitePawnSquare, behindBlackPawnSquare, previousMock)
      when(previousMock.hasNeverMoved(emptySquare)).thenReturn(false)
      board.hasNeverMoved(emptySquare) shouldBe false
    }
    "say hasNeverMoved is false if it equals from" in {
      val board = EnPassantBoard(whitePawnSquare, behindBlackPawnSquare, previousMock)
      when(previousMock.hasNeverMoved(whitePawnSquare)).thenReturn(true)
      board.hasNeverMoved(whitePawnSquare) shouldBe false
    }
    "say hasNeverMoved is true if it doesn't equal from and previous value is true" in {
      val board = EnPassantBoard(whitePawnSquare, behindBlackPawnSquare, previousMock)
      when(previousMock.hasNeverMoved(emptySquare)).thenReturn(true)
      board.hasNeverMoved(emptySquare) shouldBe true
    }
    "say toMove is White if previous game's toMove is Black" in {
      when(previousMock.toMove).thenReturn(Black)
      EnPassantBoard(whitePawnSquare, behindBlackPawnSquare, previousMock).toMove shouldBe White
    }
    "say toMove is Black if previous game's toMove is White" in {
      when(previousMock.toMove).thenReturn(White)
      EnPassantBoard(whitePawnSquare, behindBlackPawnSquare, previousMock).toMove shouldBe Black
    }
    "update the currentPositions correctly" in {
      val board = EnPassantBoard(whitePawnSquare, behindBlackPawnSquare, previousMock)
      board.currentPositions shouldBe Map(
        behindBlackPawnSquare -> whitePawn,
        whiteKingSquare -> whiteKing,
        whitePieceSquare -> whitePiece,
        blackPieceSquare -> blackPiece,
        blackKingSquare -> blackKing
      )
    }
    "not be valid if there is no piece at the from square" in {
      val board = EnPassantBoard(emptySquare, behindBlackPawnSquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if the piece at from square is not toMove colour" in {
      when(whitePawn.pathFor(whitePawnSquare, behindBlackPawnSquare, true)).thenReturn(Some(Set.empty[Square]))
      when(previousMock.from).thenReturn(blackPawnStartSquare)
      when(previousMock.to).thenReturn(blackPawnSquare)
      when(previousMock.toMove).thenReturn(Black)
      val board = EnPassantBoard(whitePawnSquare, behindBlackPawnSquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if the piece is not a pawn" in {
      when(previousMock.currentPositions).thenReturn(previousPositions + (whitePawnSquare -> whitePiece))
      when(whitePiece.pathFor(whitePawnSquare, behindBlackPawnSquare, true)).thenReturn(Some(Set.empty[Square]))
      when(previousMock.from).thenReturn(blackPawnStartSquare)
      when(previousMock.to).thenReturn(blackPawnSquare)
      val board = EnPassantBoard(whitePawnSquare, behindBlackPawnSquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if the victim piece is not a pawn" in {
      when(previousMock.currentPositions).thenReturn(previousPositions + (blackPawnSquare -> blackPiece))
      when(whitePawn.pathFor(whitePawnSquare, behindBlackPawnSquare, true)).thenReturn(Some(Set.empty[Square]))
      when(previousMock.from).thenReturn(blackPawnStartSquare)
      when(previousMock.to).thenReturn(blackPawnSquare)
      val board = EnPassantBoard(whitePawnSquare, behindBlackPawnSquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if the victim piece has only moved one" in {
      when(whitePawn.pathFor(whitePawnSquare, behindBlackPawnSquare, true)).thenReturn(Some(Set.empty[Square]))
      when(previousMock.from).thenReturn(behindBlackPawnSquare)
      when(previousMock.to).thenReturn(blackPawnSquare)
      val board = EnPassantBoard(whitePawnSquare, behindBlackPawnSquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if not a valid pawn capture" in {
      when(whitePawn.pathFor(whitePawnSquare, blackPawnStartSquare, true)).thenReturn(None)
      when(previousMock.from).thenReturn(behindBlackPawnSquare)
      when(previousMock.to).thenReturn(blackPawnSquare)
      val board = EnPassantBoard(whitePawnSquare, blackPawnStartSquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if it ends in check" in {
      when(whitePawn.pathFor(whitePawnSquare, behindBlackPawnSquare, true)).thenReturn(Some(Set.empty[Square]))
      when(previousMock.from).thenReturn(blackPawnStartSquare)
      when(previousMock.to).thenReturn(blackPawnSquare)
      when(blackPiece.pathFor(blackPieceSquare, whiteKingSquare, true)).thenReturn(Some(Set(whitePawnSquare)))
      val board = EnPassantBoard(whitePawnSquare, behindBlackPawnSquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if the previous isn't valid" in {
      when(whitePawn.pathFor(whitePawnSquare, behindBlackPawnSquare, true)).thenReturn(Some(Set.empty[Square]))
      when(previousMock.from).thenReturn(blackPawnStartSquare)
      when(previousMock.to).thenReturn(blackPawnSquare)
      when(previousMock.valid).thenReturn(false)
      val board = EnPassantBoard(whitePawnSquare, behindBlackPawnSquare, previousMock)
      board.valid shouldBe false
    }
    "not be valid if the previous is complete" in {
      when(whitePawn.pathFor(whitePawnSquare, behindBlackPawnSquare, true)).thenReturn(Some(Set.empty[Square]))
      when(previousMock.from).thenReturn(blackPawnStartSquare)
      when(previousMock.to).thenReturn(blackPawnSquare)
      when(previousMock.result).thenReturn(Some(Checkmate(White)))
      val board = EnPassantBoard(whitePawnSquare, behindBlackPawnSquare, previousMock)
      board.valid shouldBe false
    }
    "be valid if everything's fine" in {
      when(whitePawn.pathFor(whitePawnSquare, behindBlackPawnSquare, true)).thenReturn(Some(Set.empty[Square]))
      when(previousMock.from).thenReturn(blackPawnStartSquare)
      when(previousMock.to).thenReturn(blackPawnSquare)
      val board = EnPassantBoard(whitePawnSquare, behindBlackPawnSquare, previousMock)
      board.valid shouldBe true
    }
    "not have a result when there's no check" in {
      when(whitePawn.pathFor(whitePawnSquare, behindBlackPawnSquare, true)).thenReturn(Some(Set.empty[Square]))
      when(previousMock.from).thenReturn(blackPawnStartSquare)
      when(previousMock.to).thenReturn(blackPawnSquare)
      val board = EnPassantBoard(whitePawnSquare, behindBlackPawnSquare, previousMock)
      board.result shouldBe None
    }
    "not have a result when there's check that can be escaped by moving" in {
      when(whitePawn.pathFor(whitePawnSquare, behindBlackPawnSquare, true)).thenReturn(Some(Set.empty[Square]))
      when(previousMock.from).thenReturn(blackPawnStartSquare)
      when(previousMock.to).thenReturn(blackPawnSquare)
      val board = EnPassantBoard(whitePawnSquare, behindBlackPawnSquare, previousMock)
      when(whitePiece.pathFor(whitePieceSquare, blackKingSquare, true)).thenReturn(Some(Set.empty[Square]))
      when(blackKing.pathFor(blackKingSquare, emptySquare, false)).thenReturn(Some(Set.empty[Square]))
      board.result shouldBe None
    }
    "not have a result when there's check that can be escaped by blocking" in {
      when(whitePawn.pathFor(whitePawnSquare, behindBlackPawnSquare, true)).thenReturn(Some(Set.empty[Square]))
      when(previousMock.from).thenReturn(blackPawnStartSquare)
      when(previousMock.to).thenReturn(blackPawnSquare)
      val board = EnPassantBoard(whitePawnSquare, behindBlackPawnSquare, previousMock)
      when(whitePiece.pathFor(whitePieceSquare, blackKingSquare, true)).thenReturn(Some(Set(emptySquare)))
      when(blackPiece.pathFor(blackPieceSquare, emptySquare, false)).thenReturn(Some(Set.empty[Square]))
      board.result shouldBe None
    }
    "not have a result when there's check that can be escaped by taking" in {
      when(whitePawn.pathFor(whitePawnSquare, behindBlackPawnSquare, true)).thenReturn(Some(Set.empty[Square]))
      when(previousMock.from).thenReturn(blackPawnStartSquare)
      when(previousMock.to).thenReturn(blackPawnSquare)
      val board = EnPassantBoard(whitePawnSquare, behindBlackPawnSquare, previousMock)
      when(whitePiece.pathFor(whitePieceSquare, blackKingSquare, true)).thenReturn(Some(Set.empty[Square]))
      when(blackPiece.pathFor(blackPieceSquare, whitePieceSquare, true)).thenReturn(Some(Set.empty[Square]))
      board.result shouldBe None
    }
    "have a checkmate result when there's check that can't be escaped" in {
      when(whitePawn.pathFor(whitePawnSquare, behindBlackPawnSquare, true)).thenReturn(Some(Set.empty[Square]))
      when(previousMock.from).thenReturn(blackPawnStartSquare)
      when(previousMock.to).thenReturn(blackPawnSquare)
      val board = EnPassantBoard(whitePawnSquare, behindBlackPawnSquare, previousMock)
      when(whitePiece.pathFor(whitePieceSquare, blackKingSquare, true)).thenReturn(Some(Set.empty[Square]))
      board.result shouldBe Some(Checkmate(Black))
    }
  }
}
