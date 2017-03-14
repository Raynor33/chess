package chess.core

import org.scalatest._

import scala.{None, Some}

class SquareSpec extends WordSpec with Matchers {
  "A square" should {
    "not be allowed x > 7" in {
      an [IllegalArgumentException] should be thrownBy Square(8, 0)
    }
    "not be allowed y > 7" in {
      an [IllegalArgumentException] should be thrownBy Square(0, 8)
    }
    "not be allowed x < 0" in {
      an [IllegalArgumentException] should be thrownBy Square(-1, 0)
    }
    "be allowed 7, 7" in {
      Square(7, 7)
    }
    "be allowed 0, 0" in {
      Square(0, 0)
    }
    "not be allowed y < 0" in {
      an [IllegalArgumentException] should be thrownBy Square(0, -1)
    }
    "not give a flat line to itself" in {
      Square(1, 2).flatLineTo(Square(1, 2), 1) should be (None)
    }
    "not give a flat line to a non-flat square" in {
      Square(1, 2).flatLineTo(Square(2, 3), 1) should be (None)
    }
    "not give a flat line to a square that's too far away" in {
      Square(1, 2).flatLineTo(Square(1, 4), 1) should be (None)
    }
    "give a flat line to a square that's up and in range" in {
      Square(3, 4).flatLineTo(Square(3, 6), 2) should be (Some(Set(Square(3, 5))))
    }
    "give a flat line to a square that's right and in range" in {
      Square(3, 3).flatLineTo(Square(6, 3), 4) should be (Some(Set(Square(4, 3), Square(5, 3))))
    }
    "give a flat line to a square that's down and in range" in {
      Square(3, 3).flatLineTo(Square(3, 2), 2) should be (Some(Set.empty[Square]))
    }
    "give a flat line to a square that's left and in range" in {
      Square(3, 3).flatLineTo(Square(1, 3), 2) should be (Some(Set(Square(2, 3))))
    }
    "not give a diagonal line to itself" in {
      Square(1, 2).diagonalLineTo(Square(1, 2), 1) should be (None)
    }
    "not give a diagonal line to a non-diagonal square" in {
      Square(1, 2).diagonalLineTo(Square(2, 2), 1) should be (None)
    }
    "not give a diagonal line to a square that's too far away" in {
      Square(1, 2).diagonalLineTo(Square(3, 4), 1) should be (None)
    }
    "give a diagonal line to a square that's up-right in range" in {
      Square(3, 3).diagonalLineTo(Square(5, 5), 3) should be (Some(Set(Square(4, 4))))
    }
    "give a diagonal line to a square that's down-right in range" in {
      Square(3, 3).diagonalLineTo(Square(6, 0), 3) should be (Some(Set(Square(4, 2), Square(5, 1))))
    }
    "give a diagonal line to a square that's down-left in range" in {
      Square(3, 3).diagonalLineTo(Square(2, 2), 3) should be (Some(Set.empty[Square]))
    }
    "give a diagonal line to a square that's up-left in range" in {
      Square(3, 3).diagonalLineTo(Square(1, 5), 3) should be (Some(Set(Square(2, 4))))
    }
  }
}
