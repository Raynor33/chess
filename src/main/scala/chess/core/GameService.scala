package chess.core

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class GameService {
  this: GameStore =>

  def startGame(whitePlayerId: String, blackPlayerId: String) = insertGame(Game(whitePlayerId, blackPlayerId, board = NilBoard))

  def doMove(gameId: String, playerId: String, moveInstruction: MoveInstruction): Future[GameServiceResponse] = {
    getGame(gameId).flatMap {
      case None => Future.successful(NotFound)
      case Some(game) => {
        val expectedPlayer = if (game.board.toMove == White) game.whitePlayerId else game.blackPlayerId
        if (expectedPlayer != playerId) {
          Future.successful(IncorrectPlayer)
        }
        else {
          val result = moveInstruction.applyTo(game.board)
          if (result.valid) {
            saveGame(gameId, game.copy(board = result))
          }
          else {
            Future.successful(InvalidMove)
          }
        }
      }
    }
  }
}

sealed trait GameServiceResponse

case object Success extends GameServiceResponse
case object NotFound extends GameServiceResponse
case object IncorrectPlayer extends GameServiceResponse
case object InvalidMove extends GameServiceResponse