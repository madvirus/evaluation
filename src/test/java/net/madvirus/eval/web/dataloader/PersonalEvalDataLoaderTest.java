package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.command.evalseason.EvalSeason;
import net.madvirus.eval.command.personaleval.PersonalEval;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModel;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static net.madvirus.eval.command.personaleval.PersonalEval.createId;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class PersonalEvalDataLoaderTest {

    private PersonalEvalDataLoaderImpl dataLoader;
    private Repository<PersonalEval> mockPersonalEvalRepository;
    private EvalSeasonDataLoader mockEvalSeasonDataLoader;

    @Before
    public void setUp() throws Exception {
        mockPersonalEvalRepository = mock(Repository.class);
        mockEvalSeasonDataLoader = mock(EvalSeasonDataLoader.class);
        dataLoader = new PersonalEvalDataLoaderImpl(mockPersonalEvalRepository, mockEvalSeasonDataLoader);
    }

    @Test
    public void givenNoEvalSeason_thenReturnEmpty() throws Exception {
        givenNoEvalSeason("noEvalSeasonId");

        Optional<PersonalEvalState> evalState = dataLoader.getPersonalEvalStateOf("noEvalSeasonId", "userid");
        assertThat(evalState.isPresent(), equalTo(false));
    }

    private void givenNoEvalSeason(String noEvalSeasonId) {
        when(mockEvalSeasonDataLoader.load(noEvalSeasonId)).thenReturn(Optional.<EvalSeasonData>empty());
    }

    @Test
    public void givenEvalSeason_but_NotRatee_thenReturnEmpty() throws Exception {
        givenEvalSeasonButNotRatee("evalSeasonId", "noRatee");

        Optional<PersonalEvalState> state = dataLoader.getPersonalEvalStateOf("evalSeasonId", "noRatee");
        assertThat(state.isPresent(), equalTo(false));
    }

    private void givenEvalSeasonButNotRatee(String evalSeasonId, String userId) {
        EvalSeasonMappingModel mappingModel = mock(EvalSeasonMappingModel.class);
        when(mappingModel.containsRatee(userId)).thenReturn(false);
        EvalSeason evalSeason = mock(EvalSeason.class);
        EvalSeasonData seasonData = new EvalSeasonData(evalSeason, mappingModel);

        when(mockEvalSeasonDataLoader.load(evalSeasonId)).thenReturn(
                Optional.of(seasonData));
    }

    @Test
    public void givenNoPersonalEval_thenReturn_NotYetStarted_EvalState() throws Exception {
        givenEvalSeasonAndRatee("evalSeasonId", "userid");
        givenNoPersonalEval("evalSeasonId", "userid");

        Optional<PersonalEvalState> state = dataLoader.getPersonalEvalStateOf("evalSeasonId", "userid");
        assertThat(state.isPresent(), equalTo(true));
        assertThat(state.get().isStarted(), equalTo(false));
    }

    private void givenEvalSeasonAndRatee(String evalSeasonId, String userId) {
        EvalSeasonMappingModel mappingModel = mock(EvalSeasonMappingModel.class);
        when(mappingModel.containsRatee(userId)).thenReturn(true);
        EvalSeason evalSeason = mock(EvalSeason.class);
        EvalSeasonData seasonData = new EvalSeasonData(evalSeason, mappingModel);

        when(mockEvalSeasonDataLoader.load(evalSeasonId)).thenReturn(
                Optional.of(seasonData));

    }

    private void givenNoPersonalEval(String evalSeasonId, String userid) {
        String aggrId = createId(evalSeasonId, userid);
        when(mockPersonalEvalRepository.load(aggrId)).thenThrow(new AggregateNotFoundException(aggrId, "not found"));
    }

    @Test
    public void givenPersonalEval_thenReturn_StartedState() throws Exception {
        givenEvalSeasonAndRatee("evalSeasonId", "userid");
        givenPersonalEval("evalSeasonId", "userid");
        Optional<PersonalEvalState> state = dataLoader.getPersonalEvalStateOf("evalSeasonId", "userid");
        assertThat(state, notNullValue());
        assertThat(state.get().isStarted(), equalTo(true));
    }

    private void givenPersonalEval(String evalSeasonId, String userid) {
        PersonalEval personalEval = mock(PersonalEval.class);
        when(personalEval.isStarted()).thenReturn(true);
        when(mockPersonalEvalRepository.load(createId(evalSeasonId, userid))).thenReturn(personalEval);
    }

}
