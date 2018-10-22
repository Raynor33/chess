package chess.rest.response

import chess.domain.{Bishop, Black, Checkmate, Colour, King, Knight, Pawn, Piece, Queen, Result, Rook, Square, White}
import chess.service.Game
import play.api.libs.json._

case class GameResponse(id: String, whitePlayerId: String, blackPlayerId: String, toMove: Option[Colour],
                        result: Option[Result], positions: Map[Square, Piece])

object GameResponse {

  implicit val jsonWrites = new Writes[GameResponse] {
    override def writes(game: GameResponse): JsValue = Json.obj(
      "id" -> game.id,
      "whitePlayerId" -> game.whitePlayerId,
      "blackPlayerId" -> game.blackPlayerId,
      "toMove" -> (game.toMove match {
        case Some(White) => "white"
        case Some(Black) => "black"
        case _ => null
      }),
      "result" -> (game.result match {
        case Some(res) => Json.obj(
          "type" -> (res match {
            case Checkmate(_) => "checkmate"
          }),
          "winner" -> res.winner
        )
        case _ => JsNull
      }),
      "positions" -> JsObject(game.positions.map({
        case (square, piece) => algebraicNotation(square) -> pieceNotation(piece)
      }))
    )
    private def algebraicNotation(square: Square) = ('a' + square.x).asInstanceOf[Char].toString + (square.y + 1)
    private def pieceNotation(piece: Piece) = JsString((piece.colour match {
      case White => "w"
      case Black => "b"
    }) + (piece match {
      case _: King => "K"
      case _: Queen => "Q"
      case _: Rook => "R"
      case _: Bishop => "B"
      case _: Knight => "N"
      case _: Pawn => "P"
    }))
  }

  def apply(id: String, game: Game): GameResponse = new GameResponse(
    id, game.whitePlayerId, game.blackPlayerId, game.board.toMove,
    game.board.result, game.board.positions)
}