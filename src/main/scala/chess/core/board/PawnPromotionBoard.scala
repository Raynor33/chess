package chess.core.board

import chess.core.{King, Pawn, Piece, Square}

case class PawnPromotionBoard(from: Square, to: Square, promotion: Piece, previousBoard: Board) extends MoveBoard {

  override def fromOption: Option[Square] = Some(from)

  override def toOption: Option[Square] = Some(to)

  override def currentPositions = previousBoard.currentPositions - from + (to -> promotion)

  override def moveLegal = previousBoard.currentPositions.get(from).exists(_ match {
    case p: Pawn => StandardBoard(from, to, previousBoard).moveLegal
    case _ => false
  }) && (promotion match {
    case p: Pawn => false
    case k: King => false
    case _ => promotion.colour == previousBoard.toMove
  })
}
