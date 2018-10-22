package chess

import play.api.libs.json.Json

package object service {
  implicit val gameFormat = Json.format[Game]
}
