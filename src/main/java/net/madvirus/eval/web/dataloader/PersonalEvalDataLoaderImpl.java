package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.evalseaon.RateeNotFoundException;
import net.madvirus.eval.api.evalseaon.RateeType;
import net.madvirus.eval.api.personaleval.*;
import net.madvirus.eval.api.personaleval.colleague.YouAreNotColleagueRaterException;
import net.madvirus.eval.api.personaleval.first.YouAreNotFirstRaterException;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModel;
import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.query.user.UserModelRepository;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static net.madvirus.eval.api.personaleval.PersonalEval.createId;
import static net.madvirus.eval.axon.AxonUtil.runInUOW;

public class PersonalEvalDataLoaderImpl implements PersonalEvalDataLoader {

    private EvalSeasonDataLoader evalSeasonDataLoader;
    private Repository<PersonalEval> personalEvalRepository;
    private UserModelRepository userModelRepository;

    public PersonalEvalDataLoaderImpl(Repository<PersonalEval> personalEvalRepository,
                                      EvalSeasonDataLoader evalSeasonDataLoader,
                                      UserModelRepository userModelRepository) {
        this.personalEvalRepository = personalEvalRepository;
        this.evalSeasonDataLoader = evalSeasonDataLoader;
        this.userModelRepository = userModelRepository;
    }

    @Override
    public PersonalEvalData getPersonalEval(String evalSeasonId, String rateeId) throws PersonalEvalNotFoundException {
        return runInUOW(() -> {
            try {
                PersonalEval personalEval = personalEvalRepository.load(createId(evalSeasonId, rateeId));
                UserModel ratee = userModelRepository.findOne(personalEval.getUserId());

                return new PersonalEvalData(personalEval, ratee);
            } catch (AggregateNotFoundException e) {
                throw new PersonalEvalNotFoundException();
            }
        });
    }

    @Override
    public List<ColleagueEvalState> getColleagueEvalStates(String evalSeasonId, String colleagueId, Set<String> rateeIds) {
        return runInUOW(() -> {
            List<ColleagueEvalState> states = new ArrayList<ColleagueEvalState>();
            for (String rateeId : rateeIds) {
                UserModel ratee = userModelRepository.findOne(rateeId);
                try {
                    PersonalEval personalEval = personalEvalRepository.load(createId(evalSeasonId, rateeId));
                    boolean selfCompeEvalDone = personalEval.isSelfCompeEvalDone();
                    boolean colleagueEvalDone = personalEval.isColleagueCompeEvalDone(colleagueId);
                    states.add(new ColleagueEvalState(ratee, true, selfCompeEvalDone, colleagueEvalDone));
                } catch (AggregateNotFoundException e) {
                    states.add(new ColleagueEvalState(ratee, false, false, false));
                }
            }
            return states;
        });
    }

    @Override
    public PersonalEvalState getPersonalEvalStateOf(String evalSeasonId, String rateeId) {
        return runInUOW(() -> {
            checkEvalSeasonExistsAndUserIsRatee(evalSeasonId, rateeId);
            try {
                PersonalEval personalEval = personalEvalRepository.load(createId(evalSeasonId, rateeId));
                UserModel ratee = userModelRepository.findOne(personalEval.getUserId());
                return new PersonalEvalData(personalEval, ratee);
            } catch (AggregateNotFoundException e) {
                return PersonalEvalStateBuilder.notStarted();
            }
        });
    }

    @Override
    public SelfPerfEvalData getSelfPerfEvalDataForSelfEvalForm(String evalSeasonId, String rateeId) {
        return runInUOW(() -> {
            checkEvalSeasonExistsAndUserIsRatee(evalSeasonId, rateeId);

            String personalEvalId = createId(evalSeasonId, rateeId);
            try {
                PersonalEval personalEval = personalEvalRepository.load(personalEvalId);
                return new SelfPerfEvalData(
                        evalSeasonId,
                        personalEval.getId(),
                        personalEval.getUserId(),
                        personalEval.getPerfItemAndSelfEvals(),
                        personalEval.isSelfPerfEvalDone()
                );
            } catch (AggregateNotFoundException e) {
                return new SelfPerfEvalData(
                        evalSeasonId,
                        personalEvalId,
                        rateeId,
                        Collections.emptyList(),
                        false);
            }
        });
    }

