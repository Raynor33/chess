package chess.rest.data

import chess.domain.{Piece, Square}
import chess.domain.Square

case class PositionData(square: Square, piece: Piece)
