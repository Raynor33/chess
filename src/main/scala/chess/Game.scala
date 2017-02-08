package chess

sealed trait Game {
  def currentPositions(predicate: (Piece) => Boolean): Map[Square, Piece]
  def toMove: Colour
  def checkmate: Boolean
  def check(colour: Colour): Boolean
  def valid: Boolean
}

trait NonNilGame {
  def game: Game
  def toMove = ???
  def check(colour: Colour): Boolean = ???
  def checkmate: Boolean = ???
}

case class StandardMove(from: Square, to: Square, game: Game) extends Game with NonNilGame {
  override def currentPositions(predicate: (Piece) => Boolean) = ???

  override def valid = ???
}

case class CastlingMove(from: Square, to: Square, game: Game) extends Game with NonNilGame {
  override def currentPositions(predicate: (Piece) => Boolean) = ???

  override def valid = ???
}

case class EnPassantMove(from: Square, to: Square, game: Game) extends Game with NonNilGame {
  override def currentPositions(predicate: (Piece) => Boolean) = ???

  override def valid = ???
}

case class PawnPromotionMove(from: Square, to: Square, promotion: Piece, game: Game) extends Game with NonNilGame {
  override def currentPositions(predicate: (Piece) => Boolean) = ???

  override def valid = ???
}

case object Nil extends Game {
  private val pieces = (0 to 7).map(x => Square(x, 1) -> WhitePawn).toMap ++
    (0 to 7).map(x => Square(x, 6) -> BlackPawn).toMap +
    (Square(0,0) -> WhiteRook) + (Square(7,0) -> WhiteRook) +
    (Square(1,0) -> WhiteKnight) + (Square(6,0) -> WhiteKnight) +
    (Square(2,0) -> WhiteBishop) + (Square(5,0) -> WhiteBishop) +
    (Square(3,0) -> WhiteQueen) + (Square(4,0) -> WhiteKing) +
    (Square(0,7) -> BlackRook) + (Square(7,7) -> BlackRook) +
    (Square(1,7) -> BlackKnight) + (Square(6,7) -> BlackKnight) +
    (Square(2,7) -> BlackBishop) + (Square(5,7) -> BlackBishop) +
    (Square(3,7) -> BlackQueen) + (Square(4,7) -> BlackKing)

  override def currentPositions(predicate: (Piece) => Boolean) = pieces.filter(t => predicate(t._2))
  override def toMove = White
  override def checkmate = false
  override def check(colour: Colour) = false
  override def valid = true
}