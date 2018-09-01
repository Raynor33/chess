package chess.core.board

import chess.core.{Pawn, Square}

case class EnPassantBoard(from: Square, to: Square, previousBoard: Board) extends MoveBoard {
  override def currentPositions = {
    val previousPositions = previousBoard.currentPositions
    (previousPositions
      ++ previousPositions.get(from).map(p =>
        Seq(to -> p)
      ).getOrElse(Nil)
      - from
      - Square(to.x, from.y))
  }

  override def moveLegal = {
    val previousPositions = previousBoard.currentPositions
    previousBoard match {
      case move: MoveBoard =>
        previousPositions.get(Square(to.x, from.y)).exists(_ match {
          case pawn: Pawn =>
            move.from.y == pawn.colour.pawnRow &&
            Math.abs(move.to.y - move.from.y) == 2
          case _ => false
        }) &&
        previousPositions.get(from).exists(_ match {
          case pawn: Pawn => pawn.colour == previousBoard.toMove &&
            pawn.pathFor(from, to, true).isDefined
          case _ => false
        })
      case _ => false
    }
  }
}
