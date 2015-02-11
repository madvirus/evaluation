package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.evalseaon.EvalSeason;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModel;
import net.madvirus.eval.query.evalseason.RateeMappingModel;
import scala.Option;

import java.util.List;

public class EvalSeasonData extends EvalSeasonSimpleData {
    private EvalSeasonMappingModel evalSeasonMappingModel;

    public EvalSeasonData(EvalSeason evalSeason, EvalSeasonMappingModel evalSeasonMappingModel) {
        super(evalSeason);
        this.evalSeasonMappingModel = evalSeasonMappingModel;
    }

    public EvalSeasonMappingModel getMappingModel() {
        return evalSeasonMappingModel;
    }

    public List<RateeMappingModel> getMappings() {
        return evalSeasonMappingModel.getRateeMappingModels();
    }

    public boolean containsRatee(String rateeId) {
        return evalSeasonMappingModel.containsRatee(rateeId);
    }

    public boolean checkColleagueRater(String rateeId, String colleagueRaterId) {
        Option<RateeMappingModel> mappingOpt = evalSeasonMappingModel.getRateeMappingOf(rateeId);
        return mappingOpt.isEmpty() ? false :
                mappingOpt.get().containsColleagueRater(colleagueRaterId);
    }
}
