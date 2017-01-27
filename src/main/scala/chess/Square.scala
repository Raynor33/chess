package chess

case class Square(x: Int, y: Int) {
  if (x > 7 || y > 7 || x < 0 || y < 0) throw new IllegalArgumentException

  def flatLineTo(square: Square, maxSquares: Int): Option[Set[Square]] = {
    lineTo(square, maxSquares, (xDiff, yDiff) => xDiff == 0 ^ yDiff == 0)
  }
  def diagonalLineTo(square: Square, maxSquares: Int): Option[Set[Square]] = {
    lineTo(square, maxSquares, (xDiff, yDiff) => Math.abs(xDiff) == Math.abs(yDiff) && xDiff != 0)
  }

  private def lineTo(square: Square, maxSquares: Int, validator: (Int, Int) => Boolean) = {
    if (!validator(square.x - x, square.y - y) || Math.abs(square.x - x).max(Math.abs(square.y - y)) > maxSquares) {
      None
    }
    else {
      def intsBetween = (start: Int, finish:Int) => if (start == finish) List.empty[Int] else {
        val signum: Int = Integer.signum(finish - start)
        List.range(start + signum, finish, signum)
      }
      Some(intsBetween(x, square.x).zipAll(intsBetween(y, square.y), y, x).map((t) => Square(t._1, t._2)).toSet)
    }
  }
}