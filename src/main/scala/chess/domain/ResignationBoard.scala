package chess.domain

case class ResignationBoard(colour: Colour, previous: Board) extends Board {
  override def positions: Map[Square, Piece] = previous.positions

  override def toMove: Option[Colour] = None

  override def result: Option[Result] = Some(Resignation(colour))

  override def neverMoved(square: Square): Boolean = previous.neverMoved(square)

  override def valid: Boolean = true

  override def lastFrom: Option[Square] = None

  override def lastTo: Option[Square] = None
}
