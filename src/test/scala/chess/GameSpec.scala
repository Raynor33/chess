package chess

import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import Game._
import org.scalatest._
import org.scalatest.mockito.MockitoSugar

class GameSpec extends WordSpec with Matchers with OneInstancePerTest with MockitoSugar {

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
      when(previousMock.toMove) thenReturn (Black)
      nonNilGame.toMove should be(White)
    }
    "have a toMove of Black if previous game's toMove is White" in {
      when (previousMock.toMove) thenReturn (White)
      nonNilGame.toMove should be(Black)
    }

    val whiteKingSquare = Square(3,4)
    val whiteOtherSquare = Square(4,5)
    val blackOtherSquare = Square(6,7)
    val blackKingSquare = Square(5,6)
    val whiteKing = mock[Piece]
    when(whiteKing.colour).thenReturn (White)
    when(whiteKing.isKing) thenReturn (true)
    val whiteOther = mock[Piece]
    when(whiteOther.colour) thenReturn (White)
    when(whiteOther.isKing) thenReturn (false)
    val blackKing = mock[Piece]
    when(blackKing.colour) thenReturn (Black)
    when(blackKing.isKing) thenReturn (true)
    val blackOther = mock[Piece]
    when(blackOther.colour) thenReturn (Black)
    when(blackOther.isKing) thenReturn (false)
    val allPositions = Map(
      whiteKingSquare -> whiteKing,
      whiteOtherSquare -> whiteOther,
      blackOtherSquare -> blackOther,
      blackKingSquare -> blackKing
    )
    when(currentMock.currentPositions(any())).thenAnswer(new Answer[Map[Square, Piece]] {
      override def answer(invocation: InvocationOnMock): Map[Square, Piece] = {
        val predicate = invocation.getArgument[Function1[Piece, Boolean]](0)
        allPositions.filter(e => predicate(e._2))
      }
    })
    "say black is in check if a white piece is attacking it" in {
      when(whiteKing.pathFor(whiteKingSquare, blackKingSquare, true)) thenReturn (None)
      when(whiteOther.pathFor(whiteOtherSquare, blackKingSquare, true)) thenReturn (Some(Set.empty[Square]))
      nonNilGame.check(Black) should be (true)
    }
    "say black is not in check if no white piece is attacking it" in {
      when(whiteKing.pathFor(whiteKingSquare, blackKingSquare, true)) thenReturn (None)
      when(whiteOther.pathFor(whiteOtherSquare, blackKingSquare, true)) thenReturn (None)
      nonNilGame.check(Black) should be (false)
    }
    "say black is not in check if all white pieces attacking it are blocked by black" in {
      when(whiteKing.pathFor(whiteKingSquare, blackKingSquare, true)) thenReturn (None)
      when(whiteOther.pathFor(whiteOtherSquare, blackKingSquare, true)) thenReturn (Some(Set(blackOtherSquare)))
      nonNilGame.check(Black) should be (false)
    }
    "say black is not in check if all white pieces attacking it are blocked by white" in {
      when(whiteKing.pathFor(whiteKingSquare, blackKingSquare, true)) thenReturn (None)
      when(whiteOther.pathFor(whiteOtherSquare, blackKingSquare, true)) thenReturn (Some(Set(whiteOtherSquare)))
      nonNilGame.check(Black) should be (false)
    }
    "say white is in check if a black piece is attacking it" in {
      when(blackKing.pathFor(blackKingSquare, whiteKingSquare, true)) thenReturn (None)
      when(blackOther.pathFor(blackOtherSquare, whiteKingSquare, true)) thenReturn (Some(Set.empty[Square]))
      nonNilGame.check(White) should be (true)
    }
    "say white is not in check if no black piece is attacking it" in {
      when(blackKing.pathFor(blackKingSquare, whiteKingSquare, true)) thenReturn (None)
      when(blackOther.pathFor(blackOtherSquare, whiteKingSquare, true)) thenReturn (None)
      nonNilGame.check(White) should be (false)
    }
    "say white is not in check if all black pieces attacking it are blocked by white" in {
      when(blackKing.pathFor(blackKingSquare, whiteKingSquare, true)) thenReturn (None)
      when(blackOther.pathFor(blackOtherSquare, whiteKingSquare, true)) thenReturn (Some(Set(whiteOtherSquare)))
      nonNilGame.check(White) should be (false)
    }
    "say white is not in check if all black pieces attacking it are blocked by black" in {
      when(blackKing.pathFor(blackKingSquare, whiteKingSquare, true)) thenReturn (None)
      when(blackOther.pathFor(blackOtherSquare, whiteKingSquare, true)) thenReturn (Some(Set(blackOtherSquare)))
      nonNilGame.check(White) should be (false)
    }
  }
}
