package chess.core

sealed trait MoveInstruction {
  def applyTo(game: Game) : Game
}

case class StandardMoveInstruction(from: Square, to: Square) extends MoveInstruction {
  override def applyTo(game: Game) = StandardMove(from, to, game)
}
case class CastlingMoveInstruction(from: Square, to: Square) extends MoveInstruction {
  override def applyTo(game: Game) = CastlingMove(from, to, game)
}
case class EnPassantMoveInstruction(from: Square, to: Square) extends MoveInstruction {
  override def applyTo(game: Game) = EnPassantMove(from, to, game)
}
case class PawnPromotionMoveInstruction(from: Square, to: Square, piece: Piece) extends MoveInstruction {
  override def applyTo(game: Game) = PawnPromotionMove(from, to, piece, game)
}