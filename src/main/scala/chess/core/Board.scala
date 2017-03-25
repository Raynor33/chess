package chess.core

trait Board {
  def currentPositions: Map[Square, Piece]
  def toMove: Colour
  def checkmate: Boolean
  def check(colour: Colour): Boolean
  def hasNeverMoved(square:Square) : Boolean
  def valid: Boolean
}

trait Move extends Board {
  def previous: Board

  def from: Square

  def to: Square

  def moveLegal: Boolean

  def valid = moveLegal && !check(previous.toMove) && previous.valid

  def hasNeverMoved(square: Square) =
    previous.hasNeverMoved(square) && square != from

  def toMove = previous.toMove.opposite

  def check(colour: Colour) = {
    val allPositions = currentPositions
    val theKingSquare = allPositions.find(t => t._2.isKing && t._2.colour == colour)
      .map(_._1)
      .getOrElse(throw new IllegalStateException)
    allPositions.filter(_._2.colour == colour.opposite)
      .exists(t =>
        t._2.pathFor(t._1, theKingSquare, true).exists(path =>
          currentPositions.keySet.intersect(path).isEmpty
        )
      )
  }

  def checkmate = check(toMove) && {
    val allPositions = currentPositions
    !allPositions.filter(_._2.colour == toMove).keys.exists(f =>
      Square.allSquares.exists(t => StandardMove(f, t, this).valid || EnPassantMove(f, t, this).valid)
    )
  }
}

case class StandardMove(from: Square, to: Square, previous: Board) extends Move {
  override def currentPositions = {
    val previousPositions = previous.currentPositions
    // TODO How should this method behave if the move is invalid? Currently fails.
    previousPositions + (to -> previousPositions(from)) - from
  }

  override def moveLegal = {
    val previousPositions = previous.currentPositions
    previousPositions.get(from).exists(piece =>
      piece.colour == previous.toMove &&
      piece.pathFor(from, to, previousPositions.contains(to)).exists(path =>
        previousPositions.keySet.intersect(path).isEmpty) &&
      previousPositions.get(to).forall(taking =>
        taking.colour != previous.toMove)
    )
  }
}

case class CastlingMove(from: Square, to: Square, previous: Board) extends Move {
  private val castleFrom = if (to.x > from.x) Square(7, from.y) else Square(0, from.y)
  private val castleTo = if (to.x > from.x) Square(5, from.y) else Square(3, from.y)

  override def currentPositions = {
    val previousPositions = previous.currentPositions
    (previousPositions
      + (to -> previousPositions(from))
      - from
      + (castleTo -> previousPositions(castleFrom))
      - castleFrom)
  }

  override def moveLegal = {
    val moveSignum = Integer.signum(to.x - from.x)
    val previousPositions = previous.currentPositions
    previous.hasNeverMoved(from) &&
      previous.hasNeverMoved(castleFrom) &&
      previousPositions.get(from).exists(p =>
        p.isKing && p.colour == previous.toMove) &&
      Math.abs(to.x - from.x) == 2 &&
      to.y == from.y &&
      !previous.check(previous.toMove) &&
      !StandardMove(from, Square(from.x + moveSignum, from.y), previous).check(previous.toMove) &&
      (from.x + moveSignum until castleFrom.x).forall(x =>
        !previousPositions.contains(Square(x, from.y))
      )
  }

}

case class EnPassantMove(from: Square, to: Square, previous: Board) extends Move {
  override def currentPositions = {
    val previousPositions = previous.currentPositions
    (previousPositions
      + (to -> previousPositions(from))
      - from
      - Square(to.x, from.y))
  }

  override def moveLegal = {
    val previousPositions = previous.currentPositions
    previous match {
      case move: Move =>
        previousPositions.get(Square(to.x, from.y)).exists(_ match {
          case pawn: Pawn =>
            move.from.y == pawn.colour.pawnRow &&
            Math.abs(move.to.y - move.from.y) == 2
          case _ => false
        }) &&
        previousPositions.get(from).exists(_ match {
          case pawn: Pawn => pawn.colour == previous.toMove
          case _ => false
        })
      case _ => false
    }
  }
}

case class PawnPromotionMove(from: Square, to: Square, promotion: Piece, previous: Board) extends Move {
  override def currentPositions = previous.currentPositions - from + (to -> promotion)

  override def moveLegal = previous.currentPositions.get(from).exists(_ match {
    case p: Pawn => StandardMove(from, to, previous).moveLegal
    case _ => false
  }) && (promotion match {
    case p: Pawn => false
    case k: King => false
    case _ => promotion.colour == previous.toMove
  })
}

case object Nil extends Board {
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

  override def hasNeverMoved(square:Square) = true
  override val currentPositions = pieces
  override val toMove = White
  override val checkmate = false
  override def check(colour: Colour) = false
  override val valid = true
}
