package chess.core.board

import chess.core.{Checkmate, Colour, Square}

trait MoveBoard extends Board {
  def previousBoard: Board

  def from: Square

  def to: Square

  def moveLegal: Boolean

  protected def legalAndNotCheck = moveLegal &&
    !check(previousBoard.toMove)

  def valid = legalAndNotCheck &&
    previousBoard.valid &&
    previousBoard.result.isEmpty

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
      Square.allSquares.exists(t =>
        StandardBoard(f, t, this).legalAndNotCheck ||
        EnPassantBoard(f, t, this).legalAndNotCheck
      )
    )
  }) Some(Checkmate(toMove)) else None
}
