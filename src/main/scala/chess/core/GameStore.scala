package chess.core

trait GameStore {
  def getGame(id: String): Option[Board]
  def saveGame(id: Option[String], game: Board): String
}