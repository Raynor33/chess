package chess

trait Coloured {
  def colour: Colour
}

sealed trait Piece extends Coloured {
  def pathFor(move: Move): Option[Set[Square]]
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
  def pathFor(move: Move): Option[Set[Square]] = ???
}

sealed trait Queen {
  def pathFor(move: Move): Option[Set[Square]] = ???
}

sealed trait Bishop {
  def pathFor(move: Move): Option[Set[Square]] = ???
}

sealed trait Knight {
  def pathFor(move: Move): Option[Set[Square]] = ???
}

sealed trait Rook {
  def pathFor(move: Move): Option[Set[Square]] = ???
}

sealed trait Pawn {
  this: Coloured =>
  def pathFor(move: Move): Option[Set[Square]] = ???
}