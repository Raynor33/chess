package chess

import chess.core.{Black, BlackBishop, BlackKing, BlackKnight, BlackPawn, BlackQueen, BlackRook, Square, White, WhiteBishop, WhiteKing, WhiteKnight, WhitePawn, WhiteQueen, WhiteRook}
import chess.model.{DisplayGame, DisplaySquare, MoveInstruction, StandardMoveInstruction, StartGameInstruction}
import chess.utils.FreePortFixture
import com.github.simplyscala.{MongoEmbedDatabase, MongodProps}
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mock.MockitoSugar
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.ws.WSClient
import play.api.test.Helpers._

class ChessFunctionalSpec extends PlaySpec with FreePortFixture with MockitoSugar with ScalaFutures with OneServerPerSuite with MongoEmbedDatabase with BeforeAndAfterAll {

  private val mongoPort = freePort

  var mongoProps: MongodProps = null

  override def beforeAll() {
    mongoProps = mongoStart(port = mongoPort)
  }

  override def afterAll { mongoStop(mongoProps) }

  implicit val defaultPatience = PatienceConfig(timeout = Span(2, Seconds), interval = Span(5, Millis))

  private case class TestGameStep(instruction: MoveInstruction, expectedTransformation: DisplayGame => DisplayGame)

  override implicit lazy val app: Application = new GuiceApplicationBuilder()
    .configure("mongo.uri" -> List(s"localhost:$mongoPort"))
    .build()

  "a fool's mate game should work" in {
    testFullGame(
      List(
        TestGameStep(
          StandardMoveInstruction(Square(4,1), Square(4,2)),
          prev => prev.copy(
            currentPositions = prev.currentPositions
              .filterNot(s => s.square == Square(4,1))
              + DisplaySquare(Square(4,2), WhitePawn),
            toMove = Some(Black)
          )
        ),
        TestGameStep(
          StandardMoveInstruction(Square(1,7), Square(2,5)),
          prev => prev.copy(
            currentPositions = prev.currentPositions
              .filterNot(s => s.square == Square(1,7))
              + DisplaySquare(Square(2,5), BlackKnight),
            toMove = Some(White)
          )
        ),
        TestGameStep(
          StandardMoveInstruction(Square(5,0), Square(2,3)),
          prev => prev.copy(
            currentPositions = prev.currentPositions
              .filterNot(s => s.square == Square(5,0))
              + DisplaySquare(Square(2,3), WhiteBishop),
            toMove = Some(Black)
          )
        ),
        TestGameStep(
          StandardMoveInstruction(Square(0,6), Square(0,4)),
          prev => prev.copy(
            currentPositions = prev.currentPositions
              .filterNot(s => s.square == Square(0,6))
              + DisplaySquare(Square(0,4), BlackPawn),
            toMove = Some(White)
          )
        ),
        TestGameStep(
          StandardMoveInstruction(Square(3,0), Square(7,4)),
          prev => prev.copy(
            currentPositions = prev.currentPositions
              .filterNot(s => s.square == Square(3,0))
              + DisplaySquare(Square(7,4), WhiteQueen),
            toMove = Some(Black)
          )
        ),
        TestGameStep(
          StandardMoveInstruction(Square(6,7), Square(5,5)),
          prev => prev.copy(
            currentPositions = prev.currentPositions
              .filterNot(s => s.square == Square(6,7))
              + DisplaySquare(Square(5,5), BlackKnight),
            toMove = Some(White)
          )
        ),
        TestGameStep(
          StandardMoveInstruction(Square(7,4), Square(5,6)),
          prev => prev.copy(
            currentPositions = prev.currentPositions
              .filterNot(s => s.square == Square(7,4) || s.square == Square(5,6))
              + DisplaySquare(Square(5,6), WhiteQueen),
            toMove = None,
            checkmate = true
          )
        )
      )
    )
  }

  private def testFullGame(steps: List[TestGameStep]) = {
    val wsClient = app.injector.instanceOf[WSClient]
    val startInstruction = StartGameInstruction("white", "black")
    val startResponse = await(wsClient.url(s"http://localhost:$port/games").post(
      chess.model.startGameInstruction.writes(startInstruction)
    ))
    val location = startResponse.header("Location").get
    steps.foldLeft(initialDisplayBoard(
      location.substring(location.lastIndexOf("/") + 1),
      startInstruction.whitePlayerId,
      startInstruction.blackPlayerId
    )) { (previous, step) =>
      val moveResponse = await(wsClient.url(s"http://localhost:$port$location/moves").post(
        chess.model.moveInstructionFormats.writes(step.instruction))
      )
      moveResponse.status mustBe 201
      val expected = step.expectedTransformation(previous)
      val getResponse = await(wsClient.url(s"http://localhost:$port$location").get())
      chess.model.displayGame.reads(getResponse.json).get mustBe expected
      expected
    }
  }

  private def initialDisplayBoard(id: String, whitePlayerId: String, blackPlayerId: String) = DisplayGame(
    id = id,
    whitePlayerId = whitePlayerId,
    blackPlayerId = blackPlayerId,
    toMove = Some(White),
    checkmate = false,
    currentPositions = Set(
      DisplaySquare(Square(0,0), WhiteRook),
      DisplaySquare(Square(1,0), WhiteKnight),
      DisplaySquare(Square(2,0), WhiteBishop),
      DisplaySquare(Square(3,0), WhiteQueen),
      DisplaySquare(Square(4,0), WhiteKing),
      DisplaySquare(Square(5,0), WhiteBishop),
      DisplaySquare(Square(6,0), WhiteKnight),
      DisplaySquare(Square(7,0), WhiteRook),
      DisplaySquare(Square(0,1), WhitePawn),
      DisplaySquare(Square(1,1), WhitePawn),
      DisplaySquare(Square(2,1), WhitePawn),
      DisplaySquare(Square(3,1), WhitePawn),
      DisplaySquare(Square(4,1), WhitePawn),
      DisplaySquare(Square(5,1), WhitePawn),
      DisplaySquare(Square(6,1), WhitePawn),
      DisplaySquare(Square(7,1), WhitePawn),
      DisplaySquare(Square(0,7), BlackRook),
      DisplaySquare(Square(1,7), BlackKnight),
      DisplaySquare(Square(2,7), BlackBishop),
      DisplaySquare(Square(3,7), BlackQueen),
      DisplaySquare(Square(4,7), BlackKing),
      DisplaySquare(Square(5,7), BlackBishop),
      DisplaySquare(Square(6,7), BlackKnight),
      DisplaySquare(Square(7,7), BlackRook),
      DisplaySquare(Square(0,6), BlackPawn),
      DisplaySquare(Square(1,6), BlackPawn),
      DisplaySquare(Square(2,6), BlackPawn),
      DisplaySquare(Square(3,6), BlackPawn),
      DisplaySquare(Square(4,6), BlackPawn),
      DisplaySquare(Square(5,6), BlackPawn),
      DisplaySquare(Square(6,6), BlackPawn),
      DisplaySquare(Square(7,6), BlackPawn)
    )
  )
}
