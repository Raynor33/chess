package chess.core

trait Board {
  def currentPositions: Map[Square, Piece]
  def toMove: Colour
  def result: Option[Result]
  def check(colour: Colour): Boolean
  def hasNeverMoved(square:Square) : Boolean
  def valid: Boolean
}

trait MoveBoard extends Board {
  def previousBoard: Board

  def from: Square

  def to: Square

  def moveLegal: Boolean

  def valid = moveLegal && !check(previousBoard.toMove) && previousBoard.valid

  def hasNeverMoved(square: Square) =
    previousBoard.hasNeverMoved(square) && square != from

  def toMove = previousBoard.toMove.opposite

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

  def result = if (check(toMove) && {
    val allPositions = currentPositions
    !allPositions.filter(_._2.colour == toMove).keys.exists(f =>
      Square.allSquares.exists(t => StandardMoveBoard(f, t, this).valid || EnPassantMoveBoard(f, t, this).valid)
    )
  }) Some(Checkmate(toMove)) else None
}

case class StandardMoveBoard(from: Square, to: Square, previousBoard: Board) extends MoveBoard {
  override def currentPositions = {
    val previousPositions = previousBoard.currentPositions
    (previousPositions
      - from
      ++ previousPositions.get(from).map(p =>
        Seq(to -> p)
      ).getOrElse(Nil))
  }

  override def moveLegal = {
    val previousPositions = previousBoard.currentPositions
    previousPositions.get(from).exists(piece =>
      piece.colour == previousBoard.toMove &&
      piece.pathFor(from, to, previousPositions.contains(to)).exists(path =>
        previousPositions.keySet.intersect(path).isEmpty) &&
      previousPositions.get(to).forall(taking =>
        taking.colour != previousBoard.toMove)
    )
  }
}

case class CastlingMoveBoard(from: Square, to: Square, previousBoard: Board) extends MoveBoard {
  private val castleFrom = if (to.x > from.x) Square(7, from.y) else Square(0, from.y)
  private val castleTo = if (to.x > from.x) Square(5, from.y) else Square(3, from.y)

  override def currentPositions = {
    val previousPositions = previousBoard.currentPositions
    (previousPositions
      ++ previousPositions.get(from).map(p =>
        Seq(to -> p)
      ).getOrElse(Nil)
      - from
      + (castleTo -> previousPositions(castleFrom))
      - castleFrom)
  }

  override def moveLegal = {
    val moveSignum = Integer.signum(to.x - from.x)
    val previousPositions = previousBoard.currentPositions
    previousBoard.hasNeverMoved(from) &&
      previousBoard.hasNeverMoved(castleFrom) &&
      previousPositions.get(from).exists(p =>
        p.isKing && p.colour == previousBoard.toMove) &&
      Math.abs(to.x - from.x) == 2 &&
      to.y == from.y &&
      !previousBoard.check(previousBoard.toMove) &&
      !StandardMoveBoard(from, Square(from.x + moveSignum, from.y), previousBoard).check(previousBoard.toMove) &&
      (from.x + moveSignum until castleFrom.x).forall(x =>
        !previousPositions.contains(Square(x, from.y))
      )
  }

}

case class EnPassantMoveBoard(from: Square, to: Square, previousBoard: Board) extends MoveBoard {
  override def currentPositions = {
    val previousPositions = previousBoard.currentPositions
    (previousPositions
      ++ previousPositions.get(from).map(p =>
        Seq(to -> p)
      ).getOrElse(Nil)
      - from
      - Square(to.x, from.y))
  }

  override def moveLegal = {
    val previousPositions = previousBoard.currentPositions
    previousBoard match {
      case move: MoveBoard =>
        previousPositions.get(Square(to.x, from.y)).exists(_ match {
          case pawn: Pawn =>
            move.from.y == pawn.colour.pawnRow &&
            Math.abs(move.to.y - move.from.y) == 2
          case _ => false
        }) &&
        previousPositions.get(from).exists(_ match {
          case pawn: Pawn => pawn.colour == previousBoard.toMove &&
            pawn.pathFor(from, to, true).isDefined
          case _ => false
        })
      case _ => false
    }
  }
}

case class PawnPromotionMoveBoard(from: Square, to: Square, promotion: Piece, previousBoard: Board) extends MoveBoard {
  override def currentPositions = previousBoard.currentPositions - from + (to -> promotion)

  override def moveLegal = previousBoard.currentPositions.get(from).exists(_ match {
    case p: Pawn => StandardMoveBoard(from, to, previousBoard).moveLegal
    case _ => false
  }) && (promotion match {
    case p: Pawn => false
    case k: King => false
    case _ => promotion.colour == previousBoard.toMove
  })
}

case object NilBoard extends Board {
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
  override val result = None
  override def check(colour: Colour) = false
  override val valid = true
}
