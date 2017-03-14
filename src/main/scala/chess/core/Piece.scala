package chess.core

trait Coloured {
  def colour: Colour
}

trait Piece extends Coloured {
  def pathFor(from: Square, to: Square, takingPiece: Boolean): Option[Set[Square]]
  def isKing = false
}

case object WhiteKing extends King with WhiteColoured
case object WhiteQueen extends Queen with WhiteColoured
case object WhiteBishop extends Bishop with WhiteColoured
case object WhiteKnight extends Knight with WhiteColoured
case object WhiteRook extends Rook with WhiteColoured
case object WhitePawn extends Pawn with WhiteColoured

case object BlackKing extends King with BlackColoured
case object BlackQueen extends Queen with BlackColoured
case object BlackBishop extends Bishop with BlackColoured
case object BlackKnight extends Knight with BlackColoured
case object BlackRook extends Rook with BlackColoured
case object BlackPawn extends Pawn with BlackColoured

trait WhiteColoured extends Coloured {
  val colour = White
}

trait BlackColoured extends Coloured {
  val colour = Black
}

trait King extends Piece {
  def pathFor(from: Square, to: Square, takingPiece: Boolean) = from.flatLineTo(to, 1)
    .orElse(from.diagonalLineTo(to, 1))
  override def isKing = true
}

trait Queen extends Piece {
  def pathFor(from: Square, to: Square, takingPiece: Boolean) = from.flatLineTo(to, 8)
    .orElse(from.diagonalLineTo(to, 8))
}

trait Bishop extends Piece {
  def pathFor(from: Square, to: Square, takingPiece: Boolean) = from.diagonalLineTo(to, 8)
}

trait Knight extends Piece {
  def pathFor(from: Square, to: Square, takingPiece: Boolean) = {
    val xDiff = Math.abs(to.x - from.x)
    val yDiff = Math.abs(to.y - from.y)
    if (xDiff.max(yDiff) == 2 && xDiff.min(yDiff) == 1) Some(Set.empty[Square]) else None
  }
}

trait Rook extends Piece {
  def pathFor(from: Square, to: Square, takingPiece: Boolean) = from.flatLineTo(to, 8)
}

trait Pawn extends Piece {
  def pathFor(from: Square, to: Square, takingPiece: Boolean) = {
    if (Integer.signum(to.y - from.y) != colour.moveDirection) None
    else if (takingPiece) from.diagonalLineTo(to, 1)
    else from.flatLineTo(to, if (from.y == colour.pawnRow) 2 else 1)
  }
}
