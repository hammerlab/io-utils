package org.hammerlab.lines.limit

import hammerlab.indent.spaces
import hammerlab.lines._
import hammerlab.show._

class LimitedTest
  extends hammerlab.Suite {

  def check(actual: Lines, expected: String): Unit = {
    ==(
      actual.showLines,
      expected.stripMargin
    )
  }

  def check(
    limit: Limit,
    expectedNoHeader: String,
    expectedYesHeader: String
  ): Unit = {
    implicit val _limit = limit
    check(noHeader, expectedNoHeader)
    check(yesHeader, expectedYesHeader)
  }

  val noHeader =
    Limited(
      1 to 5
    )

  def yesHeader(implicit limit: Limit) =
    Limited(
      1 to 5,
      "Elems:",
      show"First $limit elems:"
    )

  test("show 1") {
    check(
      1,
      """1
        |…""",
      """First 1 elems:
        |  1
        |  …"""
    )
  }

  test("show 3") {
    check(
      3,
      """1
        |2
        |3
        |…""",
      """First 3 elems:
        |  1
        |  2
        |  3
        |  …"""
    )
  }

  val full =
    """1
       |2
       |3
       |4
       |5"""

  val fullWithHeader =
    """Elems:
      |  1
      |  2
      |  3
      |  4
      |  5"""

  test("barely show full collection") {
    check(
      4,
      full,
      fullWithHeader
    )
  }

  test("show full collection") {
    check(
      5,
      full,
      fullWithHeader
    )

    check(
      10,
      full,
      fullWithHeader
    )

    check(
      Unlimited,
      full,
      fullWithHeader
    )
  }

  test("show none") {
    check(
      0,
      "",
      ""
    )
  }
}
