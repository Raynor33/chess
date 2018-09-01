package chess.core.board

import chess.core.Square

case class CastlingBoard(from: Square, to: Square, previousBoard: Board) extends MoveBoard {
  private val castleFrom = if (to.x > from.x) Square(7, from.y) else Square(0, from.y)
  private val castleTo = if (to.x > from.x) Square(5, from.y) else Square(3, from.y)

  override def currentPositions = {
    val previousPositions = previousBoard.currentPositions
    (previousPositions
      ++ previousPositions.get(from).map(p =>
        Seq(to -> p)
      ).getOrElse(Nil)
      - from
      + (castleTo -> previousPositions(castleFrom))
      - castleFrom)
  }

  override def moveLegal = {
    val moveSignum = Integer.signum(to.x - from.x)
    val previousPositions = previousBoard.currentPositions
    previousBoard.hasNeverMoved(from) &&
      previousBoard.hasNeverMoved(castleFrom) &&
      previousPositions.get(from).exists(p =>
        p.isKing && p.colour == previousBoard.toMove) &&
      Math.abs(to.x - from.x) == 2 &&
      to.y == from.y &&
//      !previousBoard.check(previousBoard.toMove) &&
      !StandardBoard(from, Square(from.x + moveSignum, from.y), previousBoard).check(previousBoard.toMove) &&
      (from.x + moveSignum until castleFrom.x).forall(x =>
        !previousPositions.contains(Square(x, from.y))
      )
  }

}
