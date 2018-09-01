package chess.model

import chess.core.{Checkmate, Colour, Game, Piece, Square}

case class DisplayGame(id: String, whitePlayerId: String, blackPlayerId: String, toMove: Colour,
                       checkmate: Boolean, currentPositions: Set[DisplaySquare])

object DisplayGame {
  def apply(id: String, game: Game): DisplayGame = new DisplayGame(
    id, game.whitePlayerId, game.blackPlayerId, game.board.toMove,
    game.board.result match {
      case Some(Checkmate(_)) => true
      case _ => false
    },
    game.board.currentPositions.map(t => DisplaySquare(t._1, t._2)).toSet
  )
}
