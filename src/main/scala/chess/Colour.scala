package chess

sealed trait Colour {
  def moveDirection: Int
  def pawnRow: Int
}

case object White extends Colour {
  val moveDirection = 1
  val pawnRow = 1
}

case object Black extends Colour {
  val moveDirection = -1
  val pawnRow = 6
}
