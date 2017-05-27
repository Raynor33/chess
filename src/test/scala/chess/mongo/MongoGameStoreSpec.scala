package chess.mongo

import java.net.ServerSocket

import chess.core._
import com.github.simplyscala.MongoEmbedDatabase
import com.sun.xml.internal.bind.api.impl.NameConverter.Standard
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{BeforeAndAfter, Matchers, WordSpec}

class MongoGameStoreSpec extends WordSpec with Matchers with MongoEmbedDatabase with ScalaFutures {

  private def freePort = {
    var socket: ServerSocket = null
    try {
      socket = new ServerSocket(0)
      socket.getLocalPort
    }
    finally {
      socket.close()
    }
  }

  private def withFreePortFixture(f: Int => Any) = {
    f(freePort)
  }
  implicit val defaultPatience = PatienceConfig(timeout = Span(200, Seconds), interval = Span(1, Millis))

  "MongoGameStore should allow insert" in {
    withFreePortFixture { port =>
      withEmbedMongoFixture(port) { mongodProps =>
        val gameStore = MongoGameStore(List(s"localhost:$port"))
        whenReady(gameStore.insertGame(Game("1", "2", NilBoard))) {
          _.nonEmpty shouldBe true
        }
      }
    }
  }

  "MongoGameStore give none when missing" in {
    withFreePortFixture { port =>
      withEmbedMongoFixture(port) { mongodProps =>
        val gameStore = MongoGameStore(List(s"localhost:$port"))
        whenReady(gameStore.getGame("missing")) {
          _ shouldBe None
        }
      }
    }
  }

  "MongoGameStore should allow correct retrieval" in {
    withFreePortFixture { port =>
      withEmbedMongoFixture(port) { mongodProps =>
        val gameStore = MongoGameStore(List(s"localhost:$port"))
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
        val gameStore = MongoGameStore(List(s"localhost:$port"))
        val game = Game("1", "2", NilBoard)
        val game2 = Game("1", "2", StandardMoveBoard(Square(1,2), Square(2,2), NilBoard))
        whenReady(gameStore.insertGame(game)) {id =>
          whenReady(gameStore.saveGame(id, game2)) {r =>
            r shouldBe Success
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
        val gameStore = MongoGameStore(List(s"localhost:$port"))
        val game = Game("1", "2", NilBoard)
        val game2 = Game("1", "2", StandardMoveBoard(Square(1,2), Square(2,2), NilBoard))
        whenReady(gameStore.insertGame(game)) {id =>
          whenReady(gameStore.saveGame("missing", game2)) {r =>
            r shouldBe NotFound
            whenReady(gameStore.getGame(id)) { g =>
              g shouldBe Some(game)
            }
          }
        }
      }
    }
  }
}
