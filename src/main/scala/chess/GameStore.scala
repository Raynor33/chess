package chess

trait GameStore {
  def getGame(id: String): Option[Game]
  def saveGame(id: String, game: Game)
}
