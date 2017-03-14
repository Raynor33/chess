package chess.core

sealed trait Colour {
  def moveDirection: Int
  def pawnRow: Int
  def opposite: Colour
}

case object White extends Colour {
  val moveDirection = 1
  val pawnRow = 1
  val opposite = Black
}

case object Black extends Colour {
  val moveDirection = -1
  val pawnRow = 6
  val opposite = White
}
