package chess

class GameService {
  this: GameStore =>
  def startGame: String = ???
  def doMove(gameId: String, moveInstruction: MoveInstruction): Game = ???
}