package chess

import Game._

object Game {
  val MatchAll = (p: Piece) => true
  val MatchWhite = (p: Piece) => p.colour == White
  val MatchBlack = (p: Piece) => p.colour == Black
  val MatchWhiteKing = (p: Piece) => p == WhiteKing
  val MatchBlackKing = (p: Piece) => p == BlackKing
}

trait Game {
  def currentPositions(predicate: (Piece) => Boolean): Map[Square, Piece]
  def toMove: Colour
  def checkmate: Boolean
  def check(colour: Colour): Boolean
  def valid: Boolean
}

trait NonNilGame extends Game {
  def toMove = previous.toMove.opposite
  def check(colour: Colour) = {
    val allPositions = currentPositions(MatchAll)
    val kingPredicate = if (colour == Black) MatchBlackKing else MatchWhiteKing
    val kingSquare = allPositions.find(t => kingPredicate(t._2))
      .map(_._1).getOrElse(throw new IllegalStateException)
    allPositions.filter(_._2.colour == colour.opposite)
      .exists(t =>
        t._2.pathFor(t._1, kingSquare, true).map(path =>
          path.intersect(allPositions.keys.toSet).isEmpty
        ).getOrElse(false)
      )
  }
  def checkmate: Boolean = ???
  def previous: Game
}

case class StandardMove(from: Square, to: Square, previous: Game) extends NonNilGame {
  override def currentPositions(predicate: (Piece) => Boolean) = ???

  override def valid = ???
}

case class CastlingMove(from: Square, to: Square, previous: Game) extends NonNilGame {
  override def currentPositions(predicate: (Piece) => Boolean) = ???

  override def valid = ???
}

case class EnPassantMove(from: Square, to: Square, previous: Game) extends NonNilGame {
  override def currentPositions(predicate: (Piece) => Boolean) = ???

  override def valid = ???
}

case class PawnPromotionMove(from: Square, to: Square, promotion: Piece, previous: Game) extends NonNilGame {
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
