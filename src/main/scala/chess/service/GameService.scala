package chess.service

import javax.inject.Inject

import chess.domain._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GameService @Inject() (gameStore: GameStore) {

  def getGame(id: String) = gameStore.getGame(id)

  def startGame(whitePlayerId: String, blackPlayerId: String) = {
    val game = Game(whitePlayerId, blackPlayerId, board = Board.setup)
    gameStore.insertGame(game).map { id =>
      Success(id, game)
    }
  }

  def doMove(gameId: String, from: Square, to: Square, promotion: Option[Piece]): Future[GameServiceResponse] = {
    gameStore.getGame(gameId).flatMap {
      case None => Future.successful(Missing)
      case Some(game) => {
        val result = promotion match {
          case Some(piece) => game.board.pawnPromotionMove(from, to, piece)
          case None => {
            val standardResult = game.board.standardMove(from, to)
            lazy val castlingResult = game.board.castlingMove(from, to)
            lazy val enPassantResult = game.board.enPassantMove(from, to)
            if (standardResult.valid) {
              standardResult
            }
            else if (castlingResult.valid) {
              castlingResult
            }
            else {
              enPassantResult
            }
          }
        }
        if (result.valid) {
          gameStore.saveGame(gameId, game.copy(board = result))
        }
        else {
          Future.successful(InvalidMove)
        }
      }
    }
  }
}

trait GameServiceResponse

case class Success(id: String, game: Game) extends GameServiceResponse
case object Missing extends GameServiceResponse
case object InvalidMove extends GameServiceResponse