package chess.domain

case class PawnPromotionBoard(from: Square, to: Square, promotion: Piece, previousBoard: Board) extends MoveBoard {

  override def lastFrom: Option[Square] = Some(from)

  override def lastTo: Option[Square] = Some(to)

  override lazy val positions = previousBoard.positions - from + (to -> promotion)

  override def moveLegal = previousBoard.positions.get(from).exists(_ match {
    case p: Pawn => StandardBoard(from, to, previousBoard).moveLegal
    case _ => false
  }) && (promotion match {
    case p: Pawn => false
    case k: King => false
    case _ => previousBoard.toMove.contains(promotion.colour)
  })
}
