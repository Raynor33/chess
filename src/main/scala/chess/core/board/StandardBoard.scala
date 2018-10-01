package chess.core.board

import chess.core.Square

case class StandardBoard(from: Square, to: Square, previousBoard: Board) extends MoveBoard {

  override def lastFrom: Option[Square] = Some(from)

  override def lastTo: Option[Square] = Some(to)

  override lazy val positions = {
    val previousPositions = previousBoard.positions
    (previousPositions
      - from
      ++ previousPositions.get(from).map(p =>
        Seq(to -> p)
      ).getOrElse(Nil))
  }

  override def moveLegal = {
    val previousPositions = previousBoard.positions
    previousPositions.get(from).exists(piece =>
      piece.colour == previousBoard.toMove &&
      piece.pathFor(from, to, previousPositions.contains(to)).exists(path =>
        previousPositions.keySet.intersect(path).isEmpty) &&
      previousPositions.get(to).forall(taking =>
        taking.colour != previousBoard.toMove)
    )
  }


}
