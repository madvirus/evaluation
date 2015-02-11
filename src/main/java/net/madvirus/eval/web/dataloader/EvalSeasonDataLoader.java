package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;

import java.util.List;
import java.util.Optional;

public interface EvalSeasonDataLoader {
    EvalSeasonData load(String seasonId) throws EvalSeasonNotFoundException;

    List<EvalSeasonSimpleData> loadAll();
}
