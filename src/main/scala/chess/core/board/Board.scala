package chess.core.board

import chess.core._

trait Board {
  def positions: Map[Square, Piece]
  def toMove: Option[Colour]
  def result: Option[Result]
  def neverMoved(square:Square) : Boolean
  def valid: Boolean
  def lastFrom: Option[Square]
  def lastTo: Option[Square]
}

