package chess.core

import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import org.scalatest._
import org.scalatest.mockito.MockitoSugar

class BoardSpec extends WordSpec with Matchers with OneInstancePerTest with MockitoSugar {

  "The Nil Board" should {
    "have the correct positions for everything" in {
      val pieceMap = NilBoard.currentPositions
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
    "say hasNeverMoved is true" in {
      Square.allSquares.foreach(
        NilBoard.hasNeverMoved(_) should be (true)
      )
    }
    "say that it's white's move" in {
      NilBoard.toMove should be (White)
    }
    "not be in checkmate" in {
      NilBoard.checkmate should be (false)
    }
    "not be in check for white" in {
      NilBoard.check(White) should be (false)
    }
    "not be in check for black" in {
      NilBoard.check(White) should be (false)
    }
    "be valid" in {
      NilBoard.valid should be (true)
    }
  }

  "A Move" should {
    val previousMock = mock[Board]
    val currentMock = mock[MoveBoard]
    val move = new MoveBoard {
      val previousBoard = previousMock
      val from = mock[Square]
      val to = mock[Square]
      def currentPositions = currentMock.currentPositions
      def moveLegal = currentMock.moveLegal
    }

    "say hasNeverMoved is false if previous hasNeverMoved is false" in {
      val square = Square(0,0)
      when(previousMock.hasNeverMoved(square)).thenReturn(false)
      move.hasNeverMoved(square) should be (false)
    }
    "say hasNeverMoved is false if it equals from" in {
      when(previousMock.hasNeverMoved(move.from)).thenReturn(true)
      move.hasNeverMoved(move.from) should be (false)
    }
    "say hasNeverMoved is true if it doesn't equal from and previous value is true" in {
      val square = Square(0,0)
      when(previousMock.hasNeverMoved(square)).thenReturn(true)
      move.hasNeverMoved(square) should be (true)
    }

    "have a toMove of White if previous game's toMove is Black" in {
      when(previousMock.toMove) thenReturn (Black)
      move.toMove should be(White)
    }
    "have a toMove of Black if previous game's toMove is White" in {
      when (previousMock.toMove) thenReturn (White)
      move.toMove should be(Black)
    }

    val whiteKingSquare = Square(3,4)
    val whiteOtherSquare = Square(4,5)
    val blackOtherSquare = Square(6,7)
    val blackKingSquare = Square(5,6)
    val escapeSquare = Square(7,7)
    val blockSquare = Square(4,4)
    val whiteKing = mock[Piece]
    when(whiteKing.colour).thenReturn (White)
    when(whiteKing.isKing) thenReturn (true)
    when(whiteKing.pathFor(any(), any(), any())).thenReturn(None)
    val whiteOther = mock[Piece]
    when(whiteOther.colour) thenReturn (White)
    when(whiteOther.isKing) thenReturn (false)
    when(whiteOther.pathFor(any(), any(), any())).thenReturn(None)
    val blackKing = mock[Piece]
    when(blackKing.colour) thenReturn (Black)
    when(blackKing.isKing) thenReturn (true)
    when(blackKing.pathFor(any(), any(), any())).thenReturn(None)
    val blackOther = mock[Piece]
    when(blackOther.colour) thenReturn (Black)
    when(blackOther.isKing) thenReturn (false)
    when(blackOther.pathFor(any(), any(), any())).thenReturn(None)
    val allPositions = Map(
      whiteKingSquare -> whiteKing,
      whiteOtherSquare -> whiteOther,
      blackOtherSquare -> blackOther,
      blackKingSquare -> blackKing
    )
    when(currentMock.currentPositions).thenReturn(allPositions)
    "say black is not in check if no white piece is attacking it" in {
      move.check(Black) should be (false)
    }
    "say black is in check if a white piece is attacking it" in {
      when(whiteOther.pathFor(whiteOtherSquare, blackKingSquare, true)).thenReturn(Some(Set.empty[Square]))
      move.check(Black) should be (true)
    }
    "say black is not in check if white's attacks are blocked by white" in {
      when(whiteOther.pathFor(whiteOtherSquare, blackKingSquare, true)).thenReturn(Some(Set(whiteKingSquare)))
      move.check(Black) should be (false)
    }
    "say black is not in check if white's attacks are blocked by black" in {
      when(whiteOther.pathFor(whiteOtherSquare, blackKingSquare, true)).thenReturn(Some(Set(blackOtherSquare)))
      move.check(Black) should be (false)
    }
    "say white is not in check if no black piece is attacking it" in {
      move.check(White) should be (false)
    }
    "say white is in check if a black piece is attacking it" in {
      when(blackOther.pathFor(blackOtherSquare, whiteKingSquare, true)).thenReturn(Some(Set.empty[Square]))
      move.check(White) should be (true)
    }
    "say white is not in check if black's attacks are blocked by white" in {
      when(blackOther.pathFor(whiteOtherSquare, blackKingSquare, true)).thenReturn(Some(Set(whiteOtherSquare)))
      move.check(White) should be (false)
    }
    "say white is not in check if black's attacks are blocked by black" in {
      when(blackOther.pathFor(whiteOtherSquare, blackKingSquare, true)).thenReturn(Some(Set(blackKingSquare)))
      move.check(White) should be (false)
    }

    "not be checkmate if it's not check" in {
      when(previousMock.toMove) thenReturn (Black)
      move.checkmate should be (false)
    }
    "not be checkmate if an evasion escape is possible" in {
      when(previousMock.toMove) thenReturn (Black)
      when(whiteOther.pathFor(whiteOtherSquare, blackKingSquare, true)).thenReturn(Some(Set.empty[Square]))
      when(blackKing.pathFor(blackKingSquare, escapeSquare, false)).thenReturn(Some(Set.empty[Square]))
    }
    "not be checkmate if a blocking escape is possible" in {
      when(previousMock.toMove) thenReturn (Black)
      when(whiteOther.pathFor(whiteOtherSquare, blackKingSquare, true)).thenReturn(Some(Set(blockSquare)))
      when(blackOther.pathFor(blackOtherSquare, blockSquare, false)).thenReturn(Some(Set.empty[Square]))
    }
    "not be checkmate if a taking escape is possible" in {
      when(previousMock.toMove) thenReturn (Black)
      when(whiteOther.pathFor(whiteOtherSquare, blackKingSquare, true)).thenReturn(Some(Set.empty[Square]))
      when(blackOther.pathFor(blackOtherSquare, whiteOtherSquare, true)).thenReturn(Some(Set.empty[Square]))
    }
    "be checkmate if no escape is possible" in {
      when(previousMock.toMove) thenReturn (Black)
      when(whiteOther.pathFor(whiteOtherSquare, blackKingSquare, true)).thenReturn(Some(Set.empty[Square]))
    }


    "should not be valid if the previous isn't" in {
      when(previousMock.valid).thenReturn(false)
      when(currentMock.moveLegal).thenReturn(true)
      when(previousMock.toMove) thenReturn(White)
      move.valid should be (false)
    }
    "should not be valid if the move isn't legal" in {
      when(previousMock.valid).thenReturn(true)
      when(currentMock.moveLegal).thenReturn(false)
      when(previousMock.toMove) thenReturn(White)
      move.valid should be (false)
    }
    "should not be valid if the player ends in check" in {
      when(previousMock.valid).thenReturn(true)
      when(currentMock.moveLegal).thenReturn(true)
      when(previousMock.toMove) thenReturn(White)
      when(blackOther.pathFor(blackOtherSquare, whiteKingSquare, true)).thenReturn(Some(Set.empty[Square]))
      move.valid should be (false)
    }
    "should be valid otherwise" in {
      when(previousMock.valid).thenReturn(true)
      when(currentMock.moveLegal).thenReturn(true)
      when(previousMock.toMove) thenReturn(White)
      move.valid should be (true)
    }
  }

  "A StandardMove" should {
    val previousMock = mock[Board]
    val whitePiece1 = mock[Piece]
    when(whitePiece1.colour).thenReturn(White)
    when(whitePiece1.pathFor(any(), any(), any())).thenReturn(None)
    val whitePiece2 = mock[Piece]
    when(whitePiece2.colour).thenReturn(White)
    when(whitePiece2.isKing).thenReturn(true)
    when(whitePiece2.pathFor(any(), any(), any())).thenReturn(None)
    val blackPiece1 = mock[Piece]
    when(blackPiece1.colour).thenReturn(Black)
    when(blackPiece1.pathFor(any(), any(), any())).thenReturn(None)
    val blackPiece2 = mock[Piece]
    when(blackPiece2.colour).thenReturn(Black)
    when(blackPiece2.isKing).thenReturn(true)
    when(blackPiece2.pathFor(any(), any(), any())).thenReturn(None)
    val whitePiece1Square: Square = Square(1, 4)
    val whitePiece2Square: Square = Square(3, 2)
    val blackPiece1Square: Square = Square(5, 5)
    val blackPiece2Square: Square = Square(4, 6)
    val previousPositions = Map(
      whitePiece1Square -> whitePiece1,
      whitePiece2Square -> whitePiece2,
      blackPiece1Square -> blackPiece1,
      blackPiece2Square -> blackPiece2
    )
    when(previousMock.currentPositions).thenReturn(previousPositions)
    when(previousMock.toMove).thenReturn(White)

    "update the currentPositions correctly for a move into space" in {
      val move = StandardMoveBoard(whitePiece1Square, Square(2, 4), previousMock)
      move.currentPositions should be (
        Map(
          Square(2, 4) -> whitePiece1,
          whitePiece2Square -> whitePiece2,
          blackPiece1Square -> blackPiece1,
          blackPiece2Square -> blackPiece2
        )
      )
    }
    "update the currentPositions correctly for a move when taking" in {
      val move = StandardMoveBoard(whitePiece2Square, blackPiece1Square, previousMock)
      move.currentPositions should be (
        Map(
          whitePiece1Square -> whitePiece1,
          blackPiece1Square -> whitePiece2,
          blackPiece2Square -> blackPiece2
        )
      )
    }

    "not be legal if there is no piece at the from square" in {
      val move = StandardMoveBoard(Square(0, 0), Square(1, 1), previousMock)
      move.moveLegal should be (false)
    }
    "not be legal if the piece at from square is not toMove colour" in {
      when(blackPiece1.pathFor(blackPiece1Square, Square(5, 4), false)).thenReturn(Some(Set.empty[Square]))
      val move = StandardMoveBoard(blackPiece1Square, Square(5, 4), previousMock)
      move.moveLegal should be (false)
    }
    "not be legal if the path is blocked by a piece of the same colour" in {
      when(whitePiece1.pathFor(whitePiece1Square, Square(2, 4), false)).thenReturn(Some(Set(whitePiece2Square)));
      val move = StandardMoveBoard(whitePiece1Square, Square(2, 4), previousMock)
      move.moveLegal should be (false)
    }
    "not be legal if the destination is blocked by a piece of the same colour" in {
      when(whitePiece1.pathFor(whitePiece1Square, whitePiece2Square, true)).thenReturn(Some(Set.empty[Square]))
      val move = StandardMoveBoard(whitePiece1Square, whitePiece2Square, previousMock)
      move.moveLegal should be (false)
    }
    "not be legal if the path is blocked by a piece of the opposite colour" in {
      when(whitePiece1.pathFor(whitePiece1Square, Square(2, 4), false)).thenReturn(Some(Set(blackPiece1Square)));
      val move = StandardMoveBoard(whitePiece1Square, Square(2, 4), previousMock)
      move.moveLegal should be (false)
    }
    "not be legal if there is no path" in {
      when(whitePiece1.pathFor(whitePiece1Square, Square(2, 4), false)).thenReturn(None);
      val move = StandardMoveBoard(whitePiece1Square, Square(2, 4), previousMock)
      move.moveLegal should be (false)
    }
    "be legal if there is a clear path and the destination is empty" in {
      when(whitePiece1.pathFor(whitePiece1Square, Square(2, 4), false)).thenReturn(Some(Set.empty[Square]))
      val move = StandardMoveBoard(whitePiece1Square, Square(2, 4), previousMock)
      move.moveLegal should be (true)
    }
    "be legal if there is a clear path and the destination has an opponent" in {
      when(whitePiece1.pathFor(whitePiece1Square, blackPiece1Square, true)).thenReturn(Some(Set.empty[Square]))
      val move = StandardMoveBoard(whitePiece1Square, blackPiece1Square, previousMock)
      move.moveLegal should be (true)
    }
  }

  "A CastlingMove" should {
    val previousMock = mock[Board]
    val whiteRook1 = mock[Piece]
    when(whiteRook1.colour).thenReturn(White)
    when(whiteRook1.pathFor(any(), any(), any())).thenReturn(None)
    val whiteRook2 = mock[Piece]
    when(whiteRook2.colour).thenReturn(White)
    when(whiteRook2.pathFor(any(), any(), any())).thenReturn(None)
    val whiteKing = mock[Piece]
    when(whiteKing.colour).thenReturn(White)
    when(whiteKing.isKing).thenReturn(true)
    when(whiteKing.pathFor(any(), any(), any())).thenReturn(None)
    val blackRook1 = mock[Piece]
    when(blackRook1.colour).thenReturn(Black)
    when(blackRook1.pathFor(any(), any(), any())).thenReturn(None)
    val blackRook2 = mock[Piece]
    when(blackRook2.colour).thenReturn(Black)
    when(blackRook2.pathFor(any(), any(), any())).thenReturn(None)
    val blackKing = mock[Piece]
    when(blackKing.colour).thenReturn(Black)
    when(blackKing.isKing).thenReturn(true)
    when(blackKing.pathFor(any(), any(), any())).thenReturn(None)

    val previousPositions = Map(
      Square(0, 0) -> whiteRook1,
      Square(4, 0) -> whiteKing,
      Square(7, 0) -> whiteRook2,
      Square(0, 7) -> blackRook1,
      Square(4, 7) -> blackKing,
      Square(7, 7) -> blackRook2
    )
    previousPositions.keys.foreach(s =>
      when(previousMock.hasNeverMoved(s)).thenReturn(true)
    )
    when(previousMock.currentPositions).thenReturn(previousPositions)
    when(previousMock.toMove).thenReturn(White)
    "update the current positions correctly for the white king castling left" in {
      CastlingMoveBoard(Square(4, 0), Square(2, 0), previousMock).currentPositions should be (
        Map(
          Square(3, 0) -> whiteRook1,
          Square(2, 0) -> whiteKing,
          Square(7, 0) -> whiteRook2,
          Square(0, 7) -> blackRook1,
          Square(4, 7) -> blackKing,
          Square(7, 7) -> blackRook2
        )
      )
    }
    "update the current positions correctly for the white king castling right" in {
      CastlingMoveBoard(Square(4, 0), Square(6, 0), previousMock).currentPositions should be (
        Map(
          Square(0, 0) -> whiteRook1,
          Square(6, 0) -> whiteKing,
          Square(5, 0) -> whiteRook2,
          Square(0, 7) -> blackRook1,
          Square(4, 7) -> blackKing,
          Square(7, 7) -> blackRook2
        )
      )
    }
    "update the current positions correctly for the black king castling left" in {
      CastlingMoveBoard(Square(4, 7), Square(2, 7), previousMock).currentPositions should be (
        Map(
          Square(0, 0) -> whiteRook1,
          Square(4, 0) -> whiteKing,
          Square(7, 0) -> whiteRook2,
          Square(3, 7) -> blackRook1,
          Square(2, 7) -> blackKing,
          Square(7, 7) -> blackRook2
        )
      )
    }
    "update the current positions correctly for the black king castling right" in {
      CastlingMoveBoard(Square(4, 7), Square(6, 7), previousMock).currentPositions should be (
        Map(
          Square(0, 0) -> whiteRook1,
          Square(4, 0) -> whiteKing,
          Square(7, 0) -> whiteRook2,
          Square(0, 7) -> blackRook1,
          Square(6, 7) -> blackKing,
          Square(5, 7) -> blackRook2
        )
      )
    }
    "not be valid if from non king squares" in {
      CastlingMoveBoard(Square(3, 0), Square(5, 0), previousMock).moveLegal should be (false)
    }
    "not be valid if to squares not 2 horizontally from the king" in {
      CastlingMoveBoard(Square(4, 0), Square(5, 0), previousMock).moveLegal should be (false)
    }
    "not be valid if the king has moved" in {
      when(previousMock.hasNeverMoved(Square(4, 0))).thenReturn(false)
      CastlingMoveBoard(Square(4, 0), Square(6, 0), previousMock).moveLegal should be (false)
    }
    "not be valid if the castle has moved" in {
      when(previousMock.hasNeverMoved(Square(7, 0))).thenReturn(false)
      CastlingMoveBoard(Square(4, 0), Square(6, 0), previousMock).moveLegal should be (false)
    }
    "not be valid if it's not that player's turn" in {
      when(previousMock.toMove).thenReturn(Black)
      CastlingMoveBoard(Square(4, 0), Square(6, 0), previousMock).moveLegal should be (false)
    }
    "not be valid if the king is in check" in {
      when(previousMock.check(White)).thenReturn(true)
      CastlingMoveBoard(Square(4, 0), Square(6, 0), previousMock).moveLegal should be (false)
    }
    "not be valid if the square the king moves through is attackable" in {
      when(blackRook1.pathFor(Square(0, 7), Square(5, 0), true)).thenReturn(Some(Set.empty[Square]))
      CastlingMoveBoard(Square(4, 0), Square(6, 0), previousMock).moveLegal should be (false)
    }
    "not be valid if any square is blocked" in {
      for (x <- 5 to 6) {
        when(previousMock.currentPositions).thenReturn(previousPositions + (Square(x, 0) -> mock[Piece]))
        CastlingMoveBoard(Square(4, 0), Square(6, 0), previousMock).moveLegal should be (false)
      }
    }
    "be valid when everything's ok" in {
      CastlingMoveBoard(Square(4, 0), Square(6, 0), previousMock).moveLegal should be (true)
    }
  }

  "An En Passant move" should {
    val previousMock = mock[MoveBoard]
    val blackPawn = mock[Piece with Pawn]
    when(blackPawn.colour).thenReturn(Black)
    val whitePawn = mock[Piece with Pawn]
    when(whitePawn.colour).thenReturn(White)
    "update the positions correctly for a white capture" in {
      val positions = Map(
        Square(4,4) -> blackPawn,
        Square(5,4) -> whitePawn
      )
      when(previousMock.currentPositions).thenReturn(positions)
      EnPassantMoveBoard(Square(5,4), Square(4,3), previousMock).currentPositions should be (
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
      EnPassantMoveBoard(Square(4,3), Square(5,2), previousMock).currentPositions should be (
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
      EnPassantMoveBoard(Square(4,3), Square(5,2), previousMock).moveLegal should be (false)
    }
    "not be legal if it isn't a pawn" in {
      when(notPawn.colour).thenReturn(Black)
      val positions = Map(
        Square(4,3) -> notPawn,
        Square(5,3) -> whitePawn
      )
      when(previousMock.currentPositions).thenReturn(positions)
      EnPassantMoveBoard(Square(4,3), Square(5,2), previousMock).moveLegal should be (false)
    }
    "not be legal if the target isn't a pawn" in {
      when(notPawn.colour).thenReturn(White)
      val positions = Map(
        Square(4,3) -> blackPawn,
        Square(5,3) -> notPawn
      )
      when(previousMock.currentPositions).thenReturn(positions)
      EnPassantMoveBoard(Square(4,3), Square(5,2), previousMock).moveLegal should be (false)
    }
    "not be legal if the target isn't an opponent pawn" in {
      val positions = Map(
        Square(4,3) -> blackPawn,
        Square(5,3) -> blackPawn
      )
      when(previousMock.currentPositions).thenReturn(positions)
      EnPassantMoveBoard(Square(4,3), Square(5,2), previousMock).moveLegal should be (false)
    }
    "not be legal if the target piece hasn't just moved two from the first rank" in {
      when(previousMock.from).thenReturn(Square(5,2))
      val positions = Map(
        Square(4,3) -> blackPawn,
        Square(5,3) -> whitePawn
      )
      when(previousMock.currentPositions).thenReturn(positions)
      EnPassantMoveBoard(Square(4,3), Square(5,2), previousMock).moveLegal should be (false)
    }
    "be legal otherwise" in {
      val positions = Map(
        Square(4,3) -> blackPawn,
        Square(5,3) -> whitePawn
      )
      when(previousMock.currentPositions).thenReturn(positions)
      EnPassantMoveBoard(Square(4,3), Square(5,2), previousMock).moveLegal should be (true)
    }
  }

  "A Pawn Promotion move" should {
    val previousMock = mock[Board]
    val whitePawn = mock[Piece with Pawn]
    when(whitePawn.colour).thenReturn(White)
    val whiteOther = mock[Piece]
    when(whiteOther.colour).thenReturn(White)
    val blackPawn = mock[Piece with Pawn]
    when(blackPawn.colour).thenReturn(Black)
    val blackOther = mock[Piece with Pawn]
    when(blackOther.colour).thenReturn(Black)
    val positions = Map(
      Square(3,6) -> whitePawn,
      Square(4,7) -> blackOther,
      Square(3,1) -> blackPawn,
      Square(4,0) -> whiteOther
    )
    when(previousMock.currentPositions).thenReturn(positions)
    "update the positions correctly when white moving into space" in {
      val move = PawnPromotionMoveBoard(Square(3,6), Square(3,7), WhiteQueen, previousMock)
      move.currentPositions should be (
        positions - Square(3,6) + (Square(3,7) -> WhiteQueen)
      )
    }
    "update the positions correctly when black moving into space" in {
      val move = PawnPromotionMoveBoard(Square(3,1), Square(3,0), BlackQueen, previousMock)
      move.currentPositions should be (
        positions - Square(3,1) + (Square(3,0) -> BlackQueen)
      )
    }
    "update the positions correctly when white taking" in {
      val move = PawnPromotionMoveBoard(Square(3,6), Square(4,7), WhiteQueen, previousMock)
      move.currentPositions should be (
        positions - Square(3,6) + (Square(4,7) -> WhiteQueen)
      )
    }
    "update the positions correctly when black taking" in {
      val move = PawnPromotionMoveBoard(Square(3,1), Square(4,0), BlackQueen, previousMock)
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
      PawnPromotionMoveBoard(Square(3,6), Square(3,7), WhitePawn, previousMock).moveLegal should be (false)
    }
    "not allow promotion to a king" in {
      PawnPromotionMoveBoard(Square(3,6), Square(3,7), WhiteKing, previousMock).moveLegal should be (false)
    }
    "not allow promotion to a piece of the wrong colour" in {
      PawnPromotionMoveBoard(Square(3,6), Square(3,7), BlackQueen, previousMock).moveLegal should be (false)
    }
    "not be legal for non-pawns" in {
      when(whiteOther.pathFor(Square(3,6), Square(3,7), false)).thenReturn(Some(Set.empty[Square]))
      when(previousMock.currentPositions).thenReturn(Map(
        Square(3,6) -> whiteOther
      ))
      PawnPromotionMoveBoard(Square(3,6), Square(3,7), WhiteQueen, previousMock).moveLegal should be (false)
    }
    "not be legal if there is no piece at the from square" in {
      PawnPromotionMoveBoard(Square(2,6), Square(2,7), WhiteQueen, previousMock).moveLegal should be (false)
    }
    "not be legal if the piece at from square is not toMove colour" in {
      when(previousMock.toMove).thenReturn(Black)
      PawnPromotionMoveBoard(Square(3,6), Square(3,7), WhiteQueen, previousMock).moveLegal should be (false)
    }
    "not be legal if the destination is blocked by a piece of the same colour" in {
      when(previousMock.currentPositions).thenReturn(positions + (Square(4,7) -> whiteOther))
      PawnPromotionMoveBoard(Square(3,6), Square(4,7), WhiteQueen, previousMock).moveLegal should be (false)
    }
    "not be legal if there is no path" in {
      PawnPromotionMoveBoard(Square(3,6), Square(5,7), WhiteQueen, previousMock).moveLegal should be (false)
    }
    "be legal if there is a clear path and the destination is empty" in {
      PawnPromotionMoveBoard(Square(3,6), Square(3,7), WhiteQueen, previousMock).moveLegal should be (true)
    }
    "be legal if there is a clear path and the destination has an opponent" in {
      PawnPromotionMoveBoard(Square(3,6), Square(4,7), WhiteQueen, previousMock).moveLegal should be (true)
    }
  }
}
