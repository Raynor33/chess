package chess

trait Move {
  def from: Square
  def to: Square
}

case class SimpleMove(from: Square, to: Square) extends Move
case class EnPassantMove(from: Square, to: Square) extends Move
case class PawnPromotionMove(from: Square, to: Square, piece: Piece) extends Move
case class CastleMove(from: Square, to: Square) extends Move