package net.madvirus.eval.query.evalseason

import net.madvirus.eval.api.RateeMapping
import net.madvirus.eval.api.evalseaon.RateeType
import org.axonframework.test.matchers.Matchers._
import org.junit.Assert._
import org.junit.{Before, Test}

class EvalSeasonModelTest {

  var model: EvalSeasonModel = _

  val ratee1: String = "ratee1"
  val ratee2: String = "ratee2"
  val ratee3: String = "ratee3"

  val first1: String = "first1"
  val first2: String = "first2"

  val second1: String = "second1"
  val second2: String = "second2"

  val colleague1: String = "colleague1"
  val colleague2: String = "colleague2"
  val colleague3: String = "colleague3"
  val colleague4: String = "colleague4"

  @Before
  def setup(): Unit = {
    model = new EvalSeasonModel("ID", "name")
      .updateMapping(new RateeMapping(ratee1, RateeType.MEMBER, first1, second1, colleague1))
      .updateMapping(new RateeMapping(ratee2, RateeType.LEADER, null, second1, colleague2))
      .updateMapping(new RateeMapping(ratee3, RateeType.LEADER, first1, second1, colleague3, colleague4))
  }

  private def assertContains(list: Set[String], contained:List[String]): Unit = {
    contained.foreach(x => assertTrue(s"list must contains $x", list contains x))
  }

  private def assertNotContains(list: Set[String], noContained:List[String]): Unit = {
    noContained.foreach(x => assertFalse(s"list don't have to contains $x", list contains x))
  }

  @Test
  def baseMapping {
    assertThat(model.getId, equalTo("ID"))
    assertThat(model.getName, equalTo("name"))
    assertThat(model.getOpened, equalTo(false))

    val mapping: Option[RateeMapping] = model.getRateeMappingOf(ratee1)
    assertThat(mapping.nonEmpty, equalTo(true))

    assertThat(model.getRateeMappingOf("noRatee").isEmpty, equalTo(true))

    val list: Set[String] = model.getRateesOfFirstRater(first1)
    assertContains(list, List(ratee3, ratee1))
    assertNotContains(list, List(ratee2))

    val rateeOfSecond = model.getRateesOfSecondRater(second1);
    assertContains(rateeOfSecond, List(ratee1, ratee2, ratee3))

    val rateeOfColleague1 = model.getRateesOfColleague(colleague1)
    assertContains(rateeOfColleague1, List(ratee1))
    assertNotContains(rateeOfColleague1, List(ratee2, ratee3))

    val rateeOfColleague2 = model.getRateesOfColleague(colleague2)
    assertContains(rateeOfColleague2, List(ratee2))
    assertNotContains(rateeOfColleague2, List(ratee1, ratee3))

    val rateeOfColleague3 = model.getRateesOfColleague(colleague3)
    assertContains(rateeOfColleague3, List(ratee3))
    assertNotContains(rateeOfColleague3, List(ratee1, ratee2))
  }

  @Test
  def changeFirstRaterFromOneToAnother {
    model = model.updateMapping(new RateeMapping(ratee3, RateeType.MEMBER, first2, second1, colleague3))

    val rateesOfFirst1 = model.getRateesOfFirstRater(first1)
    assertContains(rateesOfFirst1, List(ratee1))
    assertNotContains(rateesOfFirst1, List(ratee2, ratee3))

    val rateesOfFirst2 = model.getRateesOfFirstRater(first2)
    assertContains(rateesOfFirst2, List(ratee3))
  }

  @Test
  def changeFirstRaterFromNullToOther {
    model = model.updateMapping(new RateeMapping(ratee2, RateeType.MEMBER, first1, second1, colleague2));

    val rateesOfFirst = model.getRateesOfFirstRater(first1)
    assertContains(rateesOfFirst, List(ratee1, ratee2, ratee3))
  }

  @Test
  def changeFirstRaterFromOneToNull: Unit = {
    model = model.updateMapping(new RateeMapping(ratee1, RateeType.MEMBER, null, second1, colleague2));
    val rateesOfFirst = model.getRateesOfFirstRater(first1)
    assertContains(rateesOfFirst, List(ratee3))
    assertNotContains(rateesOfFirst, List(ratee1, ratee2))
  }

  @Test
  def changeSecondRaterFromOneToOther {
    model = model.updateMapping(new RateeMapping(ratee1, RateeType.MEMBER, first1, second2, colleague2));
    val rateesOfSecond1 = model.getRateesOfSecondRater(second1)
    assertContains(rateesOfSecond1, List(ratee2, ratee3))
    assertNotContains(rateesOfSecond1, List(ratee1))

    val rateesOfSecond2 = model.getRateesOfSecondRater(second2)
    assertContains(rateesOfSecond2, List(ratee1))
    assertNotContains(rateesOfSecond2, List(ratee2, ratee3))
  }

  @Test
  def addNewColleagueRater: Unit = {
    model = model.updateMapping(new RateeMapping(ratee1, RateeType.MEMBER, first1, second1, colleague1, colleague2))

    val rateeOfColleague2 = model.getRateesOfColleague(colleague2)
    assertContains(rateeOfColleague2, List(ratee1, ratee2))
    assertNotContains(rateeOfColleague2, List(ratee3))
  }

  @Test
  def removeExistingColleagues: Unit = {
    model = model.updateMapping(new RateeMapping(ratee3, RateeType.MEMBER, first1, second1, colleague3))

    val rateeOfColleague4 = model.getRateesOfColleague(colleague4)
    assertTrue(rateeOfColleague4.isEmpty)
  }

  @Test
  def replaceColleagues: Unit = {
    model = model.updateMapping(new RateeMapping(ratee3, RateeType.MEMBER, first1, second1, colleague1, colleague2))

    val rateeOfColleague1 = model.getRateesOfColleague(colleague1)
    assertContains(rateeOfColleague1, List(ratee1, ratee3))

    val rateeOfColleague2 = model.getRateesOfColleague(colleague2)
    assertContains(rateeOfColleague2, List(ratee2, ratee3))

    val rateeOfColleague4 = model.getRateesOfColleague(colleague4)
    assertTrue(rateeOfColleague4.isEmpty)

    val rateeOfColleague3 = model.getRateesOfColleague(colleague3)
    assertTrue(rateeOfColleague3.isEmpty)
  }

}
