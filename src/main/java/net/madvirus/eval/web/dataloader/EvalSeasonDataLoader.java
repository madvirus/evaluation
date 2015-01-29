package net.madvirus.eval.web.dataloader;

import java.util.List;
import java.util.Optional;

public interface EvalSeasonDataLoader {
    Optional<EvalSeasonData> load(String id1);

    List<EvalSeasonSimpleData> loadAll();
}
