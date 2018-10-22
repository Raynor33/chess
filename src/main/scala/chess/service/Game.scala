package chess.service

import chess.domain.Board

case class Game(whitePlayerId: String, blackPlayerId: String, board: Board)
