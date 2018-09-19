package chess.core.board

import chess.core._

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
  override val valid = true
}
