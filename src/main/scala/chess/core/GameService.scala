package chess.core

class GameService(gameStore: GameStore) {
  def startGame = gameStore.saveGame(None, Nil)
  def doMove(gameId: String, moveInstruction: MoveInstruction) = gameStore.getGame(gameId).exists(g => {
    val result = moveInstruction.applyTo(g)
    if (result.valid) {
      gameStore.saveGame(Some(gameId), result)
    }
    result.valid
  })
}