package chess.core.board

import chess.core.{Black, BlackQueen, Checkmate, Pawn, Piece, Square, White, WhiteKing, WhitePawn, WhiteQueen}
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito.{reset, when}
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, Matchers, WordSpec}

class PawnPromotionBoardSpec extends WordSpec with Matchers with MockitoSugar with BeforeAndAfter {

  val previousMock = mock[Board]
  val whitePawn = mock[Pawn]
  val whitePiece = mock[Piece]
  val whiteKing = mock[Piece]
  val blackPiece = mock[Piece]
  val blackKing = mock[Piece]
  val whitePieceSquare = Square(1, 4)
  val whitePawnSquare = Square(6, 6)
  val whiteKingSquare = Square(3, 2)
  val backRowBlackPieceSquare = Square(5, 7)
  val blackKingSquare = Square(4, 6)
  val backRowSquare = Square(6, 7)
  val emptySquare = Square(7, 7)
  val previousPositions = Map(
    whitePieceSquare -> whitePiece,
    whitePawnSquare -> whitePawn,
    whiteKingSquare -> whiteKing,
    backRowBlackPieceSquare -> blackPiece,
    blackKingSquare -> blackKing
  )

  before {
    reset(previousMock)
    when(whitePiece.colour).thenReturn(White)
    when(whitePawn.colour).thenReturn(White)
    when(whiteKing.colour).thenReturn(White)
    when(whiteKing.isKing).thenReturn(true)
    when(blackPiece.colour).thenReturn(Black)
    when(blackKing.colour).thenReturn(Black)
    when(blackKing.isKing).thenReturn(true)

    when(whiteKing.pathFor(any(), any(), any())).thenReturn(None)
    when(whitePawn.pathFor(any(), any(), any())).thenReturn(None)
    when(whitePiece.pathFor(any(), any(), any())).thenReturn(None)
    when(blackKing.pathFor(any(), any(), any())).thenReturn(None)
    when(blackPiece.pathFor(any(), any(), any())).thenReturn(None)

    when(previousMock.currentPositions).thenReturn(previousPositions)
    when(previousMock.toMove).thenReturn(White)
    when(previousMock.valid).thenReturn(true)
    when(previousMock.result).thenReturn(None)
  }

