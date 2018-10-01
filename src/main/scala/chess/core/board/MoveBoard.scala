package chess.core.board

import chess.core.{Checkmate, Colour, Square}

trait MoveBoard extends Board {
  def previousBoard: Board

  def moveLegal: Boolean

  protected def legalAndNotCheck = moveLegal &&
    !check(previousBoard.toMove)

  def valid = legalAndNotCheck

  def neverMoved(square: Square) =
    previousBoard.neverMoved(square) && !lastFrom.contains(square)

  def toMove = previousBoard.toMove.opposite

  def check(colour: Colour) = {
    val allPositions = positions
    val theKingSquare = allPositions.find(t => t._2.isKing && t._2.colour == colour)
      .map(_._1)
      .getOrElse(throw new IllegalStateException)
    allPositions.filter(_._2.colour == colour.opposite)
      .exists(t =>
        t._2.pathFor(t._1, theKingSquare, true).exists(path =>
          positions.keySet.intersect(path).isEmpty
        )
      )
  }

  def result = if (check(toMove) && {
    val allPositions = positions
    !allPositions.filter(_._2.colour == toMove).keys.exists(f =>
      Square.allSquares.exists(t =>
        StandardBoard(f, t, this).legalAndNotCheck ||
        EnPassantBoard(f, t, this).legalAndNotCheck
      )
    )
  }) Some(Checkmate(toMove)) else None
}
