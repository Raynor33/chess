package chess

trait GameStore {
  def startGame: String
  def getGame(id: String): Option[Game]
  def saveGame(id: String, game: Game)
}