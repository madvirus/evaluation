package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.api.evalseaon.CreateEvalSeasonCommand;
import net.madvirus.eval.api.evalseaon.OpenEvaluationCommand;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModel;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModelRepository;
import net.madvirus.eval.testhelper.ESIntTestSetup;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.repository.Repository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import scala.Option;

import javax.annotation.Resource;

import static net.madvirus.eval.axon.AxonUtil.runInUOW;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ESIntTestSetup
public class EvalSeasonSpringIntTest {

    @Resource(name = "evalSeasonRepository")
    private Repository<EvalSeason> evalSeasonRepository;

    @Autowired
    private EvalSeasonMappingModelRepository queryModelRepository;

    @Autowired
    private CommandGateway gateway;

    @Test
    public void creationCommandIntTest() throws Exception {
        String id = "EVAL-2013";
        gateway.sendAndWait(new CreateEvalSeasonCommand(id, "이름"));

        Runnable runnable = () -> {
            EvalSeason evalSeason = evalSeasonRepository.load(id);
            assertThat(evalSeason, notNullValue());
        };
        runInUOW(runnable);

        Option<EvalSeasonMappingModel> modelOption = queryModelRepository.findById(id);
        assertThat(modelOption.nonEmpty(), equalTo(true));
    }

    @Test
    public void openCommand() throws Exception {
        gateway.sendAndWait(new OpenEvaluationCommand("EVAL-001"));
        Runnable runnable = () -> {
            EvalSeason evalSeason = evalSeasonRepository.load("EVAL-001");
            assertThat(evalSeason.isOpened(), equalTo(true));
        };
        runInUOW(runnable);

    }

}
