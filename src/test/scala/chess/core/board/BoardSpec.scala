package chess.core.board

import chess.core.{Black, BlackBishop, BlackKing, BlackKnight, BlackPawn, BlackQueen, BlackRook, Checkmate, Pawn, Piece, Square, White, WhiteBishop, WhiteKing, WhiteKnight, WhitePawn, WhiteQueen, WhiteRook}
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalatest._
import org.scalatest.mock.MockitoSugar

class BoardSpec extends WordSpec with Matchers with OneInstancePerTest with MockitoSugar {

  trait PawnPiece extends Pawn with Piece

  "An En Passant move" should {
    val previousMock = mock[MoveBoard]
    val blackPawn = mock[PawnPiece]
    when(blackPawn.colour).thenReturn(Black)
    val whitePawn = mock[PawnPiece]
    when(whitePawn.colour).thenReturn(White)
    "update the positions correctly for a white capture" in {
      val positions = Map(
        Square(4,4) -> blackPawn,
        Square(5,4) -> whitePawn
      )
      when(previousMock.currentPositions).thenReturn(positions)
      EnPassantBoard(Square(5,4), Square(4,3), previousMock).currentPositions should be (
        Map(
          Square(4,3) -> whitePawn
        )
      )
    }
    "update the positions correctly for a black capture" in {
      val positions = Map(
        Square(4,3) -> blackPawn,
        Square(5,3) -> whitePawn
      )
      when(previousMock.currentPositions).thenReturn(positions)
      EnPassantBoard(Square(4,3), Square(5,2), previousMock).currentPositions should be (
        Map(
          Square(5,2) -> blackPawn
        )
      )
    }

    val notPawn = mock[Piece]
    when(previousMock.from).thenReturn(Square(5,1))
    when(previousMock.to).thenReturn(Square(5,3))
    when(previousMock.toMove).thenReturn(Black)
    "not be legal if it isn't that player's turn" in {
      when(previousMock.toMove).thenReturn(White)
      val positions = Map(
        Square(4,3) -> blackPawn,
        Square(5,3) -> whitePawn
      )
      when(previousMock.currentPositions).thenReturn(positions)
      EnPassantBoard(Square(4,3), Square(5,2), previousMock).moveLegal should be (false)
    }
    "not be legal if it isn't a pawn" in {
      when(notPawn.colour).thenReturn(Black)
      val positions = Map(
        Square(4,3) -> notPawn,
        Square(5,3) -> whitePawn
      )
      when(previousMock.currentPositions).thenReturn(positions)
      EnPassantBoard(Square(4,3), Square(5,2), previousMock).moveLegal should be (false)
    }
    "not be legal if the target isn't a pawn" in {
      when(notPawn.colour).thenReturn(White)
      val positions = Map(
        Square(4,3) -> blackPawn,
        Square(5,3) -> notPawn
      )
      when(previousMock.currentPositions).thenReturn(positions)
      EnPassantBoard(Square(4,3), Square(5,2), previousMock).moveLegal should be (false)
    }
    "not be legal if the target isn't an opponent pawn" in {
      val positions = Map(
        Square(4,3) -> blackPawn,
        Square(5,3) -> blackPawn
      )
      when(previousMock.currentPositions).thenReturn(positions)
      EnPassantBoard(Square(4,3), Square(5,2), previousMock).moveLegal should be (false)
    }
    "not be legal if the target piece hasn't just moved two from the first rank" in {
      when(previousMock.from).thenReturn(Square(5,2))
      val positions = Map(
        Square(4,3) -> blackPawn,
        Square(5,3) -> whitePawn
      )
      when(previousMock.currentPositions).thenReturn(positions)
      EnPassantBoard(Square(4,3), Square(5,2), previousMock).moveLegal should be (false)
    }
    "not be legal if the to square isn't correct" in {
      val positions = Map(
        Square(4,3) -> blackPawn,
        Square(5,3) -> whitePawn
      )
      when(previousMock.currentPositions).thenReturn(positions)
      when(blackPawn.pathFor(Square(4,3), Square(5,0), true)).thenReturn(None)
      EnPassantBoard(Square(4,3), Square(5,0), previousMock).moveLegal should be (false)
    }
    "be legal otherwise" in {
      val positions = Map(
        Square(4,3) -> blackPawn,
        Square(5,3) -> whitePawn
      )
      when(blackPawn.pathFor(Square(4,3), Square(5,2), true)).thenReturn(Some(Set.empty[Square]))
      when(previousMock.currentPositions).thenReturn(positions)
      EnPassantBoard(Square(4,3), Square(5,2), previousMock).moveLegal should be (true)
    }
  }

