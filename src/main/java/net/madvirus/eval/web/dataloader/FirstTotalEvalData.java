package net.madvirus.eval.web.dataloader;

import org.apache.commons.collections.map.HashedMap;

import java.util.*;

public class FirstTotalEvalData {
    private FirstRaterRuleData firstRaterRuleData;
    private List<FirstTotalEvalSummary> evalSummaryList;

    private List<FirstTotalEvalSummary> freeGradeRateeList;
    private List<RuleGradeRateeSet> ruleGradeRateeSets;

    public FirstTotalEvalData(FirstRaterRuleData firstRaterRuleData, List<FirstTotalEvalSummary> evalSummaryList) {
        this.firstRaterRuleData = firstRaterRuleData;
        this.evalSummaryList = evalSummaryList;
        Map<String, FirstTotalEvalSummary> evalSummaryMap = new HashMap<>();
        evalSummaryList.forEach(evalSummary -> evalSummaryMap.put(evalSummary.getRatee().getId(), evalSummary));


        createRuleGradeRateeSets(evalSummaryMap, firstRaterRuleData);
        createFreeGradeRateeList(evalSummaryMap, firstRaterRuleData, evalSummaryList);
    }

    private void createRuleGradeRateeSets(Map<String, FirstTotalEvalSummary> evalSummaryMap, FirstRaterRuleData firstRaterRuleData) {
        List<RuleGradeRateeSet> result = new ArrayList<>();
        for (RuleData ruleData : firstRaterRuleData.getRuleList()) {
            List<FirstTotalEvalSummary> summaryList = new ArrayList<>();
            ruleData.getRatees().forEach(ratee -> summaryList.add(evalSummaryMap.get(ratee.getId())));
            result.add(new RuleGradeRateeSet(ruleData, summaryList));
        }
        this.ruleGradeRateeSets = result;

    }

    private void createFreeGradeRateeList(Map<String, FirstTotalEvalSummary> evalSummaryMap, FirstRaterRuleData firstRaterRuleData, List<FirstTotalEvalSummary> evalSummaryList) {
        Map<String, FirstTotalEvalSummary> freeGradeSummaryMap = new HashedMap(evalSummaryMap);
        firstRaterRuleData.getRuleList().forEach(
                ruleData ->
                        ruleData.getRatees().forEach(ratee -> freeGradeSummaryMap.remove(ratee.getId()))
        );
        ArrayList<FirstTotalEvalSummary> result = new ArrayList<>(freeGradeSummaryMap.values());
        result.sort(new Comparator<FirstTotalEvalSummary>() {
            @Override
            public int compare(FirstTotalEvalSummary o1, FirstTotalEvalSummary o2) {
                return o1.getRatee().getName().compareTo(o2.getRatee().getName());
            }
        });

        this.freeGradeRateeList = result;
    }

    public List<RuleGradeRateeSet> getRuleGradeRateeSets() {
        return ruleGradeRateeSets;
    }

    public List<FirstTotalEvalSummary> getFreeGradeRateeList() {
        return freeGradeRateeList;
    }

    public boolean isHasFreeGradeRateeList() {
        return !freeGradeRateeList.isEmpty();
    }

    public List<FirstTotalEvalSummary> getEvalSummaries() {
        return evalSummaryList;
    }

    public boolean isAllPerfEvalHad() {
        return evalSummaryList.stream().allMatch(x -> x.isFirstPerfEvalHad());
    }

    public boolean isAllCompeEvalHad() {
        return evalSummaryList.stream().allMatch(x -> x.isFirstCompeEvalHad());
    }

    public boolean isTotalEvalAvailable() {
        return isAllPerfEvalHad() && isAllCompeEvalHad();
    }

    public boolean isTotalEvalDone() {
        return evalSummaryList.stream().allMatch(x -> x.isFirstTotalEvalDone());
    }
}
