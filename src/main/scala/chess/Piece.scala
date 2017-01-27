package chess

sealed trait Piece {
  def colour: Colour
  def pathFor(move: Move): Option[Set[Square]]
}

case object WhiteKing extends Piece with White with King
case object WhiteQueen extends Piece with White with Queen
case object WhiteBishop extends Piece with White with Bishop
case object WhiteKnight extends Piece with White with Knight
case object WhiteRook extends Piece with White with Rook
case object WhitePawn extends Piece with White with Pawn

case object BlackKing extends Piece with Black with King
case object BlackQueen extends Piece with Black with Queen
case object BlackBishop extends Piece with Black with Bishop
case object BlackKnight extends Piece with Black with Knight
case object BlackRook extends Piece with Black with Rook
case object BlackPawn extends Piece with Black with Pawn

private sealed trait White {
  val colour = White
}

private sealed trait Black {
  val colour = Black
}

private sealed trait King {
  def pathFor(move: Move): Option[Set[Square]] = ???
}

private sealed trait Queen {
  def pathFor(move: Move): Option[Set[Square]] = ???
}

private sealed trait Bishop {
  def pathFor(move: Move): Option[Set[Square]] = ???
}

private sealed trait Knight {
  def pathFor(move: Move): Option[Set[Square]] = ???
}

private sealed trait Rook {
  def pathFor(move: Move): Option[Set[Square]] = ???
}

private sealed trait Pawn {
  def pathFor(move: Move): Option[Set[Square]] = ???
}