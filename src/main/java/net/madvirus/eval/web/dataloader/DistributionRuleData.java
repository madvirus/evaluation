package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.query.user.UserModel;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class DistributionRuleData {

    public static Comparator<UserModel> userModelNameComparator = new Comparator<UserModel>() {
        @Override
        public int compare(UserModel o1, UserModel o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    private Map<UserModel, FirstRaterRuleData> firstRaterRules = new TreeMap<>(userModelNameComparator);

    public void addRule(FirstRaterRuleData firstRaterRuleData) {
        firstRaterRules.put(firstRaterRuleData.getFirstRater(), firstRaterRuleData);
    }

    public Collection<FirstRaterRuleData> getRuleDataList() {
        return firstRaterRules.values();
    }

}
