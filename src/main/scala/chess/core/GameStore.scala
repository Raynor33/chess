package chess.core

import scala.concurrent.Future

trait GameStore {
  def getGame(id: String): Future[Option[Game]]
  def insertGame(game: Game): Future[String]
  def saveGame(id: String, game: Game): Future[GameServiceResponse]
}