package net.madvirus.eval.testhelper;

import net.madvirus.eval.api.personaleval.Grade;
import net.madvirus.eval.command.personaleval.colleague.UpdateColleagueCompetencyEvalCommand;
import net.madvirus.eval.command.personaleval.common.TotalEvalUpdate;
import net.madvirus.eval.command.personaleval.first.UpdateFirstCompetencyEvalCommand;
import net.madvirus.eval.command.personaleval.first.UpdateFirstPerformanceEvalCommand;
import net.madvirus.eval.command.personaleval.first.UpdateFirstTotalEvalCommand;
import net.madvirus.eval.command.personaleval.second.UpdateSecondCompetencyEvalCommand;
import net.madvirus.eval.command.personaleval.second.UpdateSecondPerformanceEvalCommand;
import net.madvirus.eval.command.personaleval.second.UpdateSecondTotalEvalCommand;
import net.madvirus.eval.command.personaleval.self.UpdateSelfCompetencyEvalCommand;
import net.madvirus.eval.command.personaleval.self.UpdateSelfPerformanceEvalCommand;
import net.madvirus.eval.domain.personaleval.CompetencyEvalSet;
import net.madvirus.eval.domain.personaleval.ItemEval;
import net.madvirus.eval.domain.personaleval.PerformanceItem;
import net.madvirus.eval.domain.personaleval.PerformanceItemAndSelfEval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.madvirus.eval.testhelper.CompetencyHelper.createCompetencyEvalWith;

public abstract class CommandHelper {
    public static UpdateSelfPerformanceEvalCommand updateSelfPerfEvalCommand(
            String evalSeasonId, String userId, boolean done, int... weights) {
        List<PerformanceItemAndSelfEval> evals = new ArrayList<>();
        for (int i = 1; i <= weights.length; i++) {
            evals.add(perfAndSelfEval("카테고리", "목표타입", "목표" + i, "결과" + i, weights[i - 1], "커멘트" + i, Grade.A));
        }
        UpdateSelfPerformanceEvalCommand command = new UpdateSelfPerformanceEvalCommand(
                evalSeasonId, userId, evals, done
        );
        return command;
    }

    public static PerformanceItemAndSelfEval perfAndSelfEval(String category, String goalType, String goal, String result, int weight, String comment, Grade grade) {
        return new PerformanceItemAndSelfEval(
                new PerformanceItem(category, goalType, goal, result, weight),
                new ItemEval(comment, grade)
        );
    }


    public static UpdateSelfCompetencyEvalCommand updateSelfCompeEvalCommand(
            String evalSeasonId, String userId, boolean done, boolean hasLeadership, boolean hasAm) {
        return createCompetencyEvalWith("본인", hasLeadership, hasAm,
                (List<ItemEval> commons, List<ItemEval> leaderships, List<ItemEval> ams) -> {
                    return new UpdateSelfCompetencyEvalCommand(
                            evalSeasonId, userId,
                            new CompetencyEvalSet(commons, leaderships, ams, ItemEval.empty(), done));

                });
    }


    public static UpdateColleagueCompetencyEvalCommand updateColleagueCompeEvalCommand(
            String evalSeasonId, String rateeId, String raterId, boolean done, boolean hasLeadership, boolean hasAm) {
        return createCompetencyEvalWith("본인", hasLeadership, hasAm,
                (List<ItemEval> commons, List<ItemEval> leaderships, List<ItemEval> ams) -> {
                    return new UpdateColleagueCompetencyEvalCommand(evalSeasonId, rateeId, raterId,
                            new CompetencyEvalSet(commons, leaderships, ams,
                                    new ItemEval("동료 종합 평가", null),
                                    done));
                });
    }

    public static UpdateFirstPerformanceEvalCommand updateFirstPerfEvalCommandWithRater(String raterId) {
        return new UpdateFirstPerformanceEvalCommand(
                "EVAL2014", "ratee", raterId,
                Arrays.asList(new ItemEval("first comment", Grade.B)),
                new ItemEval("total comment", Grade.B));
    }

    public static UpdateSecondPerformanceEvalCommand updateSecondPerfEvalCommandWithRater(String raterId) {
        return new UpdateSecondPerformanceEvalCommand(
                "EVAL2014", "ratee", raterId,
                Arrays.asList(new ItemEval("first comment", Grade.B)),
                new ItemEval("total comment", Grade.B));
    }

    public static UpdateFirstCompetencyEvalCommand updateFirstCompeEvalCommand(
            String evalSeasonId, String rateeId, String raterId, boolean done, boolean hasLeadership, boolean hasAm) {
        return createCompetencyEvalWith("1차", hasLeadership, hasAm,
                (List<ItemEval> commons, List<ItemEval> leaderships, List<ItemEval> ams) -> {
                    return new UpdateFirstCompetencyEvalCommand(evalSeasonId, rateeId, raterId,
                            new CompetencyEvalSet(commons, leaderships, ams,
                                    new ItemEval("1차 종합 평가", Grade.A),
                                    done));
                });
    }

    public static UpdateSecondCompetencyEvalCommand updateSecondCompeEvalCommand(
            String evalSeasonId, String rateeId, String raterId, boolean done, boolean hasLeadership, boolean hasAm) {
        return createCompetencyEvalWith("2차", hasLeadership, hasAm,
                (List<ItemEval> commons, List<ItemEval> leaderships, List<ItemEval> ams) -> {
                    return new UpdateSecondCompetencyEvalCommand(evalSeasonId, rateeId, raterId,
                            new CompetencyEvalSet(commons, leaderships, ams,
                                    new ItemEval("2차 종합 평가", Grade.A),
                                    done));
                });
    }

    public static UpdateFirstTotalEvalCommand updateFirstTotalEvalCommand(String evalSeasonId, String raterId, boolean done, String ... rateeIds) {
        List<TotalEvalUpdate> evalUpdates = new ArrayList<>();
        for (int i = 0 ; i < rateeIds.length ; i++) {
            evalUpdates.add(new TotalEvalUpdate(rateeIds[i], "comment" + (i+1), Grade.A));
        }
        return new UpdateFirstTotalEvalCommand(evalSeasonId, raterId, evalUpdates, done);
    }

    public static UpdateSecondTotalEvalCommand updateSecondTotalEvalCommand(String evalSeasonId, String raterId, boolean done, String ... rateeIds) {
        List<TotalEvalUpdate> evalUpdates = new ArrayList<>();
        for (int i = 0 ; i < rateeIds.length ; i++) {
            evalUpdates.add(new TotalEvalUpdate(rateeIds[i], "comment" + (i+1), Grade.A));
        }
        return new UpdateSecondTotalEvalCommand(evalSeasonId, raterId, evalUpdates, done);
    }

}
