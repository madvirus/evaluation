package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;
import net.madvirus.eval.api.personaleval.YouAreNotRateeException;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class PersonalEvalDataLoaderImpl_GetSelfPerfEval_Test extends AbstractPersonalEvalDataLoaderImplTest {

    @Test(expected = EvalSeasonNotFoundException.class)
    public void givenNoEvalSeason_and_NoPersonalEval_then_Should_Throw_Ex() throws Exception {
        givenNoEvalSeason("EVAL2014");
        givenNoPersonalEval("EVAL2014", "bkchoi");
        dataLoader.getSelfPerfEvalDataForSelfEvalForm("EVAL2014", "bkchoi");
    }

    @Test(expected = YouAreNotRateeException.class)
    public void givenEvalSeason_and_NoPersonalEval_and_NoRatee_thenReturnEmpty() throws Exception {
        givenEvalSeasonButNotRatee("EVAL2014", "bkchoi");
        givenNoPersonalEval("EVAL2014", "bkchoi");

        dataLoader.getSelfPerfEvalDataForSelfEvalForm("EVAL2014", "bkchoi");
    }

    @Test
    public void givenEvalSeason_and_NoPersonalEval_and_Ratee_then_Should_ReturnData() throws Exception {
        givenEvalSeasonAndRatee("EVAL2014", "bkchoi");
        givenNoPersonalEval("EVAL2014", "bkchoi");

        SelfPerfEvalData selfPerfEval = dataLoader.getSelfPerfEvalDataForSelfEvalForm("EVAL2014", "bkchoi");
        assertThat(selfPerfEval, notNullValue());
        assertThat(selfPerfEval.isDone(), equalTo(false));
        assertThat(selfPerfEval.getItemAndEvals(), hasSize(0));
    }
}
