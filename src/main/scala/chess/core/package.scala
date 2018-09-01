package chess

import play.api.libs.json.{Json, _}

package object core {

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

  implicit val squareFormat = Json.format[Square]
  implicit val gameFormat = Json.format[Game]
  implicit val ColourFormat: OFormat[Colour] = julienrf.json.derived.flat.oformat((__ \ "type").format[String])
}
