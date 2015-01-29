package net.madvirus.eval.query.evalseason

import net.madvirus.eval.testhelper.ESIntTestSetup
import org.hamcrest.Matchers._
import org.junit.Assert._
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@ESIntTestSetup
@RunWith(classOf[SpringJUnit4ClassRunner])
class EvanSeasonMappingModelInitializerIntTest {

  @Autowired
  var repository: EvalSeasonMappingModelRepository = null

  @Test
  def replay(): Unit = {
    val evalSeasonModel = repository.findById("EVAL-001")
    assertThat(evalSeasonModel.nonEmpty, equalTo(true))
  }

}
