package chess.service

import chess.domain.{Board, CastlingBoard, EnPassantBoard, PawnPromotionBoard, Piece, Square, StandardBoard}

sealed trait MoveInstruction {
  def applyTo(game: Board) : Board
}

case class StandardMoveInstruction(from: Square, to: Square) extends MoveInstruction {
  override def applyTo(game: Board) = StandardBoard(from, to, game)
}
case class CastlingMoveInstruction(from: Square, to: Square) extends MoveInstruction {
  override def applyTo(game: Board) = CastlingBoard(from, to, game)
}
case class EnPassantMoveInstruction(from: Square, to: Square) extends MoveInstruction {
  override def applyTo(game: Board) = EnPassantBoard(from, to, game)
}
case class PawnPromotionMoveInstruction(from: Square, to: Square, piece: Piece) extends MoveInstruction {
  override def applyTo(game: Board) = PawnPromotionBoard(from, to, piece, game)
}