package chess.store

import chess.core.Game
import chess.service.GameServiceResponse

import scala.concurrent.Future

trait GameStore {
  def getGame(id: String): Future[Option[Game]]
  def insertGame(game: Game): Future[String]
  def saveGame(id: String, game: Game): Future[GameServiceResponse]
}