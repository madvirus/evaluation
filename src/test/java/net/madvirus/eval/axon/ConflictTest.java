package net.madvirus.eval.axon;

import net.madvirus.eval.domain.personaleval.PersonalEval;
import net.madvirus.eval.springconfig.Constants;
import net.madvirus.eval.testhelper.AbstractRunReplayTest;
import net.madvirus.eval.testhelper.CommandHelper;
import org.axonframework.repository.Repository;
import org.axonframework.unitofwork.TransactionManager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.jdbc.Sql;

import static net.madvirus.eval.axon.AxonUtil.runInUOW;

@Sql("classpath:db-test-without-ad.sql")
@Sql("classpath:db-test-personal-eval.sql")
public class ConflictTest extends AbstractRunReplayTest {
    // 한 개의 PersonalEval이 필요하고,
    // 한 버전의 PersonalEval에 대해 동시에 상태 변경 요청을 날려서,
    // 어떤 에러가 발생하는지 확인하고,
    // ConflictResolver가 이를 어떻게 해결해주는지 확인해야 함

    @Autowired
    @Qualifier(Constants.PERSONAL_EVAL_REPO_QUALIFIER)
    private Repository<PersonalEval> personalEvalRepository;

    @Autowired
    private TransactionManager tx;

    @Test
    public void testName() throws Exception {
        Thread t1 = new Thread(() -> {
            runInUOW(tx, () -> {
                System.out.println("------------------- 1. 평가 객체 로딩: ");
                PersonalEval eval = personalEvalRepository.load("EVAL2014-ratee11");
                System.out.println("------------------- 1. 평가 객체 로딩됨: "+eval.getVersion());
                sleep(1000);
                System.out.println("------------------- 1. 본인 평가 실행: "+ eval.getVersion());
                eval.getSelfRaterOperator().updateSelfCompetencyEvaluation(
                        CommandHelper.updateSelfCompeEvalCommand("EVAL2014", "ratee11", false, false, false));
                sleep(1000);
                System.out.println("------------------- 1. 본인 평가 완료: "+ eval.getVersion());
            });
        });
        Thread t2 = new Thread(() -> {
            runInUOW(tx, () -> {
                System.out.println("------------------- 2. 평가 객체 로딩: ");
                PersonalEval eval = personalEvalRepository.load("EVAL2014-ratee11");
                System.out.println("------------------- 2. 평가 객체 로딩됨: "+eval.getVersion());

                sleep(1000);
                System.out.println("------------------- 2. 동료 평가 실행: "+ eval.getVersion());
                eval.getColleagueRaterOperator().updateColleagueCompetencyEval(
                        CommandHelper.updateColleagueCompeEvalCommand("EVAL2014", "ratee11", "ratee12", false, false, false)
                );
                sleep(1000);
                System.out.println("------------------- 2. 동료 평가 완료: "+ eval.getVersion());
            });
        });
        t1.start();
        t2.start();

        sleep(5000);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
