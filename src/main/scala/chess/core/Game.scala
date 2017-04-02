package chess.core

case class Game(whitePlayerId: Option[String], blackPlayerId: Option[String], board: Board)