  "A Pawn Promotion move" should {
    val previousMock = mock[Board]
    val whitePawn = mock[PawnPiece]
    when(whitePawn.colour).thenReturn(White)
    val whiteOther = mock[Piece]
    when(whiteOther.colour).thenReturn(White)
    val blackPawn = mock[PawnPiece]
    when(blackPawn.colour).thenReturn(Black)
    val blackOther = mock[PawnPiece]
    when(blackOther.colour).thenReturn(Black)
    val positions = Map(
      Square(3,6) -> whitePawn,
      Square(4,7) -> blackOther,
      Square(3,1) -> blackPawn,
      Square(4,0) -> whiteOther
    )
    when(previousMock.currentPositions).thenReturn(positions)
    "update the positions correctly when white moving into space" in {
      val move = PawnPromotionBoard(Square(3,6), Square(3,7), WhiteQueen, previousMock)
      move.currentPositions should be (
        positions - Square(3,6) + (Square(3,7) -> WhiteQueen)
      )
    }
    "update the positions correctly when black moving into space" in {
      val move = PawnPromotionBoard(Square(3,1), Square(3,0), BlackQueen, previousMock)
      move.currentPositions should be (
        positions - Square(3,1) + (Square(3,0) -> BlackQueen)
      )
    }
    "update the positions correctly when white taking" in {
      val move = PawnPromotionBoard(Square(3,6), Square(4,7), WhiteQueen, previousMock)
      move.currentPositions should be (
        positions - Square(3,6) + (Square(4,7) -> WhiteQueen)
      )
    }
    "update the positions correctly when black taking" in {
      val move = PawnPromotionBoard(Square(3,1), Square(4,0), BlackQueen, previousMock)
      move.currentPositions should be (
        positions - Square(3,1) + (Square(4,0) -> BlackQueen)
      )
    }

    when(whitePawn.pathFor(any(), any(), any())).thenReturn(None)
    when(whitePawn.pathFor(Square(3,6), Square(3,7), false)).thenReturn(Some(Set.empty[Square]))
    when(whitePawn.pathFor(Square(3,6), Square(4,7), true)).thenReturn(Some(Set.empty[Square]))
    when(blackPawn.pathFor(any(), any(), any())).thenReturn(None)
    when(blackPawn.pathFor(Square(3,1), Square(3,0), false)).thenReturn(Some(Set.empty[Square]))
    when(blackPawn.pathFor(Square(3,1), Square(4,0), true)).thenReturn(Some(Set.empty[Square]))
    when(previousMock.toMove).thenReturn(White)
    "not allow promotion to a pawn" in {
      PawnPromotionBoard(Square(3,6), Square(3,7), WhitePawn, previousMock).moveLegal should be (false)
    }
    "not allow promotion to a king" in {
      PawnPromotionBoard(Square(3,6), Square(3,7), WhiteKing, previousMock).moveLegal should be (false)
    }
    "not allow promotion to a piece of the wrong colour" in {
      PawnPromotionBoard(Square(3,6), Square(3,7), BlackQueen, previousMock).moveLegal should be (false)
    }
    "not be legal for non-pawns" in {
      when(whiteOther.pathFor(Square(3,6), Square(3,7), false)).thenReturn(Some(Set.empty[Square]))
      when(previousMock.currentPositions).thenReturn(Map(
        Square(3,6) -> whiteOther
      ))
      PawnPromotionBoard(Square(3,6), Square(3,7), WhiteQueen, previousMock).moveLegal should be (false)
    }
    "not be legal if there is no piece at the from square" in {
      PawnPromotionBoard(Square(2,6), Square(2,7), WhiteQueen, previousMock).moveLegal should be (false)
    }
    "not be legal if the piece at from square is not toMove colour" in {
      when(previousMock.toMove).thenReturn(Black)
      PawnPromotionBoard(Square(3,6), Square(3,7), WhiteQueen, previousMock).moveLegal should be (false)
    }
    "not be legal if the destination is blocked by a piece of the same colour" in {
      when(previousMock.currentPositions).thenReturn(positions + (Square(4,7) -> whiteOther))
      PawnPromotionBoard(Square(3,6), Square(4,7), WhiteQueen, previousMock).moveLegal should be (false)
    }
    "not be legal if there is no path" in {
      PawnPromotionBoard(Square(3,6), Square(5,7), WhiteQueen, previousMock).moveLegal should be (false)
    }
    "be legal if there is a clear path and the destination is empty" in {
      PawnPromotionBoard(Square(3,6), Square(3,7), WhiteQueen, previousMock).moveLegal should be (true)
    }
    "be legal if there is a clear path and the destination has an opponent" in {
      PawnPromotionBoard(Square(3,6), Square(4,7), WhiteQueen, previousMock).moveLegal should be (true)
    }
  }
}
