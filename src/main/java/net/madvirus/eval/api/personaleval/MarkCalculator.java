package net.madvirus.eval.api.personaleval;

import net.madvirus.eval.api.evalseaon.RateeType;

import java.math.BigDecimal;

public class MarkCalculator {
    public static Double calculate(
            RateeType rateeType, Grade perfGrade,
            Double commonAvg, Double leadershipAvg, Double amAvg) {
        BigDecimal perfMark = BigDecimal.valueOf(perfGrade.getNumber());
        BigDecimal perfPortion = rateeType.getPerformanceRatio().multiply(perfMark);
        BigDecimal commonPortion = rateeType.getCommonRatio().multiply(BigDecimal.valueOf(commonAvg));
        BigDecimal leadershipPortion = rateeType.hasLeadership() ?
                rateeType.getLeadershipRatio().multiply(BigDecimal.valueOf(leadershipAvg)) :
                BigDecimal.ZERO;
        BigDecimal amPortion = rateeType.hasAm() ?
                rateeType.getAmRatio().multiply(BigDecimal.valueOf(amAvg)) :
                BigDecimal.ZERO;
        return perfPortion.add(commonPortion).add(leadershipPortion).add(amPortion).doubleValue();
    }
}
