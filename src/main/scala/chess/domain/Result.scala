package chess.domain

sealed trait Result {
  def winner: Option[Colour]
}

case class Resignation(resigned: Colour) extends Result {
  override def winner: Option[Colour] = Some(resigned.opposite)
}

case class Checkmate(checkmated: Colour) extends Result {
  override def winner: Option[Colour] = Some(checkmated.opposite)
}
