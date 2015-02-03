package net.madvirus.eval.query.evalseason

import net.madvirus.eval.testhelper.AbstractIntTest
import org.hamcrest.Matchers._
import org.junit.Assert._
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class EvanSeasonMappingModelInitializerIntTest extends AbstractIntTest {

  @Autowired
  val initializer: EvanSeasonMappingModelInitializer = null
  @Autowired
  var repository: EvalSeasonMappingModelRepository = null

  @Test
  def replay(): Unit = {
    initializer.replay()
    val evalSeasonModel = repository.findById("EVAL-001")
    assertThat(evalSeasonModel.nonEmpty, equalTo(true))
  }

}
