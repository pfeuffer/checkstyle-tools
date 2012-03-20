package de.pfeufferweb.tools.checkstyle
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class CheckstyleSumTest extends FlatSpec with ShouldMatchers {

  "A ByFileCounter" should "be empty for empty results" in {
    val counter = new ByFileCounter
    counter.counts.isEmpty should equal(true)
  }
}