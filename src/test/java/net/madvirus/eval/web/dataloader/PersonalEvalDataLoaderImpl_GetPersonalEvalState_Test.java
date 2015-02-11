package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;
import net.madvirus.eval.api.personaleval.YouAreNotRateeException;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class PersonalEvalDataLoaderImpl_GetPersonalEvalState_Test extends AbstractPersonalEvalDataLoaderImplTest {

    @Test(expected = EvalSeasonNotFoundException.class)
    public void givenNoEvalSeason_then_Should_Throw_Ex() throws Exception {
        givenNoEvalSeason("noEvalSeasonId");
        dataLoader.getPersonalEvalStateOf("noEvalSeasonId", "userid");
    }

    @Test(expected = YouAreNotRateeException.class)
    public void givenEvalSeason_but_NotRatee_then_Should_Throw_Ex() throws Exception {
        givenEvalSeasonButNotRatee("evalSeasonId", "noRatee");
        dataLoader.getPersonalEvalStateOf("evalSeasonId", "noRatee");
    }

    @Test
    public void givenNoPersonalEval_then_Should_Return_NotYetStarted_EvalState() throws Exception {
        givenEvalSeasonAndRatee("evalSeasonId", "userid");
        givenNoPersonalEval("evalSeasonId", "userid");

        PersonalEvalState state = dataLoader.getPersonalEvalStateOf("evalSeasonId", "userid");
        assertThat(state.isStarted(), equalTo(false));
    }

    @Test
    public void givenPersonalEval_then_Should_Return_StartedState() throws Exception {
        givenEvalSeasonAndRatee("evalSeasonId", "userid");
        givenPersonalEval("evalSeasonId", "userid");
        PersonalEvalState state = dataLoader.getPersonalEvalStateOf("evalSeasonId", "userid");
        assertThat(state, notNullValue());
        assertThat(state.isStarted(), equalTo(true));
    }

}
