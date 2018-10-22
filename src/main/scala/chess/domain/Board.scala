package chess.domain

trait Board {
  def positions: Map[Square, Piece]
  def toMove: Option[Colour]
  def result: Option[Result]
  def neverMoved(square:Square) : Boolean
  def valid: Boolean
  def lastFrom: Option[Square]
  def lastTo: Option[Square]
  def standardMove(from: Square, to: Square): Board = StandardBoard(from, to, this)
  def castlingMove(from: Square, to: Square): Board = CastlingBoard(from, to, this)
  def enPassantMove(from: Square, to: Square): Board = EnPassantBoard(from, to, this)
  def pawnPromotionMove(from: Square, to: Square, piece: Piece): Board = PawnPromotionBoard(from, to, piece, this)
}

object Board {
  def setup: Board = NilBoard
}