package chess.store

import chess.core._
import chess.service.{Missing, Success}
import chess.utils.FreePortFixture
import com.github.simplyscala.MongoEmbedDatabase
import org.scalatest.concurrent.ScalaFutures
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{Matchers, WordSpec}
import play.api.Configuration

class MongoGameStoreSpec extends WordSpec with Matchers with ScalaFutures with MockitoSugar with FreePortFixture with MongoEmbedDatabase {

  val mockConfiguration: Configuration = mock[Configuration]
  implicit val defaultPatience = PatienceConfig(timeout = Span(1, Seconds), interval = Span(1, Millis))

  "MongoGameStore should allow insert" in {
    withFreePortFixture { port =>
      withEmbedMongoFixture(port) { mongodProps =>
        when(mockConfiguration.getStringList("mongo.uri")).thenReturn(Some(java.util.Arrays.asList(s"localhost:$port")))
        val gameStore = new MongoGameStore(mockConfiguration)
        whenReady(gameStore.insertGame(Game("1", "2", NilBoard))) {
          _.nonEmpty shouldBe true
        }
      }
    }
  }

  "MongoGameStore give none when missing" in {
    withFreePortFixture { port =>
      withEmbedMongoFixture(port) { mongodProps =>
        when(mockConfiguration.getStringList("mongo.uri")).thenReturn(Some(java.util.Arrays.asList(s"localhost:$port")))
        val gameStore = new MongoGameStore(mockConfiguration)
        whenReady(gameStore.getGame("missing")) {
          _ shouldBe None
        }
      }
    }
  }

  "MongoGameStore should allow correct retrieval" in {
    withFreePortFixture { port =>
      withEmbedMongoFixture(port) { mongodProps =>
        when(mockConfiguration.getStringList("mongo.uri")).thenReturn(Some(java.util.Arrays.asList(s"localhost:$port")))
        val gameStore = new MongoGameStore(mockConfiguration)
        val game = Game("1", "2", NilBoard)
        val game2 = Game("3", "4", NilBoard)
        whenReady(gameStore.insertGame(game)) {id =>
          whenReady(gameStore.insertGame(game2)) { id2 =>
            whenReady(gameStore.getGame(id)) {
              _ shouldBe Some(game)
            }
            whenReady(gameStore.getGame(id2)) {
              _ shouldBe Some(game2)
            }
          }
        }
      }
    }
  }

  "MongoGameStore should allow update" in {
    withFreePortFixture { port =>
      withEmbedMongoFixture(port) { mongodProps =>
        when(mockConfiguration.getStringList("mongo.uri")).thenReturn(Some(java.util.Arrays.asList(s"localhost:$port")))
        val gameStore = new MongoGameStore(mockConfiguration)
        val game = Game("1", "2", NilBoard)
        val game2 = Game("1", "2", StandardMoveBoard(Square(1,2), Square(2,2), NilBoard))
        whenReady(gameStore.insertGame(game)) {id =>
          whenReady(gameStore.saveGame(id, game2)) {r =>
            r shouldBe Success(id, game2)
            whenReady(gameStore.getGame(id)) { g =>
              g shouldBe Some(game2)
            }
          }
        }
      }
    }
  }

  "MongoGameStore should return NotFound when missing" in {
    withFreePortFixture { port =>
      withEmbedMongoFixture(port) { mongodProps =>
        when(mockConfiguration.getStringList("mongo.uri")).thenReturn(Some(java.util.Arrays.asList(s"localhost:$port")))
        val gameStore = new MongoGameStore(mockConfiguration)
        val game = Game("1", "2", NilBoard)
        val game2 = Game("1", "2", StandardMoveBoard(Square(1,2), Square(2,2), NilBoard))
        whenReady(gameStore.insertGame(game)) {id =>
          whenReady(gameStore.saveGame("missing", game2)) {r =>
            r shouldBe Missing
            whenReady(gameStore.getGame(id)) { g =>
              g shouldBe Some(game)
            }
          }
        }
      }
    }
  }
}
