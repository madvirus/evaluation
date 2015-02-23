package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;
import net.madvirus.eval.domain.evalseason.DistributionRule;

import java.util.List;

public interface EvalSeasonDataLoader {
    EvalSeasonData load(String seasonId) throws EvalSeasonNotFoundException;

    List<EvalSeasonSimpleData> loadAll();

    DistributionRuleData getDistributionRule(String evalSeasonId);
}
