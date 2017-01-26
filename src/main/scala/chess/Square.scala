package chess

case class Square(x: Int, y: Int) {

  def flatLineTo(square: Square, maxSquares: Int): Option[Set[Square]] = ???
  def diagonalLineTo(square: Square, maxSquares: Int): Option[Set[Square]] = ???
}
