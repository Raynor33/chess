package chess

trait GameStore {
  def getGame(id: String): Option[Board]
  def saveGame(id: String, board: Board)
}
