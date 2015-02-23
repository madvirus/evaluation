package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.personaleval.Grade;
import net.madvirus.eval.domain.evalseason.RateeType;
import net.madvirus.eval.domain.personaleval.CompetencyEvalSet;
import net.madvirus.eval.domain.personaleval.ItemEval;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class CompetencyEvalSetUtil {
    public static CompetencyEvalSet createEmptyEvalSet(RateeType rateeType) {
        return new CompetencyEvalSet(
                emptyItemEvals(5),
                rateeType.hasLeadership() ? emptyItemEvals(5) : null,
                rateeType.hasAm() ? emptyItemEvals(4) : null,
                ItemEval.empty(),
                false
        );
    }

    public static List<ItemEval> emptyItemEvals(int count) {
        return IntStream.range(0, count).mapToObj(x -> new ItemEval("", Grade.A)).collect(Collectors.toList());
    }

}
