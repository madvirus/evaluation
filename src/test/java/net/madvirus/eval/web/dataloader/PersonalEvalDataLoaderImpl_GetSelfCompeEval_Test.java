package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;
import net.madvirus.eval.api.personaleval.YouAreNotRateeException;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class PersonalEvalDataLoaderImpl_GetSelfCompeEval_Test extends AbstractPersonalEvalDataLoaderImplTest {
    @Test(expected = EvalSeasonNotFoundException.class)
    public void givenNoEvalSeason_and_NoPersonalEval_then_Should_Throw_ex() throws Exception {
        givenNoEvalSeason("EVAL2014");
        givenNoPersonalEval("EVAL2014", "bkchoi");
        dataLoader.getSelfCompeEvalDataForSelfEvalForm("EVAL2014", "bkchoi");
    }

    @Test(expected = YouAreNotRateeException.class)
    public void givenEvalSeason_and_NoPersonalEval_and_NoRatee_thenReturnEmpty() throws Exception {
        givenEvalSeasonButNotRatee("EVAL2014", "bkchoi");
        givenNoPersonalEval("EVAL2014", "bkchoi");
        dataLoader.getSelfCompeEvalDataForSelfEvalForm("EVAL2014", "bkchoi");
    }

    @Test
    public void givenEvalSeason_and_NoPersonalEval_and_Ratee_then_Should_ReturnData() throws Exception {
        givenEvalSeasonAndRatee("EVAL2014", "bkchoi");
        givenNoPersonalEval("EVAL2014", "bkchoi");

        CompeEvalData selfCompeEval = dataLoader.getSelfCompeEvalDataForSelfEvalForm("EVAL2014", "bkchoi");
        assertThat(selfCompeEval, notNullValue());
        assertThat(selfCompeEval.getEvalSet().isDone(), equalTo(false));
    }
}
