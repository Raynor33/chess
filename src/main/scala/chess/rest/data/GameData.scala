package chess.rest.data

import chess.domain.Colour
import chess.domain.{Checkmate, Piece, Square}
import chess.service.Game

case class GameData(id: String, whitePlayerId: String, blackPlayerId: String, toMove: Option[Colour],
                    checkmate: Boolean, currentPositions: Set[PositionData])

object GameData {
  def apply(id: String, game: Game): GameData = new GameData(
    id, game.whitePlayerId, game.blackPlayerId, game.board.toMove,
    game.board.result match {
      case Some(Checkmate(_)) => true
      case _ => false
    },
    game.board.positions.map(t => PositionData(t._1, t._2)).toSet
  )
}
