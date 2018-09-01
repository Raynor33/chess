package chess.core

import chess.core.board.{Board, NilBoard}

case class Game(whitePlayerId: String, blackPlayerId: String, board: Board = NilBoard)
