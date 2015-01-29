package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.command.evalseason.EvalSeason;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModel;
import net.madvirus.eval.query.evalseason.RateeMappingModel;

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
}
