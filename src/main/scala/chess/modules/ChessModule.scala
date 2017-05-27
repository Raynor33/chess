package chess.modules

import chess.store.{GameStore, MongoGameStore}
import com.google.inject.AbstractModule
import com.google.inject.name.Names
import play.api.{Configuration, Environment}
import collection.JavaConverters._

class ChessModule(environment: Environment, configuration: Configuration) extends AbstractModule {
  override def configure() = {
    bind(classOf[GameStore]).to(classOf[MongoGameStore])
  }
}
