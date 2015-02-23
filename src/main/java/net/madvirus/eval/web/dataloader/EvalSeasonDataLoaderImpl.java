package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;
import net.madvirus.eval.domain.evalseason.EvalSeason;
import net.madvirus.eval.domain.personaleval.PersonalEval;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModel;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModelRepository;
import net.madvirus.eval.query.evalseason.RateeMappingModel;
import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.query.user.UserModelRepository;
import net.madvirus.eval.web.dataloader.EvalSeasonAllEvalStates.EvalStateData;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import scala.Option;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.madvirus.eval.axon.AxonUtil.runInUOW;

public class EvalSeasonDataLoaderImpl implements EvalSeasonDataLoader {
    private Repository<EvalSeason> evalSeasonRepository;
    private EvalSeasonMappingModelRepository mappingModelRepository;
    private UserModelRepository userModelRepository;
    private Repository<PersonalEval> personalEvalRepository;

    public EvalSeasonDataLoaderImpl(Repository<EvalSeason> evalSeasonRepository,
                                    EvalSeasonMappingModelRepository mappingModelRepository,
                                    UserModelRepository userModelRepository,
                                    Repository<PersonalEval> personalEvalRepository) {
        this.evalSeasonRepository = evalSeasonRepository;
        this.mappingModelRepository = mappingModelRepository;
        this.userModelRepository = userModelRepository;
        this.personalEvalRepository = personalEvalRepository;
    }

    @Override
    public EvalSeasonData load(String id) {
        return runInUOW(() -> {
            try {
                EvalSeason evalSeason = evalSeasonRepository.load(id);
                Option<EvalSeasonMappingModel> model = mappingModelRepository.findById(id);
                return new EvalSeasonData(evalSeason, model.get());
            } catch (AggregateNotFoundException ex) {
                throw new EvalSeasonNotFoundException();
            }
        });
    }

    @Override
    public List<EvalSeasonSimpleData> loadAll() {
        return runInUOW(() -> {
            List<EvalSeasonMappingModel> all = mappingModelRepository.findAll();
            List<EvalSeasonSimpleData> dataList = all.stream()
                    .map(x -> evalSeasonRepository.load(x.getId()))
                    .map(ev -> new EvalSeasonSimpleData(ev))
                    .collect(Collectors.toList());
            return dataList;
        });
    }

    @Override
    public DistributionRuleData getDistributionRule(String evalSeasonId) {
        return runInUOW(() -> {
            try {
                EvalSeason evalSeason = evalSeasonRepository.load(evalSeasonId);
                return createDistributionRuleData(evalSeason);
            } catch (AggregateNotFoundException e) {
                throw new EvalSeasonNotFoundException(e);
            }
        });
    }

    private DistributionRuleData createDistributionRuleData(EvalSeason evalSeason) {
        DistributionRuleData data = new DistributionRuleData();

        Option<EvalSeasonMappingModel> mappingModelOpt = mappingModelRepository.findById(evalSeason.getId());
        EvalSeasonMappingModel evalSeasonMappingModel = mappingModelOpt.get();

        Set<UserModel> firstRaters = getFirstRaterUserModels(evalSeasonMappingModel);
        firstRaters.forEach(firstRater -> {
            FirstRaterRuleData firstRaterRuleData = evalSeason.populateRuleData(firstRater.getId(),
                    (ruleList) -> new FirstRaterRuleData(
                            firstRater,
                            evalSeasonMappingModel.getRateesOfFirstRater(firstRater.getId()),
                            ruleList));
            data.addRule(firstRaterRuleData);
        });
        return data;
    }

    private Set<UserModel> getFirstRaterUserModels(EvalSeasonMappingModel mappingModel) {
        List<RateeMappingModel> rateeMappingModels = mappingModel.getRateeMappingModels();
        Set<UserModel> firstRaters = new HashSet<>();
        rateeMappingModels.forEach(model -> {
            if (model.getFirstRater() != null) {
                firstRaters.add(model.getFirstRater());
            }
        });
        return firstRaters;
    }

    @Override
    public EvalSeasonAllEvalStates loadEvalStates(String evalSeasonId) {
        return runInUOW(() -> {
            Option<EvalSeasonMappingModel> mappingModelOption = mappingModelRepository.findById(evalSeasonId);
            if (mappingModelOption.isEmpty()) {
                throw new EvalSeasonNotFoundException();
            }
            EvalSeasonAllEvalStates states = new EvalSeasonAllEvalStates(evalSeasonId);

            EvalSeasonMappingModel mappingModel = mappingModelOption.get();
            List<RateeMappingModel> mappings = mappingModel.getRateeMappingModels();
            mappings.forEach(mapping -> {
                try {
                    PersonalEval personalEval = personalEvalRepository.load(PersonalEval.createId(evalSeasonId, mapping.getRatee().getId()));
                    EvalStateData.Builder builder = EvalStateData.self(mapping.getRatee(), personalEval.isSelfPerfEvalDone(), personalEval.isSelfCompeEvalDone());
                    if (mapping.hasFirstRater()) {
                        builder.first(mapping.getFirstRater(),
                                personalEval.isFirstPerfEvalHad(),
                                personalEval.isFirstCompeEvalHad(),
                                personalEval.isFirstTotalEvalDone());
                    } else {
                        builder.firstSkip();
                    }
                    builder.second(mapping.getSecondRater(),
                            personalEval.isSecondPerfEvalHad(),
                            personalEval.isSecondCompeEvalHad(),
                            personalEval.isSecondTotalEvalDone());
                    mapping.getColleagueRaters().forEach(collUser -> {
                        boolean collEvalDone = personalEval.isColleagueCompeEvalDone(collUser.getId());
                        boolean collEvalHad = personalEval.hasColleagueCompeEval(collUser.getId());
                        if (collEvalDone) {
                            builder.colleague(collUser,
                                    EvalSeasonAllEvalStates.State.DONE);
                        } else {
                            builder.colleague(collUser, collEvalHad ? EvalSeasonAllEvalStates.State.DOING : EvalSeasonAllEvalStates.State.NONE);
                        }
                    });
                    states.add(builder.build());
                } catch (AggregateNotFoundException e) {
                    states.add(EvalStateData.notYetStarted(mapping.getRatee(), mapping.getColleagueRaters(), mapping.getFirstRater(), mapping.getSecondRater()));
                }
            });
            return states;
        });
    }
}
