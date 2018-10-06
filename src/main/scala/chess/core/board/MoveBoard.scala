package chess.core.board

import chess.core.{Checkmate, Colour, Piece, Result, Square}

trait MoveBoard extends Board {
  def previousBoard: Board

  def moveLegal: Boolean

  protected def legalAndNotCheck = moveLegal &&
    !previousBoard.toMove.exists(check)

  def valid = legalAndNotCheck

  def neverMoved(square: Square) =
    previousBoard.neverMoved(square) && !lastFrom.contains(square)

  def toMove = if (result.isDefined) None else previousBoard.toMove.map(_.opposite)

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

  def result = previousBoard.toMove.map(_.opposite).flatMap(nextMover => if (check(nextMover) && {
    val allPositions = positions
    val boardData = BoardData(positions, Some(nextMover), None, neverMoved, true, lastFrom, lastTo)
    !allPositions.filter(_._2.colour == nextMover).keys.exists(f =>
    Square.allSquares.exists(t => StandardBoard(f, t, boardData).legalAndNotCheck ||
          EnPassantBoard(f, t, boardData).legalAndNotCheck))
  }) Some(Checkmate(nextMover)) else None)

  private case class BoardData(positions: Map[Square, Piece], toMove: Option[Colour], result: Option[Result],
    neverMovedDef: Square => Boolean, valid: Boolean, lastFrom: Option[Square], lastTo: Option[Square]) extends Board {
    override def neverMoved(square: Square): Boolean = neverMovedDef(square)
  }

}
