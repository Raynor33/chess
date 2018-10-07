package chess.service

import javax.inject.Inject

import chess.domain._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GameService @Inject() (gameStore: GameStore) {

  def getGame(id: String) = gameStore.getGame(id)

  def startGame(whitePlayerId: String, blackPlayerId: String) = {
    val game = Game(whitePlayerId, blackPlayerId, board = NilBoard)
    gameStore.insertGame(game).map { id =>
      Success(id, game)
    }
  }

  def doMove(gameId: String, moveInstruction: MoveInstruction): Future[GameServiceResponse] = {
    gameStore.getGame(gameId).flatMap {
      case None => Future.successful(Missing)
      case Some(game) => {
        val result = moveInstruction.applyTo(game.board)
        if (result.valid) {
          gameStore.saveGame(gameId, game.copy(board = result))
        }
        else {
          Future.successful(InvalidMove(gameId, game))
        }
      }
    }
  }
}

trait GameServiceResponse

case class Success(id: String, game: Game) extends GameServiceResponse
case object Missing extends GameServiceResponse
case class InvalidMove(id: String, game: Game) extends GameServiceResponse