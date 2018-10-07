package chess

import chess.persistence.MongoGameStore
import chess.service.GameStore
import com.google.inject.AbstractModule
import play.api.{Configuration, Environment}

class ChessModule(environment: Environment, configuration: Configuration) extends AbstractModule {
  override def configure() = {
    bind(classOf[GameStore]).to(classOf[MongoGameStore])
  }
}
