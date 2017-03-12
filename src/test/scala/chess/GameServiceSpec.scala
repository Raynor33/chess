package chess

import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import org.scalatest.{Matchers, OneInstancePerTest, WordSpec}
import org.scalatest.mockito.MockitoSugar

class GameServiceSpec extends WordSpec with Matchers with OneInstancePerTest with MockitoSugar {

  val mockGameStore = mock[GameStore]
  val gameService = new GameService(mockGameStore)
  "A game service" should {
    "save a Nil game to the store and return the id when starting" in {
      val id = "someid"
      when(mockGameStore.saveGame(None, Nil)).thenReturn(id)
      gameService.startGame should be (id)
      verify(mockGameStore, times(1)).saveGame(None, Nil)
      verifyNoMoreInteractions(mockGameStore)
    }
    "return false if a game doesn't exist" in {
      val id = "someid"
      val moveInstruction = mock[MoveInstruction]
      when(mockGameStore.getGame(id)).thenReturn(None)
      gameService.doMove(id, moveInstruction) should be (false)
      verify(mockGameStore, times(1)).getGame(id)
      verifyNoMoreInteractions(mockGameStore)
    }
    "return false if the resulting game isn't valid" in {
      val id = "someid"
      val moveInstruction = mock[MoveInstruction]
      val game1 = mock[Game]
      val game2 = mock[Game]
      when(game2.valid).thenReturn(false)
      when(moveInstruction.applyTo(game1)).thenReturn(game2)
      when(mockGameStore.getGame(id)).thenReturn(Some(game1))
      gameService.doMove(id, moveInstruction) should be (false)
      verify(mockGameStore, times(1)).getGame(id)
      verifyNoMoreInteractions(mockGameStore)
    }
    "return true if the resulting game is valid and save the result" in {
      val id = "someid"
      val moveInstruction = mock[MoveInstruction]
      val game1 = mock[Game]
      val game2 = mock[Game]
      when(game2.valid).thenReturn(true)
      when(moveInstruction.applyTo(game1)).thenReturn(game2)
      when(mockGameStore.getGame(id)).thenReturn(Some(game1))
      gameService.doMove(id, moveInstruction) should be (true)
      verify(mockGameStore, times(1)).getGame(id)
      verify(mockGameStore, times(1)).saveGame(Some(id), game2)
      verifyNoMoreInteractions(mockGameStore)
    }
  }
}
