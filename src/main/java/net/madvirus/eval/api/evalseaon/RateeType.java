package net.madvirus.eval.api.evalseaon;

import java.math.BigDecimal;

public enum RateeType {
    MEMBER(0.7, 0.3, 0, 0), // 팀원
    PART_LEADER(0.7, 0.2, 0.1, 0), // 파트장
    TEAM_LEADER(0.6, 0.2, 0.2, 0), // 팀장
    AM(0.6, 0.2, 0.1, 0.1), // AM
    AM_MEMBER(0.6, 0.3, 0, 0.1), // AM팀원
    AM_LEADER(0.8, 0.1, 0, 0.1) // AM팀장
    ;

    private BigDecimal performanceRatio;
    private BigDecimal commonRatio;
    private BigDecimal leadershipRatio;
    private BigDecimal amRatio;

    private RateeType(double performanceRatio, double commonRatio,
                      double leadershipRatio, double amRatio) {
        this.performanceRatio = BigDecimal.valueOf(performanceRatio);
        this.commonRatio = BigDecimal.valueOf(commonRatio);
        this.leadershipRatio = BigDecimal.valueOf(leadershipRatio);
        this.amRatio = BigDecimal.valueOf(amRatio);
    }

    public BigDecimal getPerformanceRatio() {
        return performanceRatio;
    }

    public BigDecimal getCommonRatio() {
        return commonRatio;
    }

    public BigDecimal getLeadershipRatio() {
        return leadershipRatio;
    }

    public BigDecimal getAmRatio() {
        return amRatio;
    }

    public boolean hasLeadership() {
        return leadershipRatio.doubleValue() > 0.0;
    }

    public boolean hasAm() {
        return amRatio.doubleValue() > 0.0;
    }
}