    @Override
    public CompeEvalData getSelfCompeEvalDataForSelfEvalForm(String evalSeasonId, String rateeId) {
        return runInUOW(() -> {
            checkEvalSeasonExistsAndUserIsRatee(evalSeasonId, rateeId);
            String personalEvalId = createId(evalSeasonId, rateeId);
            try {
                PersonalEval personalEval = personalEvalRepository.load(personalEvalId);
                Optional<CompetencyEvalSet> selfCompeEvalSet = personalEval.getSelfCompeEvalSet();
                return new CompeEvalData(
                        evalSeasonId,
                        personalEval.getId(),
                        personalEval.getUserId(),
                        personalEval.getRateeType(),
                        selfCompeEvalSet.orElse(CompetencyEvalSetUtil.createEmptyEvalSet(personalEval.getRateeType()))
                );
            } catch (AggregateNotFoundException e) {
                RateeType rateeType = evalSeasonDataLoader.load(evalSeasonId).getMappingModel().getRateeMappingOf(rateeId).get().getType();
                return new CompeEvalData(
                        evalSeasonId,
                        personalEvalId,
                        rateeId,
                        rateeType,
                        CompetencyEvalSetUtil.createEmptyEvalSet(rateeType)
                );
            }
        });
    }

    private void checkEvalSeasonExistsAndUserIsRatee(String evalSeasonId, String userId) {
        EvalSeasonData evalSeasonDataOpt = evalSeasonDataLoader.load(evalSeasonId);
        if (!evalSeasonDataOpt.getMappingModel().containsRatee(userId)) {
            throw new YouAreNotRateeException();
        }
    }

    @Override
    public CompeEvalData getColleagueCompeEvalDataForEvalForm(String evalSeasonId, String rateeId, String colleagueId) {
        return runInUOW(() -> {
            checkEvalSeasonAndMatchRateeColleagueRater(evalSeasonId, rateeId, colleagueId);
            String personalEvalId = createId(evalSeasonId, rateeId);
            try {
                PersonalEval personalEval = personalEvalRepository.load(personalEvalId);
                Optional<CompetencyEvalSet> colleagueCompeEvalSet = personalEval.getColleagueCompeEvalSet(colleagueId);
                return new CompeEvalData(
                        evalSeasonId,
                        personalEval.getId(),
                        personalEval.getUserId(),
                        personalEval.getRateeType(),
                        colleagueCompeEvalSet.orElseGet(() -> CompetencyEvalSetUtil.createEmptyEvalSet(personalEval.getRateeType()))
                );
            } catch (AggregateNotFoundException e) {
                throw new PersonalEvalNotFoundException();
            }
        });
    }

    private void checkEvalSeasonAndMatchRateeColleagueRater(String evalSeasonId, String rateeId, String colleagueRaterId) {
        EvalSeasonData evalSeasonDataOpt = evalSeasonDataLoader.load(evalSeasonId);
        EvalSeasonMappingModel mappingModel = evalSeasonDataOpt.getMappingModel();
        if (!mappingModel.containsRatee(rateeId)) {
            throw new RateeNotFoundException();
        }
        // TODO 동료 평가자 검사하는 코드 한 곳으로 몰 필요
        if (!mappingModel.getRateeMappingOf(rateeId).get().containsColleagueRater(colleagueRaterId)) {
            throw new YouAreNotColleagueRaterException();
        }
    }

    @Override
    public FirstTotalEvalData getFirstTotalEvalData(String evalSeasonId, String firstRaterId) {
        return runInUOW(() -> {
            EvalSeasonData evalSeasonDataOpt = evalSeasonDataLoader.load(evalSeasonId);
            if (!evalSeasonDataOpt.getMappingModel().containsFirstRater(firstRaterId)) {
                throw new YouAreNotFirstRaterException();
            }
            Set<UserModel> users = evalSeasonDataOpt.getMappingModel().getRateesOfFirstRater(firstRaterId);
            List<FirstTotalEvalSummary> evalSummaryList = new ArrayList<>();
            for (UserModel user : users) {
                evalSummaryList.add(
                        new FirstTotalEvalSummary(
                                user,
                                personalEvalRepository.load(PersonalEval.createId(evalSeasonId, user.getId()))));
            }
            return new FirstTotalEvalData(evalSummaryList);
        });
    }

    private boolean checkEvalSeasonExistsAndUserIsFirstRater(String evalSeasonId, String userId) {
        EvalSeasonData evalSeasonDataOpt = evalSeasonDataLoader.load(evalSeasonId);
        if (!evalSeasonDataOpt.getMappingModel().containsFirstRater(userId)) {
            throw new YouAreNotFirstRaterException();
        }
        return false;
    }

}
