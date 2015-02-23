package net.madvirus.eval.testhelper;

import net.madvirus.eval.api.personaleval.Grade;
import net.madvirus.eval.api.personaleval.PersonalEvaluationCreatedEvent;
import net.madvirus.eval.api.personaleval.first.FirstCompetencyEvaluatedEvent;
import net.madvirus.eval.api.personaleval.first.FirstPerformanceEvaluatedEvent;
import net.madvirus.eval.api.personaleval.first.FirstTotalEvaluatedEvent;
import net.madvirus.eval.api.personaleval.second.SecondCompetencyEvaluatedEvent;
import net.madvirus.eval.api.personaleval.second.SecondPerformanceEvaluatedEvent;
import net.madvirus.eval.api.personaleval.second.SecondTotalEvaluatedEvent;
import net.madvirus.eval.api.personaleval.self.SelfCompetencyEvaluatedEvent;
import net.madvirus.eval.api.personaleval.self.SelfPerformanceEvaluatedEvent;
import net.madvirus.eval.domain.evalseason.RateeType;
import net.madvirus.eval.domain.personaleval.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.madvirus.eval.testhelper.CompetencyHelper.createCompetencyEvalWith;

public abstract class EventHelper {
    public static PersonalEvaluationCreatedEvent personalEvalCreatedEvent(String evalSeason, String rateeId, String first, String second) {
        return new PersonalEvaluationCreatedEvent(PersonalEval.createId(evalSeason, rateeId), evalSeason, rateeId, RateeType.MEMBER, first, second);
    }

    public static SelfPerformanceEvaluatedEvent selfPerfEvaluatedEvent(String evalSeasonId, String rateeId, boolean done) {
        return new SelfPerformanceEvaluatedEvent(
                PersonalEval.createId(evalSeasonId, rateeId), done,
                Arrays.asList(
                        new PerformanceItemAndSelfEval(new PerformanceItem("category", "goalType", "goal", "result", 100), new ItemEval("comment", Grade.A)))
        );
    }

    public static SelfCompetencyEvaluatedEvent selfCompeEvaluatedEvent(String evalSeasonId, String rateeId, boolean done) {
        return new SelfCompetencyEvaluatedEvent(
                PersonalEval.createId(evalSeasonId, rateeId),
                createCompetencyEvalWith("본인", false, false,
                        (List<ItemEval> commons, List<ItemEval> leaderships, List<ItemEval> ams) -> {
                            return new CompetencyEvalSet(commons, leaderships, ams, null, done);
                        })
        );
    }

    public static FirstPerformanceEvaluatedEvent firstPerfEvaluatedEvent(String evalSeasonId, String rateeId) {
        return new FirstPerformanceEvaluatedEvent(
                PersonalEval.createId(evalSeasonId, rateeId),
                null,
                new ItemEval("comment", Grade.A)
        );
    }

    public static FirstCompetencyEvaluatedEvent firstCompeEvaluatedEvent(String evalSeasonId, String rateeId, String firstRater) {
        return new FirstCompetencyEvaluatedEvent(
                PersonalEval.createId(evalSeasonId, rateeId),
                firstRater,
                new CompetencyEvalSet(
                        Collections.emptyList(), null, null,
                        new ItemEval("comment", Grade.A),
                        false)
        );
    }

    public static FirstTotalEvaluatedEvent firstTotalEvaluatedEvent(String evalSeasonId, boolean done) {
        return new FirstTotalEvaluatedEvent(evalSeasonId,
                new TotalEval("second total comment", Grade.A, done));
    }

    public static SecondPerformanceEvaluatedEvent secondPerfEvaluatedEvent(String evalSeasonId, String rateeId) {
        return new SecondPerformanceEvaluatedEvent(
                PersonalEval.createId(evalSeasonId, rateeId),
                null,
                new ItemEval("comment", Grade.A)
        );
    }

    public static SecondCompetencyEvaluatedEvent secondCompeEvaluatedEvent(String evalSeasonId, String rateeId, String firstRater) {
        return new SecondCompetencyEvaluatedEvent(
                PersonalEval.createId(evalSeasonId, rateeId),
                firstRater,
                new CompetencyEvalSet(
                        Collections.emptyList(), null, null,
                        new ItemEval("comment", Grade.A),
                        false)
        );
    }

    public static SecondTotalEvaluatedEvent secondTotalEvaluatedEvent(String evalSeasonId, boolean done) {
        return new SecondTotalEvaluatedEvent(evalSeasonId,
                new TotalEval("second total comment", Grade.A, done));
    }
}
