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
    def moveType[T <: Product](t: T) = Json.obj("moveType" -> JsString(t.productPrefix))
    def writes(board: Board) = board match {
      case Nil => JsNull
      case s: StandardMove => moveType(s) ++ Json.format[StandardMove].writes(s)
      case c: CastlingMove => moveType(c) ++ Json.format[CastlingMove].writes(c)
      case e: EnPassantMove => moveType(e) ++ Json.format[EnPassantMove].writes(e)
      case p: PawnPromotionMove => moveType(p) ++ Json.format[PawnPromotionMove].writes(p)
    }
    def reads(json: JsValue) = {
      json match {
        case JsNull => JsSuccess(Nil)
        case o: JsObject => (o \ "moveType").toOption match {
            case Some(JsString("StandardMove")) => Json.format[StandardMove].reads(json)
            case Some(JsString("CastlingMove")) => Json.format[CastlingMove].reads(json)
            case Some(JsString("EnPassantMove")) => Json.format[EnPassantMove].reads(json)
            case Some(JsString("PawnPromotionMove")) => Json.format[PawnPromotionMove].reads(json)
            case _ => JsError()
          }
        case _ => JsError()
      }
    }
  }

  implicit val squareFormat = Json.format[Square]
  implicit val gameFormat = Json.format[Game]

}
