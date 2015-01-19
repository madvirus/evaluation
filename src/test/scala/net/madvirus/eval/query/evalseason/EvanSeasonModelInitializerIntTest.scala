package net.madvirus.eval.query.evalseason

import com.github.springtestdbunit.annotation.DatabaseSetup
import net.madvirus.eval.testhelper.ESIntTestSetup
import org.hamcrest.Matchers._
import org.junit.Assert._
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@ESIntTestSetup
@DatabaseSetup(value = Array("/EventTestDataEvalSeason.xml"))
@RunWith(classOf[SpringJUnit4ClassRunner])
class EvanSeasonModelInitializerIntTest {

  @Autowired
  var repository: EvalSeasonModelRepository = null

  @Test
  def replay(): Unit = {
    val evalSeasonModel = repository.findById("EVAL-001")
    assertThat(evalSeasonModel.nonEmpty, equalTo(true))
  }

}
