package chess

trait Board {
  this: BoardState =>
  def validMove(move: Move): Boolean = ???
  def checkmate: Boolean = ???
  def check(colour: Colour): Boolean = ???
}

sealed trait BoardState {
  def pieceAt(square: Square): Piece
  def currentPositions(predicate: (Piece) => Boolean): Map[Square, Piece]
  def toMove: Colour
}

case object InitialBoard extends BoardState with Board {
  override def pieceAt(square: Square): Piece = ???

  override def currentPositions(predicate: (Piece) => Boolean): Map[Square, Piece] = ???

  override def toMove: Colour = ???
}

case class SuccessorBoard(board: Board, move: Move) extends BoardState with Board {
  override def pieceAt(square: Square): Piece = ???

  override def currentPositions(predicate: (Piece) => Boolean): Map[Square, Piece] = ???

  override def toMove: Colour = ???
}
