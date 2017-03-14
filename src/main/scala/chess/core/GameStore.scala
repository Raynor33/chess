package chess.core

trait GameStore {
  def getGame(id: String): Option[Game]
  def saveGame(id: Option[String], game: Game): String
}