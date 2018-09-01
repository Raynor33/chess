package chess.core.board

import chess.core._

trait Board {
  def currentPositions: Map[Square, Piece]
  def toMove: Colour
  def result: Option[Result]
  protected def check(colour: Colour): Boolean
  def hasNeverMoved(square:Square) : Boolean
  def valid: Boolean
}

