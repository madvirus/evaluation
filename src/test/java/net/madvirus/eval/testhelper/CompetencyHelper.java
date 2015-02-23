package net.madvirus.eval.testhelper;

import net.madvirus.eval.api.personaleval.Grade;
import net.madvirus.eval.domain.personaleval.CompetencyEvalSet;
import net.madvirus.eval.domain.personaleval.ItemEval;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CompetencyHelper {
    public static CompetencyEvalSet createCompetencyEvalSet(String rater, boolean hasLeadership, boolean hasAm, boolean done) {
        return createCompetencyEvalWith(rater, hasLeadership, hasAm,
                (c, l, a) -> new CompetencyEvalSet(c, l, a, new ItemEval("c", Grade.A), done));
    }
    public static <T> T createCompetencyEvalWith(String rater, boolean hasLeadership, boolean hasAm,
                                                 CreatorFunction<T> creator) {
        List<ItemEval> commons = IntStream.range(0, 5)
                .mapToObj(idx -> {
                    return new ItemEval(rater + "공통 역량 평가" + idx, Grade.A);
                }).collect(Collectors.toList());
        List<ItemEval> leaderships = hasLeadership ?
                IntStream.range(0, 5)
                        .mapToObj(idx -> new ItemEval(rater + "리더십 역량 평가" + idx, Grade.B)).collect(Collectors.toList()) :
                null;
        List<ItemEval> ams = hasAm ?
                IntStream.range(0, 4)
                        .mapToObj(idx -> new ItemEval(rater + "AM 역량 평가" + idx, Grade.B)).collect(Collectors.toList()) :
                null;
        return creator.func(commons, leaderships, ams);
    }

    public static interface CreatorFunction<T> {

        T func(List<ItemEval> commons, List<ItemEval> leaderships, List<ItemEval> ams);
    }

}
