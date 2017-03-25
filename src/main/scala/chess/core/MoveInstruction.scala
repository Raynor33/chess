package chess.core

sealed trait MoveInstruction {
  def applyTo(game: Board) : Board
}

case class StandardMoveInstruction(from: Square, to: Square) extends MoveInstruction {
  override def applyTo(game: Board) = StandardMove(from, to, game)
}
case class CastlingMoveInstruction(from: Square, to: Square) extends MoveInstruction {
  override def applyTo(game: Board) = CastlingMove(from, to, game)
}
case class EnPassantMoveInstruction(from: Square, to: Square) extends MoveInstruction {
  override def applyTo(game: Board) = EnPassantMove(from, to, game)
}
case class PawnPromotionMoveInstruction(from: Square, to: Square, piece: Piece) extends MoveInstruction {
  override def applyTo(game: Board) = PawnPromotionMove(from, to, piece, game)
}