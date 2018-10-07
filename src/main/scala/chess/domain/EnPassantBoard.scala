package chess.domain

case class EnPassantBoard(from: Square, to: Square, previousBoard: Board) extends MoveBoard {

  override def lastFrom: Option[Square] = Some(from)

  override def lastTo: Option[Square] = Some(to)

  override lazy val positions = {
    val previousPositions = previousBoard.positions
    (previousPositions
      ++ previousPositions.get(from).map(p =>
        Seq(to -> p)
      ).getOrElse(Nil)
      - from
      - Square(to.x, from.y))
  }

  override def moveLegal = {
    val previousPositions = previousBoard.positions
    (previousBoard.lastFrom, previousBoard.lastTo) match {
      case (Some(previousFrom), Some(previousTo)) =>
        previousPositions.get(Square(to.x, from.y)).exists(_ match {
          case pawn: Pawn =>
            previousFrom.y == pawn.colour.pawnRow &&
            Math.abs(previousTo.y - previousFrom.y) == 2
          case _ => false
        }) &&
        previousPositions.get(from).exists(_ match {
          case pawn: Pawn => previousBoard.toMove.contains(pawn.colour) &&
            pawn.pathFor(from, to, true).isDefined
          case _ => false
        })
      case _ => false
    }
  }
}
