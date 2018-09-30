package chess.core.board

import chess.core.Square

case class CastlingBoard(from: Square, to: Square, previousBoard: Board) extends MoveBoard {
  private val castleFrom = if (to.x > from.x) Square(7, from.y) else Square(0, from.y)
  private val castleTo = if (to.x > from.x) Square(5, from.y) else Square(3, from.y)
  private val moveSignum = Integer.signum(to.x - from.x)

  override def fromOption: Option[Square] = Some(from)

  override def toOption: Option[Square] = Some(to)

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
    val previousPositions = previousBoard.currentPositions
    previousBoard.hasNeverMoved(from) &&
      previousBoard.hasNeverMoved(castleFrom) &&
      previousPositions.get(from).exists(p =>
        p.isKing && p.colour == previousBoard.toMove) &&
      Math.abs(to.x - from.x) == 2 &&
      to.y == from.y &&
      !fromOrThroughCheck &&
      (from.x + moveSignum until castleFrom.x).forall(x =>
        !previousPositions.contains(Square(x, from.y))
      )
  }

  private def fromOrThroughCheck = {
    val allPositions = currentPositions
    currentPositions.filter(_._2.colour == previousBoard.toMove.opposite)
      .exists(t =>
        t._2.pathFor(t._1, from, true).exists(path =>
          allPositions.keySet.intersect(path).isEmpty
        ) || t._2.pathFor(t._1, from.copy(x = from.x + moveSignum), true).exists(path =>
          allPositions.keySet.intersect(path).isEmpty
        )
      )
  }

}
