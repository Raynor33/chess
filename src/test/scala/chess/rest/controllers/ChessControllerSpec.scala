package chess.rest.controllers

import chess.domain.{NilBoard, Square}
import chess.rest.request.{MoveRequest, StartGameRequest}
import chess.rest.response.GameResponse
import chess.service._
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import play.api.mvc.{Result, Results}
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.Future

class ChessControllerSpec extends PlaySpec with Results with MockitoSugar with BeforeAndAfter {

  val gameService = mock[GameService]
  val controller = new ChessController(gameService)

  before {
    reset(gameService)
  }

//  "A ChessController" should {
//    "start a game" in {
//      val instruction = StartGameRequest("white", "black")
//      val id = "id"
//      val game = Game(instruction.whitePlayerId, instruction.blackPlayerId, NilBoard)
//      when(gameService.startGame(instruction.whitePlayerId, instruction.blackPlayerId))
//        .thenReturn(Future.successful(Success(id, game)))
//      val result: Future[Result] = controller.start()(FakeRequest("POST", "/games").withBody(
//        Json.toJson(instruction))
//      )
//      contentAsString(result) mustBe Json.toJson(GameResponse(id, game)).toString()
//      header("Location", result) mustBe Some("/games/id")
//      status(result) mustBe 201
//      verify(gameService, times(1)).startGame(instruction.whitePlayerId, instruction.blackPlayerId)
//    }
//    "give the correct response for the first successful move" ignore {
//      val instruction = StandardMoveInstruction(Square(1,2), Square(3,4))
//      val id = "id"
//      val game = Game("white", "black", instruction.applyTo(NilBoard))
//      when(gameService.doMove(id, instruction)).thenReturn(Future.successful(Success(id, game)))
//      val result: Future[Result] = controller.move(id)(FakeRequest("POST", "/games/" + id + "/moves").withBody(
//        Json.toJson(instruction))
//      )
//      Json.fromJson[MoveRequest](Json.parse(contentAsString(result))) mustBe MoveData(id, instruction)
//      header("Location", result) mustBe Some("/games/id/moves/0")
//      status(result) mustBe 201
//      verify(gameService, times(1)).doMove(id, instruction)
//    }
//    "give the correct response for the second successful move" ignore {
//      val instruction = StandardMoveInstruction(Square(1,2), Square(3,4))
//      val instruction1 = StandardMoveInstruction(Square(5,6), Square(7,0))
//      val id = "id"
//      val game = Game("white", "black", instruction1.applyTo(instruction.applyTo(NilBoard)))
//      when(gameService.doMove(id, instruction1)).thenReturn(Future.successful(Success(id, game)))
//      val result: Future[Result] = controller.move(id)(FakeRequest("POST", "/games/" + id + "/moves").withBody(
//        Json.toJson(instruction1))
//      )
//      contentAsString(result) mustBe Json.toJson(MoveData(id, instruction1)).toString()
//      header("Location", result) mustBe Some("/games/id/moves/1")
//      status(result) mustBe 201
//      verify(gameService, times(1)).doMove(id, instruction1)
//    }
//    "give the correct response for an invalid move" in {
//      val instruction = StandardMoveInstruction(Square(1,2), Square(3,4))
//      val id = "id"
//      val game = Game("white", "black", NilBoard)
//      when(gameService.doMove(id, instruction)).thenReturn(Future.successful(InvalidMove(id, game)))
//      val result: Future[Result] = controller.move(id)(FakeRequest("POST", "/games/" + id + "/moves").withBody(
//        Json.toJson(instruction))
//      )
//      status(result) mustBe 409
//      verify(gameService, times(1)).doMove(id, instruction)
//    }
//    "give the correct response for a missing game" in {
//      val instruction = StandardMoveInstruction(Square(1,2), Square(3,4))
//      val id = "id"
//      when(gameService.doMove(id, instruction)).thenReturn(Future.successful(Missing))
//      val result: Future[Result] = controller.move(id)(FakeRequest("POST", "/games/" + id + "/moves").withBody(
//        Json.toJson(instruction))
//      )
//      status(result) mustBe 404
//      verify(gameService, times(1)).doMove(id, instruction)
//    }
//    "give the game in the response for a successful get" in {
//      val id = "id"
//      val game = Game("white", "black", NilBoard)
//      when(gameService.getGame(id)).thenReturn(Future.successful(Some(game)))
//      val result: Future[Result] = controller.get(id)(FakeRequest("GET", "/games/" + id))
//      status(result) mustBe 200
//      contentAsString(result) mustBe Json.toJson(GameData(id, game)).toString()
//      verify(gameService, times(1)).getGame(id)
//    }
//    "give a 404 response for a when missing on get" in {
//      val id = "id"
//      when(gameService.getGame(id)).thenReturn(Future.successful(None))
//      val result: Future[Result] = controller.get(id)(FakeRequest("GET", "/games/" + id))
//      status(result) mustBe 404
//      verify(gameService, times(1)).getGame(id)
//    }
//  }
}
