package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.domain.evalseason.DistributionRule;
import net.madvirus.eval.query.user.UserModel;

import java.util.*;

public class FirstRaterRuleData {
    private UserModel firstRater;
    private List<DistributionRule> ruleList;
    private Map<String, UserModel> rateeMap;

    public FirstRaterRuleData(UserModel firstRater, Set<UserModel> ratees, List<DistributionRule> ruleList) {
        this.firstRater = firstRater;
        this.rateeMap = new HashMap<>();
        ratees.forEach(userModel -> {
            rateeMap.put(userModel.getId(), userModel);
        });
        this.ruleList = ruleList;
    }

    public UserModel getFirstRater() {
        return firstRater;
    }

    public List<UserModel> getRatees() {
        ArrayList<UserModel> userModels = new ArrayList<>(rateeMap.values());
        userModels.sort(DistributionRuleData.userModelNameComparator);
        return userModels;
    }

    public List<RuleData> getRuleList() {
        List<RuleData> ruleDatas = new ArrayList<>();
        if (ruleList != null) {
            for (DistributionRule rule : ruleList) {
                List<UserModel> ratees = new ArrayList<>();
                for (String rateeId : rule.getRateeIds()) {
                    ratees.add(rateeMap.get(rateeId));
                }
                ratees.sort(DistributionRuleData.userModelNameComparator);
                ruleDatas.add(new RuleData(rule, ratees));
            }
        }
        return ruleDatas;
    }
}
