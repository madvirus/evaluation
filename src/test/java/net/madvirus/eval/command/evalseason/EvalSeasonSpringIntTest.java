package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.testhelper.ESIntTestSetup;
import net.madvirus.eval.api.evalseaon.CreateEvalSeasonCommand;
import net.madvirus.eval.query.evalseason.EvalSeasonModel;
import net.madvirus.eval.query.evalseason.EvalSeasonModelRepositoryImpl;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.repository.Repository;
import org.axonframework.unitofwork.DefaultUnitOfWork;
import org.axonframework.unitofwork.UnitOfWork;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import scala.Option;

import javax.annotation.Resource;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ESIntTestSetup
public class EvalSeasonSpringIntTest {
    @Resource(name="evalSeasonRepository")
    private Repository<EvalSeason> evalSeasonRepository;

    @Autowired
    private EvalSeasonModelRepositoryImpl queryModelRepository;

    @Autowired
    private CommandGateway gateway;

    @Test
    public void creationCommandIntTest() throws Exception {
        String id = "ID" + Double.toHexString(Math.random());
        gateway.sendAndWait(new CreateEvalSeasonCommand(id, "이름"));

        runInUOW(() -> {
            EvalSeason evalSeason = evalSeasonRepository.load(id);
            assertThat(evalSeason, notNullValue());
        });

        Option<EvalSeasonModel> modelOption = queryModelRepository.findById(id);
        assertThat(modelOption.nonEmpty(), equalTo(true));
    }

    public void runInUOW(Runnable runnable) throws Exception {
        UnitOfWork uow = DefaultUnitOfWork.startAndGet();
        try {
            runnable.run();
            uow.commit();
        } catch(Exception ex) {
            uow.rollback(ex);
            throw ex;
        }
    }
}
