package chess.core

import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, OneInstancePerTest, WordSpec}
import org.scalatest.mockito.MockitoSugar

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

  trait MockGameStore extends GameStore {
    override def getGame(id: String): Future[Option[Game]] = mockGameStore.getGame(id)

    override def insertGame(game: Game): Future[String] = mockGameStore.insertGame(game)

    override def saveGame(id: String, game: Game): Future[GameServiceResponse] = mockGameStore.saveGame(id, game)
  }

  "A game service" should {
    "save a Nil game to the store and return the id when starting" in new GameService with MockGameStore {
      val expectedGame = Game(playerId, player2Id, board = NilBoard)
      when(mockGameStore.insertGame(expectedGame)).thenReturn(Future(gameId))
      whenReady(startGame(playerId, player2Id)) {
        _ shouldBe gameId
      }
      verify(mockGameStore, times(1)).insertGame(expectedGame)
      verifyNoMoreInteractions(mockGameStore)
    }
    "return not found if trying to move in a missing game" in new GameService with MockGameStore {
      when(mockGameStore.getGame(gameId)).thenReturn(Future(None))
      whenReady(doMove(gameId, playerId, moveInstruction)) {
        _ shouldBe NotFound
      }
    }
    when(mockGameStore.getGame(gameId)).thenReturn(Future(Some(game)))
    when(mockBoard.toMove).thenReturn(White)
    when(moveInstruction.applyTo(mockBoard)).thenReturn(mockBoard2)
    when(mockBoard2.valid).thenReturn(true)
    "allow the correct player to make the move" in new GameService with MockGameStore {
      when(mockGameStore.saveGame(gameId, game2)).thenReturn(Future.successful(Success))
      whenReady(doMove(gameId, playerId, moveInstruction)) {
        _ shouldBe Success
      }
      verify(mockGameStore).saveGame(gameId, game2)
    }
    "not allow the other player to make the move" in new GameService with MockGameStore {
      whenReady(doMove(gameId, player2Id, moveInstruction)) {
        _ shouldBe IncorrectPlayer
      }
      verify(mockGameStore, never()).saveGame(gameId, game2)
    }
    "not allow a random player to make the move" in new GameService with MockGameStore {
      whenReady(doMove(gameId, "someOtherImposter", moveInstruction)) {
        _ shouldBe IncorrectPlayer
      }
      verify(mockGameStore, never()).saveGame(gameId, game2)
    }
    "not allow an illegal move" in new GameService with MockGameStore {
      when(mockBoard2.valid).thenReturn(false)
      whenReady(doMove(gameId, playerId, moveInstruction)) {
        _ shouldBe InvalidMove
      }
      verify(mockGameStore, never()).saveGame(gameId, game2)
    }
  }
}
