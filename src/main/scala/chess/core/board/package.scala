package chess.core

import play.api.libs.json._

package object board {

  implicit object BoardReadsWrites extends Format[Board] {
    def moveType[T <: Product](t: T) = Json.obj("type" -> JsString(t.productPrefix))
    def writes(board: Board) = board match {
      case NilBoard => JsNull
      case s: StandardBoard => moveType(s) ++ Json.format[StandardBoard].writes(s)
      case c: CastlingBoard => moveType(c) ++ Json.format[CastlingBoard].writes(c)
      case e: EnPassantBoard => moveType(e) ++ Json.format[EnPassantBoard].writes(e)
      case p: PawnPromotionBoard => moveType(p) ++ Json.format[PawnPromotionBoard].writes(p)
    }
    def reads(json: JsValue) = {
      json match {
        case JsNull => JsSuccess(NilBoard)
        case o: JsObject => (o \ "type").toOption match {
          case Some(JsString("StandardBoard")) => Json.format[StandardBoard].reads(json)
          case Some(JsString("CastlingBoard")) => Json.format[CastlingBoard].reads(json)
          case Some(JsString("EnPassantBoard")) => Json.format[EnPassantBoard].reads(json)
          case Some(JsString("PawnPromotionBoard")) => Json.format[PawnPromotionBoard].reads(json)
          case _ => JsError()
        }
        case _ => JsError()
      }
    }
  }
}
