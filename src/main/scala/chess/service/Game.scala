package chess.service

import chess.domain.{Board, NilBoard}

case class Game(whitePlayerId: String, blackPlayerId: String, board: Board = NilBoard)
