package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.domain.evalseason.DistributionRule;
import net.madvirus.eval.query.user.UserModel;

import java.util.List;

public class RuleData {
    private DistributionRule rule;
    private List<UserModel> ratees;

    public RuleData(DistributionRule rule, List<UserModel> ratees) {
        this.rule = rule;
        this.ratees = ratees;
    }

    public int getsNumber() {
        return rule.getsNumber();
    }

    public List<UserModel> getRatees() {
        return ratees;
    }

    public String getName() {
        return rule.getName();
    }

    public int getCdNumber() {
        return rule.getCdNumber();
    }

    public int getbNumber() {
        return rule.getbNumber();
    }

    public int getaNumber() {
        return rule.getaNumber();
    }
}
