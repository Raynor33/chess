package chess

import org.scalatest._

class PieceSpec extends WordSpec with Matchers {

  "A WhitePiece" should {
    "be white" in {
      val whitePiece = new WhiteColoured {}
      whitePiece.colour should be (White)
    }
  }

  "A BlackPiece" should {
    "be black" in {
      val blackPiece = new BlackColoured {}
      blackPiece.colour should be (Black)
    }
  }

  "A King" should {
    "allow a flat move of one" in {

    }
    "allow a diagonal move of one" in {

    }
    "not allow a move if neither flat nor diagonal is possible" in {

    }
  }

  "A Queen" should {
    "allow a flat move of eight" in {

    }
    "allow a diagonal move of eight" in {

    }
    "not allow a move if neither flat nor diagonal is possible" in {

    }
  }

  "A Bishop" should {
    "allow a diagonal move of eight" in {

    }
    "not allow a move a diagonal is not possible" in {

    }
  }

  "A Knight" should {
    "allow a move two right and one up" in {

    }
    "allow a move two right and one down" in {

    }
    "allow a move two left and one up" in {

    }
    "allow a move two left and one down" in {

    }
    "allow a move two up and one left" in {

    }
    "allow a move two up and one right" in {

    }
    "allow a move two down and one left" in {

    }
    "allow a move two down and one right" in {

    }
    "not allow a move one diagonally" in {

    }
    "not allow a move two diagonally" in {

    }
    "not allow a move two flat" in {

    }
  }

  "A Rook" should {
    "allow a flat move of eight" in {

    }
    "not allow a move a flat is not possible" in {

    }
  }

  "A white pawn" should {
    "allow a move of one up if not taking" in {

    }
    "allow a move of two up if not taking and in the first row" in {

    }
    "not allow a move of one up if taking" in {

    }
    "not allow a move of two up if taking" in {

    }
    "not allow a move of one down" in {

    }
    "allow a move of one diagonally left if taking" in {

    }
    "allow a move of one diagonally right if taking" in {

    }
    "not allow a move of one diagonally left if not taking" in {

    }
    "not allow a move of one diagonally right if not taking" in {

    }
    "not allow a move of one diagonally left and down" in {

    }
    "not allow a move of one diagonally right and down" in {

    }
  }

  "A black pawn" should {
    "allow a move of one down if not taking" in {

    }
    "allow a move of two down if not taking and in the first row" in {

    }
    "not allow a move of one down if taking" in {

    }
    "not allow a move of two down if taking" in {

    }
    "not allow a move of one up" in {

    }
    "allow a move of one diagonally left if taking" in {

    }
    "allow a move of one diagonally right if taking" in {

    }
    "not allow a move of one diagonally left if not taking" in {

    }
    "not allow a move of one diagonally right if not taking" in {

    }
    "not allow a move of one diagonally left and up" in {

    }
    "not allow a move of one diagonally right and up" in {

    }
  }
}
