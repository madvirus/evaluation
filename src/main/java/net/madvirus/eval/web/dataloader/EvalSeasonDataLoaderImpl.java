package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;
import net.madvirus.eval.domain.evalseason.DistributionRule;
import net.madvirus.eval.domain.evalseason.EvalSeason;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModel;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModelRepository;
import net.madvirus.eval.query.evalseason.RateeMappingModel;
import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.query.user.UserModelRepository;
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
    private EvalSeasonMappingModelRepository evalSeasonMappingModelRepository;
    private UserModelRepository userModelRepository;

    public EvalSeasonDataLoaderImpl(Repository<EvalSeason> evalSeasonRepository,
                                    EvalSeasonMappingModelRepository evalSeasonMappingModelRepository,
                                    UserModelRepository userModelRepository) {
        this.evalSeasonRepository = evalSeasonRepository;
        this.evalSeasonMappingModelRepository = evalSeasonMappingModelRepository;
        this.userModelRepository = userModelRepository;
    }

    @Override
    public EvalSeasonData load(String id) {
        return runInUOW(() -> {
            try {
                EvalSeason evalSeason = evalSeasonRepository.load(id);
                Option<EvalSeasonMappingModel> model = evalSeasonMappingModelRepository.findById(id);
                return new EvalSeasonData(evalSeason, model.get());
            } catch (AggregateNotFoundException ex) {
                throw new EvalSeasonNotFoundException();
            }
        });
    }

    @Override
    public List<EvalSeasonSimpleData> loadAll() {
        return runInUOW(() -> {
            List<EvalSeasonMappingModel> all = evalSeasonMappingModelRepository.findAll();
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

        Option<EvalSeasonMappingModel> mappingModelOpt = evalSeasonMappingModelRepository.findById(evalSeason.getId());
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

    private Set<UserModel> getFirstRaterUserModels(EvalSeasonMappingModel evalSeasonMappingModel) {
        List<RateeMappingModel> rateeMappingModels = evalSeasonMappingModel.getRateeMappingModels();
        Set<UserModel> firstRaters = new HashSet<>();
        rateeMappingModels.forEach(model -> {
            if (model.getFirstRater() != null) {
                firstRaters.add(model.getFirstRater());
            }
        });
        return firstRaters;
    }
}
