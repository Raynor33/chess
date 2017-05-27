package chess.formats

import chess.core._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

object ChessFormats {
  import play.api.libs.json.Json

  implicit object PieceReadWrites extends Format[Piece] {
    private val pieceMap = Seq(
      WhiteKing, WhiteQueen, WhiteBishop, WhiteKnight, WhiteRook, WhitePawn,
      BlackKing, BlackQueen, BlackBishop, BlackKnight, BlackRook, BlackPawn
    ).map(p => JsString(p.productPrefix) -> p).toMap[JsValue, Piece]

    override def reads(json: JsValue) = {
      pieceMap.get(json) match {
        case Some(p) => JsSuccess(p)
        case _ => JsError()
      }
    }

    override def writes(piece: Piece) = {
      JsString(piece.asInstanceOf[Product].productPrefix)
    }
  }

  implicit object BoardReadsWrites extends Format[Board] {
    def moveType[T <: Product](t: T) = Json.obj("type" -> JsString(t.productPrefix))
    def writes(board: Board) = board match {
      case NilBoard => JsNull
      case s: StandardMoveBoard => moveType(s) ++ Json.format[StandardMoveBoard].writes(s)
      case c: CastlingMoveBoard => moveType(c) ++ Json.format[CastlingMoveBoard].writes(c)
      case e: EnPassantMoveBoard => moveType(e) ++ Json.format[EnPassantMoveBoard].writes(e)
      case p: PawnPromotionMoveBoard => moveType(p) ++ Json.format[PawnPromotionMoveBoard].writes(p)
    }
    def reads(json: JsValue) = {
      json match {
        case JsNull => JsSuccess(NilBoard)
        case o: JsObject => (o \ "type").toOption match {
            case Some(JsString("StandardMoveBoard")) => Json.format[StandardMoveBoard].reads(json)
            case Some(JsString("CastlingMoveBoard")) => Json.format[CastlingMoveBoard].reads(json)
            case Some(JsString("EnPassantMoveBoard")) => Json.format[EnPassantMoveBoard].reads(json)
            case Some(JsString("PawnPromotionMoveBoard")) => Json.format[PawnPromotionMoveBoard].reads(json)
            case _ => JsError()
          }
        case _ => JsError()
      }
    }
  }

  implicit val squareFormat = Json.format[Square]
  implicit val gameFormat = Json.format[Game]

}
