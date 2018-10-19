package chess.rest

import chess.utils.FreePortFixture
import com.github.simplyscala.{MongoEmbedDatabase, MongodProps}
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mock.MockitoSugar
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json._
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.test.Helpers._

class ChessFunctionalSpec extends PlaySpec with FreePortFixture with MockitoSugar with ScalaFutures
  with OneServerPerSuite with MongoEmbedDatabase with BeforeAndAfterAll with GivenWhenThen {

  private val mongoPort = freePort

  var mongoProps: MongodProps = null

  override def beforeAll() {
    mongoProps = mongoStart(port = mongoPort)
  }

  override def afterAll {
    mongoStop(mongoProps)
  }

  implicit val defaultPatience = PatienceConfig(timeout = Span(2, Seconds), interval = Span(5, Millis))

  override implicit lazy val app: Application = new GuiceApplicationBuilder()
    .configure("mongo.uri" -> List(s"localhost:$mongoPort"))
    .build()

  "Chess API" should {
    "start a game" in {
      Given("the server is running")
      implicit val host = s"http://localhost:$port"
      implicit val wsClient = app.injector.instanceOf[WSClient]

      var gameLocation: String = null
      whenPost("/games", Data.startGameJson("a", "b")) { implicit response =>
        thenResponseStatus(201)
        thenResponseBody(expected = Data.gameJson(
          JsString("a"), JsString("b"), JsString("white"), JsNull, Data.initialPositions
        ), ignoredKeys = Set("id"))

        gameLocation = response.header("Location").get
      }
    }
    "reject invalid moves" in {
      Given("the server is running")
      implicit val host = s"http://localhost:$port"
      implicit val wsClient = app.injector.instanceOf[WSClient]

      var gameLocation: String = null
      whenPost("/games", Data.startGameJson("a", "b")) { implicit response =>
        thenResponseStatus(200)
        gameLocation = response.header("Location").get
      }
//      whenPost(gameLocation, Data.moveJson()) {
//
//      }
    }
  }

  private def whenGet(path: String)(assertions: WSResponse => Unit)(implicit wsClient: WSClient, host: String) = {
    When(s"I GET from $path")
    val response = await(wsClient.url(s"$host$path").get())
    assertions(response)
  }

  private def whenPost(path: String, body: JsObject)(assertions: WSResponse => Unit)(implicit wsClient: WSClient, host: String) = {
    When(s"I POST to $path with body $body")
    val response = await(wsClient.url(s"$host$path").post(body))
    assertions(response)
  }

  private def thenResponseStatus(expected: Int)(implicit response: WSResponse) = {
    Then(s"the response status is $expected")
    response.status mustBe expected
  }

  private def thenResponseBody(expected: JsValue, ignoredKeys: Set[String] = Set.empty[String])(implicit response: WSResponse) = {
    Then(s"the response body is $expected")
    (response.json match {
      case obj: JsObject => JsObject(obj.fields.filter(field => !ignoredKeys.contains(field._1)))
      case jsVal: JsValue => jsVal
    }) mustBe expected
  }
}

private object Data {
  def startGameJson(white: String, black: String) = {
    JsObject(Map(
      "whitePlayerId" -> JsString(white),
      "blackPlayerId" -> JsString(black)
    ))
  }

  val initialPositions = Json.obj(
    "a1" -> "wR",
    "b1" -> "wN",
    "c1" -> "wB",
    "d1" -> "wQ",
    "e1" -> "wK",
    "f1" -> "wB",
    "g1" -> "wN",
    "h1" -> "wR",
    "a2" -> "wP",
    "b2" -> "wP",
    "c2" -> "wP",
    "d2" -> "wP",
    "e2" -> "wP",
    "f2" -> "wP",
    "g2" -> "wP",
    "h2" -> "wP",
    "a7" -> "bP",
    "b7" -> "bP",
    "c7" -> "bP",
    "d7" -> "bP",
    "e7" -> "bP",
    "f7" -> "bP",
    "g7" -> "bP",
    "h7" -> "bP",
    "a8" -> "bR",
    "b8" -> "bN",
    "c8" -> "bB",
    "d8" -> "bQ",
    "e8" -> "bK",
    "f8" -> "bB",
    "g8" -> "bN",
    "h8" -> "bR"
  )

  def gameJson(whiteId: JsValue, blackId: JsValue, toMove: JsValue, result: JsValue, positions: JsValue) = {
    Json.obj(
      "whitePlayerId" -> whiteId,
      "blackPlayerId" -> blackId,
      "toMove" -> toMove,
      "result" -> result,
      "positions" -> positions
    )
  }
}
