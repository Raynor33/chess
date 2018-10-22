package chess.rest.controllers

import javax.inject.{Inject, Singleton}

import chess.rest.request.{MoveRequest, StartGameRequest}
import chess.rest.response.GameResponse
import chess.service._
import chess.service.{GameService, Missing}
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ChessController @Inject() (gameService: GameService) extends Controller {

  def start = Action.async(parse.json) { request =>
    val startGameRequest = request.body.as[StartGameRequest]
    gameService.startGame(startGameRequest.whitePlayerId, startGameRequest.blackPlayerId).map({
      case Success(id, game) => Created(Json.toJson(GameResponse(id, game)))
        .withHeaders("Location" -> (request.path + "/" + id))
      case _ => InternalServerError
    })
  }

  def move(id: String) = Action.async(parse.json) { request =>
    val move = request.body.as[MoveRequest]
    //TODO implement without reference to domain internals
//    def moveIndex(board: Board): Int = board match {
//      case m: MoveBoard => 1 + moveIndex(m.previousBoard)
//      case NilBoard => -1
//    }
    gameService.doMove(id, move.from, move.to, move.promotion).map {
      case Success(_, game) => Created(Json.toJson(move))
//        .withHeaders("Location" -> (request.path + "/" + moveIndex(game.board)))
      case InvalidMove => Conflict("")
      case Missing => NotFound("")
    }
  }

  def get(id: String) = Action.async { request =>
    gameService.getGame(id).map(o =>
      o.map(g => Ok(Json.toJson(GameResponse(id, g)))).getOrElse(NotFound(""))
    )
  }
}
