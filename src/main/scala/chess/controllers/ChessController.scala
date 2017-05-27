package chess.controllers

import javax.inject.{Inject, Singleton}

import chess.core.{Board, MoveBoard, NilBoard}
import chess.model._
import chess.service.{GameService, InvalidMove, Missing, Success}
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ChessController @Inject() (gameService: GameService) extends Controller {

  def start = Action.async(parse.json) { request =>
    val instruction = request.body.as[StartGameInstruction]
    gameService.startGame(instruction.whitePlayerId, instruction.blackPlayerId).map({
      case Success(id, game) => Created(Json.writes.writes(DisplayGame(id, game)))
        .withHeaders("Location" -> (request.path + "/" + id))
      case _ => InternalServerError
    })
  }

  def move(id: String) = Action.async(parse.json) { request =>
    val instruction = request.body.as[MoveInstruction]
    def moveIndex(board: Board): Int = board match {
      case m: MoveBoard => 1 + moveIndex(m.previousBoard)
      case NilBoard => -1
    }
    gameService.doMove(id, instruction).map {
      case Success(_, game) => Created(Json.writes.writes(DisplayMove(id, instruction)))
        .withHeaders("Location" -> (request.path + "/" + moveIndex(game.board)))
      case InvalidMove(_, game) => Conflict("")
      case Missing => NotFound("")
    }
  }

  def get(id: String) = Action.async { request =>
    gameService.getGame(id).map(o =>
      o.map(g => Ok(Json.writes.writes(DisplayGame(id, g)))).getOrElse(NotFound(""))
    )
  }
}
