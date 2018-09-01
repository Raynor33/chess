package chess.service

import chess.core.board.{Board, NilBoard}
import chess.core.{Game, White}
import chess.model.MoveInstruction
import chess.store.GameStore
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, OneInstancePerTest, WordSpec}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GameServiceSpec extends WordSpec with Matchers with OneInstancePerTest with MockitoSugar with ScalaFutures {

  private val playerId = "somePlayer"
  private val player2Id = "someOtherPlayer"
  private val gameId = "someGame"
  private val moveInstruction = mock[MoveInstruction]
  private val mockBoard = mock[Board]
  private val mockBoard2 = mock[Board]
  private val mockGameStore = mock[GameStore]
  private val game = Game(playerId, player2Id, mockBoard)
  private val game2 = Game(playerId, player2Id, mockBoard2)

  object MockGameStore extends GameStore {
    override def getGame(id: String): Future[Option[Game]] = mockGameStore.getGame(id)

    override def insertGame(game: Game): Future[String] = mockGameStore.insertGame(game)

    override def saveGame(id: String, game: Game): Future[GameServiceResponse] = mockGameStore.saveGame(id, game)
  }

  "A game service" should {
    "save a Nil game to the store and return the id when starting" in new GameService(MockGameStore) {
      val expectedGame = Game(playerId, player2Id, board = NilBoard)
      when(mockGameStore.insertGame(expectedGame)).thenReturn(Future(gameId))
      whenReady(startGame(playerId, player2Id)) {
        _ shouldBe Success(gameId, expectedGame)
      }
      verify(mockGameStore, times(1)).insertGame(expectedGame)
      verifyNoMoreInteractions(mockGameStore)
    }
    "return not found if trying to move in a missing game" in new GameService(MockGameStore) {
      when(mockGameStore.getGame(gameId)).thenReturn(Future(None))
      whenReady(doMove(gameId, moveInstruction)) {
        _ shouldBe Missing
      }
    }
    when(mockGameStore.getGame(gameId)).thenReturn(Future(Some(game)))
    when(mockBoard.toMove).thenReturn(White)
    when(moveInstruction.applyTo(mockBoard)).thenReturn(mockBoard2)
    when(mockBoard2.valid).thenReturn(true)
    "allow a valid move" in new GameService(MockGameStore) {
      when(mockGameStore.saveGame(gameId, game2)).thenReturn(Future.successful(Success(gameId, game2)))
      whenReady(doMove(gameId, moveInstruction)) {
        _ shouldBe Success(gameId, game2)
      }
      verify(mockGameStore).saveGame(gameId, game2)
    }
    "not allow an illegal move" in new GameService(MockGameStore) {
      when(mockBoard2.valid).thenReturn(false)
      whenReady(doMove(gameId, moveInstruction)) {
        _ shouldBe InvalidMove(gameId, game)
      }
      verify(mockGameStore, never()).saveGame(gameId, game2)
    }
  }
}
