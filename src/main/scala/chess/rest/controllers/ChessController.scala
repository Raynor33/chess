package chess.rest.controllers

import javax.inject.{Inject, Singleton}

import chess.rest.data._
import chess.service._
import chess.service.{GameService, Missing, MoveInstruction, StartGameInstruction}
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ChessController @Inject() (gameService: GameService) extends Controller {

  def start = Action.async(parse.json) { request =>
    val instruction = request.body.as[StartGameInstruction]
    gameService.startGame(instruction.whitePlayerId, instruction.blackPlayerId).map({
      case Success(id, game) => Created(Json.writes.writes(GameData(id, game)))
        .withHeaders("Location" -> (request.path + "/" + id))
      case _ => InternalServerError
    })
  }

  def move(id: String) = Action.async(parse.json) { request =>
    val instruction = request.body.as[MoveInstruction]
    //TODO implement without reference to domain internals
//    def moveIndex(board: Board): Int = board match {
//      case m: MoveBoard => 1 + moveIndex(m.previousBoard)
//      case NilBoard => -1
//    }
    gameService.doMove(id, instruction).map {
      case Success(_, game) => Created(Json.writes.writes(MoveData(id, instruction)))
//        .withHeaders("Location" -> (request.path + "/" + moveIndex(game.board)))
      case InvalidMove(_, game) => Conflict("")
      case Missing => NotFound("")
    }
  }

  def get(id: String) = Action.async { request =>
    gameService.getGame(id).map(o =>
      o.map(g => Ok(Json.writes.writes(GameData(id, g)))).getOrElse(NotFound(""))
    )
  }
}
