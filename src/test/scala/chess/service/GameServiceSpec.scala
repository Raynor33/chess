package chess.service

import chess.domain.{Board, NilBoard, Square, White, WhiteQueen}
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, OneInstancePerTest, WordSpec}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import org.mockito.ArgumentMatchers.{eq => eql, _}

class GameServiceSpec extends WordSpec with Matchers with OneInstancePerTest with MockitoSugar with ScalaFutures {

  private val playerId = "somePlayer"
  private val player2Id = "someOtherPlayer"
  private val gameId = "someGame"
  private val mockBoard = mock[Board]
  private val mockStandardBoard = mock[Board]
  private val mockCastlingBoard = mock[Board]
  private val mockEnPassantBoard = mock[Board]
  private val mockPawnPromotionBoard = mock[Board]
  private val mockGameStore = mock[GameStore]
  private val game = Game(playerId, player2Id, mockBoard)
  private val from = Square(0, 0)
  private val to = Square(1, 1)


  "A game service" should {
    "save a new game to the store and return the id when starting" in {
      val gameService = new GameService(mockGameStore)
      val expectedGame = Game(playerId, player2Id, board = Board.setup)
      when(mockGameStore.insertGame(expectedGame)).thenReturn(Future(gameId))
      whenReady(gameService.startGame(playerId, player2Id)) {
        _ shouldBe Success(gameId, expectedGame)
      }
      verify(mockGameStore, times(1)).insertGame(expectedGame)
      verifyNoMoreInteractions(mockGameStore)
    }
    "return not found if trying to move in a missing game" in {
      val gameService = new GameService(mockGameStore)
      when(mockGameStore.getGame(gameId)).thenReturn(Future(None))
      whenReady(gameService.doMove(gameId, Square(0, 0), Square(1, 1), None)) {
        _ shouldBe Missing
      }
    }
    "allow a valid standard move" in {
      val gameService = new GameService(mockGameStore)
      when(mockGameStore.getGame(gameId)).thenReturn(Future.successful(Some(game)))
      when(mockBoard.standardMove(from, to)).thenReturn(mockStandardBoard)
      when(mockStandardBoard.valid).thenReturn(true)
      val nextGame = game.copy(board = mockStandardBoard)
      when(mockGameStore.saveGame(gameId, nextGame)).thenReturn(Future.successful(Success(gameId, nextGame)))
      whenReady(gameService.doMove(gameId, from, to, None)) {
        _ shouldBe Success(gameId, nextGame)
      }
      verify(mockGameStore).saveGame(gameId, nextGame)
    }
    "allow a valid castling move" in {
      val gameService = new GameService(mockGameStore)
      when(mockGameStore.getGame(gameId)).thenReturn(Future.successful(Some(game)))
      when(mockBoard.standardMove(from, to)).thenReturn(mockStandardBoard)
      when(mockStandardBoard.valid).thenReturn(false)
      when(mockBoard.castlingMove(from, to)).thenReturn(mockCastlingBoard)
      when(mockCastlingBoard.valid).thenReturn(true)
      val nextGame = game.copy(board = mockCastlingBoard)
      when(mockGameStore.saveGame(gameId, nextGame)).thenReturn(Future.successful(Success(gameId, nextGame)))
      whenReady(gameService.doMove(gameId, from, to, None)) {
        _ shouldBe Success(gameId, nextGame)
      }
      verify(mockGameStore).saveGame(gameId, nextGame)
    }
    "allow a valid en passant move" in {
      val gameService = new GameService(mockGameStore)
      when(mockGameStore.getGame(gameId)).thenReturn(Future.successful(Some(game)))
      when(mockBoard.standardMove(from, to)).thenReturn(mockStandardBoard)
      when(mockStandardBoard.valid).thenReturn(false)
      when(mockBoard.castlingMove(from, to)).thenReturn(mockCastlingBoard)
      when(mockCastlingBoard.valid).thenReturn(false)
      when(mockBoard.enPassantMove(from, to)).thenReturn(mockEnPassantBoard)
      when(mockEnPassantBoard.valid).thenReturn(true)
      val nextGame = game.copy(board = mockEnPassantBoard)
      when(mockGameStore.saveGame(gameId, nextGame)).thenReturn(Future.successful(Success(gameId, nextGame)))
      whenReady(gameService.doMove(gameId, from, to, None)) {
        _ shouldBe Success(gameId, nextGame)
      }
      verify(mockGameStore).saveGame(gameId, nextGame)
    }
    "allow a valid pawn promotion move" in {
      val gameService = new GameService(mockGameStore)
      when(mockGameStore.getGame(gameId)).thenReturn(Future.successful(Some(game)))
      when(mockBoard.pawnPromotionMove(from, to, WhiteQueen)).thenReturn(mockPawnPromotionBoard)
      when(mockPawnPromotionBoard.valid).thenReturn(true)
      val nextGame = game.copy(board = mockPawnPromotionBoard)
      when(mockGameStore.saveGame(gameId, nextGame)).thenReturn(Future.successful(Success(gameId, nextGame)))
      whenReady(gameService.doMove(gameId, from, to, Some(WhiteQueen))) {
        _ shouldBe Success(gameId, nextGame)
      }
      verify(mockGameStore).saveGame(gameId, nextGame)
    }
    "not allow an invalid pawn promotion move" in {
      val gameService = new GameService(mockGameStore)
      when(mockGameStore.getGame(gameId)).thenReturn(Future.successful(Some(game)))
      when(mockBoard.pawnPromotionMove(from, to, WhiteQueen)).thenReturn(mockPawnPromotionBoard)
      when(mockPawnPromotionBoard.valid).thenReturn(false)
      val nextGame = game.copy(board = mockPawnPromotionBoard)
      whenReady(gameService.doMove(gameId, from, to, Some(WhiteQueen))) {
        _ shouldBe InvalidMove
      }
      verify(mockGameStore, never()).saveGame(eql(gameId), any())
    }
    "not allow an invalid non promotion move" in {
      val gameService = new GameService(mockGameStore)
      when(mockGameStore.getGame(gameId)).thenReturn(Future.successful(Some(game)))
      when(mockBoard.standardMove(from, to)).thenReturn(mockStandardBoard)
      when(mockStandardBoard.valid).thenReturn(false)
      when(mockBoard.castlingMove(from, to)).thenReturn(mockCastlingBoard)
      when(mockCastlingBoard.valid).thenReturn(false)
      when(mockBoard.enPassantMove(from, to)).thenReturn(mockEnPassantBoard)
      when(mockEnPassantBoard.valid).thenReturn(false)
      whenReady(gameService.doMove(gameId, from, to, None)) {
        _ shouldBe InvalidMove
      }
      verify(mockGameStore, never()).saveGame(eql(gameId), any())
    }
  }
}
