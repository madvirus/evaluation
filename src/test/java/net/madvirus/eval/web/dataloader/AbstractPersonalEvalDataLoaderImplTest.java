package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.evalseaon.EvalSeason;
import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;
import net.madvirus.eval.api.evalseaon.RateeType;
import net.madvirus.eval.api.personaleval.PersonalEval;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModel;
import net.madvirus.eval.query.evalseason.RateeMappingModel;
import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.query.user.UserModelRepository;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import org.junit.Before;

import static net.madvirus.eval.api.personaleval.PersonalEval.createId;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractPersonalEvalDataLoaderImplTest {
    protected PersonalEvalDataLoaderImpl dataLoader;
    private Repository<PersonalEval> mockPersonalEvalRepository;
    private EvalSeasonDataLoader mockEvalSeasonDataLoader;

    @Before
    public void setUp() throws Exception {
        mockPersonalEvalRepository = mock(Repository.class);
        mockEvalSeasonDataLoader = mock(EvalSeasonDataLoader.class);
        UserModelRepository mockUserRepository = mock(UserModelRepository.class);
        dataLoader = new PersonalEvalDataLoaderImpl(mockPersonalEvalRepository, mockEvalSeasonDataLoader, mockUserRepository);
    }

    protected void givenNoEvalSeason(String noEvalSeasonId) {
        when(mockEvalSeasonDataLoader.load(noEvalSeasonId)).thenThrow(new EvalSeasonNotFoundException());
    }

    protected void givenEvalSeasonButNotRatee(String evalSeasonId, String userId) {
        EvalSeasonMappingModel mappingModel = mock(EvalSeasonMappingModel.class);
        when(mappingModel.containsRatee(userId)).thenReturn(false);
        EvalSeason evalSeason = mock(EvalSeason.class);
        EvalSeasonData seasonData = new EvalSeasonData(evalSeason, mappingModel);

        when(mockEvalSeasonDataLoader.load(evalSeasonId)).thenReturn(seasonData);
    }

    protected void givenEvalSeasonAndRatee(String evalSeasonId, String userId) {
        EvalSeasonMappingModel mappingModel = new EvalSeasonMappingModel(evalSeasonId, 0)
                .updateMapping(new RateeMappingModel(
                        new UserModel(userId, userId, ""),
                        RateeType.MEMBER,
                        new UserModel("first", "first", ""),
                        new UserModel("second", "second", "")
                ), 1);
        EvalSeason evalSeason = mock(EvalSeason.class);
        EvalSeasonData seasonData = new EvalSeasonData(evalSeason, mappingModel);

        when(mockEvalSeasonDataLoader.load(evalSeasonId)).thenReturn(seasonData);

    }

    protected void givenNoPersonalEval(String evalSeasonId, String userid) {
        String aggrId = createId(evalSeasonId, userid);
        when(mockPersonalEvalRepository.load(aggrId)).thenThrow(new AggregateNotFoundException(aggrId, "not found"));
    }

    protected void givenPersonalEval(String evalSeasonId, String userid) {
        PersonalEval personalEval = mock(PersonalEval.class);
        when(mockPersonalEvalRepository.load(createId(evalSeasonId, userid))).thenReturn(personalEval);
    }
}
