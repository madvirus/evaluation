package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.domain.evalseason.EvalSeason;
import net.madvirus.eval.domain.evalseason.RateeType;
import net.madvirus.eval.domain.personaleval.PersonalEval;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModel;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModelRepository;
import net.madvirus.eval.query.evalseason.RateeMappingModel;
import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.query.user.UserModelRepository;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import org.junit.Before;
import scala.Option;

import static net.madvirus.eval.domain.personaleval.PersonalEval.createId;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractPersonalEvalDataLoaderImplTest {
    protected PersonalEvalDataLoaderImpl dataLoader;
    private Repository<PersonalEval> mockPersonalEvalRepository;
    private Repository<EvalSeason> mockEvalSeasonRepository;
    private EvalSeasonMappingModelRepository mockMappingModelRepository;

    @Before
    public void setUp() throws Exception {
        mockPersonalEvalRepository = mock(Repository.class);
        mockEvalSeasonRepository = mock(Repository.class);
        UserModelRepository mockUserRepository = mock(UserModelRepository.class);
        mockMappingModelRepository = mock(EvalSeasonMappingModelRepository.class);
        dataLoader = new PersonalEvalDataLoaderImpl(mockPersonalEvalRepository, mockEvalSeasonRepository, mockUserRepository, mockMappingModelRepository);
    }

    protected void givenNoEvalSeason(String noEvalSeasonId) {
        when(mockMappingModelRepository.findById(noEvalSeasonId)).thenReturn(Option.<EvalSeasonMappingModel>empty());
    }

    protected void givenEvalSeasonButNotRatee(String evalSeasonId, String userId) {
        EvalSeasonMappingModel mappingModel = mock(EvalSeasonMappingModel.class);
        when(mappingModel.containsRatee(userId)).thenReturn(false);
        when(mockMappingModelRepository.findById(evalSeasonId)).thenReturn(Option.apply(mappingModel));
    }

    protected void givenEvalSeasonAndRatee(String evalSeasonId, String userId) {
        EvalSeasonMappingModel mappingModel = new EvalSeasonMappingModel(evalSeasonId, 0)
                .updateMapping(new RateeMappingModel(
                        new UserModel(userId, userId, ""),
                        RateeType.MEMBER,
                        new UserModel("first", "first", ""),
                        new UserModel("second", "second", "")
                ), 1);
        when(mockMappingModelRepository.findById(evalSeasonId)).thenReturn(Option.apply(mappingModel));
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
