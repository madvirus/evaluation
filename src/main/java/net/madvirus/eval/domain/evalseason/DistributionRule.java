package net.madvirus.eval.domain.evalseason;

import java.util.List;

public class DistributionRule {
    private String name;
    private int sNumber;
    private int aNumber;
    private int bNumber;
    private int cdNumber;
    private List<String> rateeIds;

    protected DistributionRule() {}
    public DistributionRule(String name, int sNumber, int aNumber, int bNumber, int cdNumber, List<String> rateeIds) {
        this.name = name;
        this.sNumber = sNumber;
        this.aNumber = aNumber;
        this.bNumber = bNumber;
        this.cdNumber = cdNumber;
        this.rateeIds = rateeIds;
    }

    public int getaNumber() {
        return aNumber;
    }

    public int getbNumber() {
        return bNumber;
    }

    public int getCdNumber() {
        return cdNumber;
    }

    public String getName() {
        return name;
    }

    public List<String> getRateeIds() {
        return rateeIds;
    }

    public int getsNumber() {
        return sNumber;
    }

    public boolean sameRateeNumberAndGradeCountSum() {
        return (sNumber + aNumber + bNumber + cdNumber) == rateeIds.size();
    }

    public boolean hasRatees(List<String> rateeCheckIds) {
        for (String rateeId : rateeCheckIds) {
            if (rateeIds.contains(rateeId)) {
                return true;
            }
        }
        return false;
    }
}
