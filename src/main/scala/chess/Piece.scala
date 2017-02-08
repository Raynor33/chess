package chess

trait Coloured {
  def colour: Colour
}

sealed trait Piece extends Coloured {
  def pathFor(from: Square, to: Square): Option[Set[Square]]
}

case object WhiteKing extends Piece with WhiteColoured with King
case object WhiteQueen extends Piece with WhiteColoured with Queen
case object WhiteBishop extends Piece with WhiteColoured with Bishop
case object WhiteKnight extends Piece with WhiteColoured with Knight
case object WhiteRook extends Piece with WhiteColoured with Rook
case object WhitePawn extends Piece with WhiteColoured with Pawn

case object BlackKing extends Piece with BlackColoured with King
case object BlackQueen extends Piece with BlackColoured with Queen
case object BlackBishop extends Piece with BlackColoured with Bishop
case object BlackKnight extends Piece with BlackColoured with Knight
case object BlackRook extends Piece with BlackColoured with Rook
case object BlackPawn extends Piece with BlackColoured with Pawn

trait WhiteColoured extends Coloured {
  val colour = White
}

trait BlackColoured extends Coloured {
  val colour = Black
}

trait King {
  def pathFor(from: Square, to: Square): Option[Set[Square]] = from.flatLineTo(to, 1)
    .orElse(from.diagonalLineTo(to, 1))
}

trait Queen {
  def pathFor(from: Square, to: Square): Option[Set[Square]] = from.flatLineTo(to, 8)
    .orElse(from.diagonalLineTo(to, 8))
}

trait Bishop {
  def pathFor(from: Square, to: Square): Option[Set[Square]] = from.diagonalLineTo(to, 8)
}

trait Knight {
  def pathFor(from: Square, to: Square): Option[Set[Square]] = {
    val xDiff = Math.abs(to.x - from.x)
    val yDiff = Math.abs(to.y - from.y)
    if (xDiff.max(yDiff) == 2 && xDiff.min(yDiff) == 1) Some(Set.empty[Square]) else None
  }
}

trait Rook {
  def pathFor(from: Square, to: Square): Option[Set[Square]] = from.flatLineTo(to, 8)
}

trait Pawn {
  this: Coloured =>
  def pathFor(from: Square, to: Square): Option[Set[Square]] = ???
}