  "A PawnPromotionBoard" should {
    "say hasNeverMoved is false if previous hasNeverMoved is false" in {
      val board = PawnPromotionBoard(whitePawnSquare, backRowSquare, WhiteQueen, previousMock)
      when(previousMock.hasNeverMoved(whitePieceSquare)).thenReturn(false)
      board.hasNeverMoved(whitePieceSquare) shouldBe false
    }
    "say hasNeverMoved is false if it equals from" in {
      val board = PawnPromotionBoard(whitePawnSquare, backRowSquare, WhiteQueen, previousMock)
      when(previousMock.hasNeverMoved(whitePawnSquare)).thenReturn(true)
      board.hasNeverMoved(whitePawnSquare) shouldBe false
    }
    "say hasNeverMoved is true if it doesn't equal from and previous value is true" in {
      val board = PawnPromotionBoard(whitePawnSquare, backRowSquare, WhiteQueen, previousMock)
      when(previousMock.hasNeverMoved(whitePieceSquare)).thenReturn(true)
      board.hasNeverMoved(whitePieceSquare) shouldBe true
    }
    "say toMove is White if previous game's toMove is Black" in {
      when(previousMock.toMove).thenReturn(Black)
      PawnPromotionBoard(backRowBlackPieceSquare, backRowSquare, BlackQueen, previousMock).toMove shouldBe White
    }
    "say toMove is Black if previous game's toMove is White" in {
      when(previousMock.toMove).thenReturn(White)
      PawnPromotionBoard(whitePawnSquare, backRowSquare, WhiteQueen, previousMock).toMove shouldBe Black
    }
    "update the currentPositions correctly for a move into space" in {
      val board = PawnPromotionBoard(whitePawnSquare, backRowSquare, WhiteQueen, previousMock)
      board.currentPositions shouldBe Map(
        whitePieceSquare -> whitePiece,
        backRowSquare -> WhiteQueen,
        whiteKingSquare -> whiteKing,
        backRowBlackPieceSquare -> blackPiece,
        blackKingSquare -> blackKing
      )
    }
    "update the currentPositions correctly for a move when taking" in {
      val board = PawnPromotionBoard(whitePawnSquare, backRowBlackPieceSquare, WhiteQueen, previousMock)
      board.currentPositions shouldBe Map(
        whitePieceSquare -> whitePiece,
        whiteKingSquare -> whiteKing,
        backRowBlackPieceSquare -> WhiteQueen,
        blackKingSquare -> blackKing
      )
    }
    "not be valid if there is no piece at the from square" in {
      when(whitePawn.pathFor(whitePawnSquare, backRowSquare, false)).thenReturn(Some(Set.empty[Square]))
      when(previousMock.currentPositions).thenReturn(previousPositions - whitePawnSquare)
      val board = PawnPromotionBoard(whitePawnSquare, backRowSquare, WhiteQueen, previousMock)
      board.valid shouldBe false
    }
    "not be valid if the piece at from square is not toMove colour" in {
      when(blackPiece.pathFor(whitePawnSquare, backRowSquare, false)).thenReturn(Some(Set.empty[Square]))
      when(previousMock.currentPositions).thenReturn(previousPositions + (whitePawnSquare -> blackPiece))
      val board = PawnPromotionBoard(whitePawnSquare, backRowSquare, WhiteQueen, previousMock)
      board.valid shouldBe false
    }
    "not be valid if the destination is blocked by a piece of the same colour" in {
      when(previousMock.currentPositions).thenReturn(previousPositions + (backRowSquare -> whitePiece))
      when(whitePawn.pathFor(whitePawnSquare, backRowSquare, false)).thenReturn(Some(Set.empty[Square]))
      val board = PawnPromotionBoard(whitePawnSquare, backRowSquare, WhiteQueen, previousMock)
      board.valid shouldBe false
    }
    "not be valid if not a valid pawn move" in {
      when(whitePawn.pathFor(whitePawnSquare, backRowSquare, false)).thenReturn(None)
      val board = PawnPromotionBoard(whitePieceSquare, backRowSquare, WhiteQueen, previousMock)
      board.valid shouldBe false
    }
    "not be valid if not a pawn" in {
      when(previousMock.currentPositions).thenReturn(previousPositions + (whitePawnSquare -> whitePiece))
      when(whitePiece.pathFor(whitePawnSquare, backRowSquare, false)).thenReturn(Some(Set.empty[Square]))
      val board = PawnPromotionBoard(whitePieceSquare, backRowSquare, WhiteQueen, previousMock)
      board.valid shouldBe false
    }
    "not be valid if it ends in check" in {
      when(blackPiece.pathFor(backRowBlackPieceSquare, whiteKingSquare, true)).thenReturn(Some(Set.empty[Square]))
      when(whitePawn.pathFor(whitePawnSquare, backRowSquare, false)).thenReturn(Some(Set.empty[Square]))
      val board = PawnPromotionBoard(whitePieceSquare, backRowSquare, WhiteQueen, previousMock)
      board.valid shouldBe false
    }
    "not be valid when promoting to a pawn" in {
      when(whitePawn.pathFor(whitePawnSquare, backRowSquare, false)).thenReturn(Some(Set.empty[Square]))
      val board = PawnPromotionBoard(whitePawnSquare, backRowSquare, WhitePawn, previousMock)
      board.valid shouldBe false
    }
    "not be valid when promoting to a king" in {
      when(whitePawn.pathFor(whitePawnSquare, backRowSquare, false)).thenReturn(Some(Set.empty[Square]))
      val board = PawnPromotionBoard(whitePawnSquare, backRowSquare, WhiteKing, previousMock)
      board.valid shouldBe false
    }
    "not be valid when promoting to the wrong colour" in {
      when(whitePawn.pathFor(whitePawnSquare, backRowSquare, false)).thenReturn(Some(Set.empty[Square]))
      val board = PawnPromotionBoard(whitePawnSquare, backRowSquare, BlackQueen, previousMock)
      board.valid shouldBe false
    }
    "not be valid if the previous isn't valid" in {
      when(whitePawn.pathFor(whitePawnSquare, backRowSquare, false)).thenReturn(Some(Set.empty[Square]))
      val board = PawnPromotionBoard(whitePawnSquare, backRowSquare, WhiteQueen, previousMock)
      when(previousMock.valid).thenReturn(false)
      board.valid shouldBe false
    }
    "not be valid if the previous is complete" in {
      when(whitePawn.pathFor(whitePawnSquare, backRowSquare, false)).thenReturn(Some(Set.empty[Square]))
      val board = PawnPromotionBoard(whitePawnSquare, backRowSquare, WhiteQueen, previousMock)
      when(previousMock.result).thenReturn(Some(Checkmate(White)))
      board.valid shouldBe false
    }
    "be valid when not taking and all ok" in {
      when(whitePawn.pathFor(whitePawnSquare, backRowSquare, false)).thenReturn(Some(Set.empty[Square]))
      val board = PawnPromotionBoard(whitePawnSquare, backRowSquare, WhiteQueen, previousMock)
      board.valid shouldBe true
    }
    "be valid when taking and all ok" in {
      when(whitePawn.pathFor(whitePawnSquare, backRowBlackPieceSquare, true)).thenReturn(Some(Set.empty[Square]))
      val board = PawnPromotionBoard(whitePawnSquare, backRowBlackPieceSquare, WhiteQueen, previousMock)
      board.valid shouldBe true
    }
    "not have a result when there's no check" in {
      when(whitePawn.pathFor(whitePawnSquare, backRowBlackPieceSquare, true)).thenReturn(Some(Set.empty[Square]))
      val board = PawnPromotionBoard(whitePawnSquare, backRowBlackPieceSquare, whitePiece, previousMock)
      board.result shouldBe None
    }
    "not have a result when there's check that can be escaped by moving" in {
      when(whitePawn.pathFor(whitePawnSquare, backRowBlackPieceSquare, true)).thenReturn(Some(Set.empty[Square]))
      val board = PawnPromotionBoard(whitePawnSquare, backRowBlackPieceSquare, whitePiece, previousMock)
      when(whitePiece.pathFor(backRowBlackPieceSquare, blackKingSquare, true)).thenReturn(Some(Set.empty[Square]))
      when(blackKing.pathFor(blackKingSquare, emptySquare, false)).thenReturn(Some(Set.empty[Square]))
      board.result shouldBe None
    }
    "not have a result when there's check that can be escaped by blocking" in {
      when(whitePawn.pathFor(whitePawnSquare, backRowSquare, false)).thenReturn(Some(Set.empty[Square]))
      val board = PawnPromotionBoard(whitePawnSquare, backRowSquare, whitePiece, previousMock)
      when(whitePiece.pathFor(backRowSquare, blackKingSquare, true)).thenReturn(Some(Set(emptySquare)))
      when(blackPiece.pathFor(backRowBlackPieceSquare, emptySquare, false)).thenReturn(Some(Set.empty[Square]))
      board.result shouldBe None
    }
    "not have a result when there's check that can be escaped by taking" in {
      when(whitePawn.pathFor(whitePawnSquare, backRowSquare, false)).thenReturn(Some(Set.empty[Square]))
      val board = PawnPromotionBoard(whitePawnSquare, backRowSquare, whitePiece, previousMock)
      when(whitePiece.pathFor(backRowSquare, blackKingSquare, true)).thenReturn(Some(Set(emptySquare)))
      when(blackPiece.pathFor(backRowBlackPieceSquare, backRowSquare, true)).thenReturn(Some(Set.empty[Square]))
      board.result shouldBe None
    }
    "have a checkmate result when there's check that can't be escaped" in {
      when(whitePawn.pathFor(whitePawnSquare, backRowSquare, false)).thenReturn(Some(Set.empty[Square]))
      val board = PawnPromotionBoard(whitePawnSquare, backRowSquare, whitePiece, previousMock)
      when(whitePiece.pathFor(backRowSquare, blackKingSquare, true)).thenReturn(Some(Set(emptySquare)))
      board.result shouldBe Some(Checkmate(Black))
    }
  }
}
