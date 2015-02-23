package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;
import net.madvirus.eval.api.evalseaon.RateeNotFoundException;
import net.madvirus.eval.api.personaleval.PersonalEvalNotFoundException;
import net.madvirus.eval.api.personaleval.YouAreNotRateeException;
import net.madvirus.eval.api.personaleval.colleague.YouAreNotColleagueRaterException;
import net.madvirus.eval.api.personaleval.first.YouAreNotFirstRaterException;
import net.madvirus.eval.api.personaleval.second.YouAreNotSecondRaterException;
import net.madvirus.eval.domain.evalseason.EvalSeason;
import net.madvirus.eval.domain.evalseason.RateeType;
import net.madvirus.eval.domain.personaleval.CompetencyEvalSet;
import net.madvirus.eval.domain.personaleval.PersonalEval;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModel;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModelRepository;
import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.query.user.UserModelRepository;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import scala.Option;

import java.util.*;

import static net.madvirus.eval.axon.AxonUtil.runInUOW;
import static net.madvirus.eval.domain.personaleval.PersonalEval.createId;

public class PersonalEvalDataLoaderImpl implements PersonalEvalDataLoader {

    private Repository<PersonalEval> personalEvalRepository;
    private Repository<EvalSeason> evalSeasonRepository;
    private UserModelRepository userModelRepository;
    private EvalSeasonMappingModelRepository evalSeasonMappingModelRepository;

    public PersonalEvalDataLoaderImpl(Repository<PersonalEval> personalEvalRepository,
                                      Repository<EvalSeason> evalSeasonRepository,
                                      UserModelRepository userModelRepository,
                                      EvalSeasonMappingModelRepository evalSeasonMappingModelRepository) {
        this.personalEvalRepository = personalEvalRepository;
        this.evalSeasonRepository = evalSeasonRepository;
        this.userModelRepository = userModelRepository;
        this.evalSeasonMappingModelRepository = evalSeasonMappingModelRepository;
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
    public PersonalEvalState getPersonalEvalStateOf(String evalSeasonId, String rateeId) {
        return runInUOW(() -> {
            checkEvalSeasonExistsAndUserIsRatee(evalSeasonId, rateeId);
            UserModel ratee = userModelRepository.findOne(rateeId);
            try {
                PersonalEval personalEval = personalEvalRepository.load(createId(evalSeasonId, rateeId));
                return new PersonalEvalData(personalEval, ratee);
            } catch (AggregateNotFoundException e) {
                return PersonalEvalStateBuilder.notStarted(ratee);
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
    public SelfPerfEvalData getSelfPerfEvalDataForSelfEvalForm(String evalSeasonId, String rateeId) {
        return runInUOW(() -> {
            checkEvalSeasonExistsAndUserIsRatee(evalSeasonId, rateeId);

            String personalEvalId = createId(evalSeasonId, rateeId);
            try {
                PersonalEvalData personalEval = getPersonalEval(evalSeasonId, rateeId);
                return new SelfPerfEvalData(
                        evalSeasonId,
                        personalEval.getId(),
                        personalEval.getRatee().getId(),
                        personalEval.getPerfItemAndAllEvals(),
                        personalEval.isSelfPerfEvalDone()
                );
            } catch (PersonalEvalNotFoundException e) {
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
                RateeType rateeType = evalSeasonMappingModelRepository.findById(evalSeasonId)
                        .get().getRateeMappingOf(rateeId).get().getType();
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
        EvalSeasonMappingModel mappingModel = getMappingModelOrThrowExIfNotFound(evalSeasonId);
        if (!mappingModel.containsRatee(userId)) {
            throw new YouAreNotRateeException(String.format("%s user is not ratee in %s evalseason", userId, evalSeasonId));
        }
    }

    private EvalSeasonMappingModel getMappingModelOrThrowExIfNotFound(String evalSeasonId) {
        Option<EvalSeasonMappingModel> mappingModelOpt = evalSeasonMappingModelRepository.findById(evalSeasonId);
        if (mappingModelOpt.isEmpty()) {
            throw new EvalSeasonNotFoundException();
        }
        return mappingModelOpt.get();
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
        EvalSeasonMappingModel mappingModel = getMappingModelOrThrowExIfNotFound(evalSeasonId);
        if (!mappingModel.containsRatee(rateeId)) {
            throw new RateeNotFoundException(rateeId);
        }
        // TODO 동료 평가자 검사하는 코드 한 곳으로 몰 필요
        if (!mappingModel.getRateeMappingOf(rateeId).get().containsColleagueRater(colleagueRaterId)) {
            throw new YouAreNotColleagueRaterException();
        }
    }

    @Override
    public FirstTotalEvalData getFirstTotalEvalData(String evalSeasonId, String firstRaterId) {
        return runInUOW(() -> {
            EvalSeasonMappingModel mappingModel = getMappingModelOrThrowExIfNotFound(evalSeasonId);
            if (!mappingModel.containsFirstRater(firstRaterId)) {
                throw new YouAreNotFirstRaterException();
            }
            UserModel firstRater = userModelRepository.findOne(firstRaterId);
            EvalSeason evalSeason = evalSeasonRepository.load(evalSeasonId);
            FirstRaterRuleData firstRaterRuleData = evalSeason.populateRuleData(firstRaterId,
                    (ruleList) -> new FirstRaterRuleData(
                            firstRater,
                            mappingModel.getRateesOfFirstRater(firstRaterId),
                            ruleList));

            Set<UserModel> users = mappingModel.getRateesOfFirstRater(firstRaterId);
            List<FirstTotalEvalSummary> evalSummaryList = new ArrayList<>();
            for (UserModel user : users) {
                evalSummaryList.add(
                        new FirstTotalEvalSummary(
                                user,
                                personalEvalRepository.load(PersonalEval.createId(evalSeasonId, user.getId()))));
            }
            return new FirstTotalEvalData(firstRaterRuleData, evalSummaryList);
        });
    }

    @Override
    public SecondTotalEvalData getSecondTotalEvalData(String evalSeasonId, String secondRaterId) {
        return runInUOW(() -> {
            EvalSeasonMappingModel mappingModel = getMappingModelOrThrowExIfNotFound(evalSeasonId);
            if (!mappingModel.containsSecondRater(secondRaterId)) {
                throw new YouAreNotSecondRaterException();
            }
            Set<UserModel> users = mappingModel.getRateesOfSecondRater(secondRaterId);

            List<SecondTotalEvalSummary> evalSummaryList = new ArrayList<>();
            for (UserModel user : users) {
                evalSummaryList.add(
                        new SecondTotalEvalSummary(
                                user,
                                personalEvalRepository.load(PersonalEval.createId(evalSeasonId, user.getId()))));
            }
            return new SecondTotalEvalData(evalSummaryList);
        });
    }

}
