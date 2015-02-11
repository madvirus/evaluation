package net.madvirus.eval.testhelper;

import net.madvirus.eval.api.evalseaon.RateeType;
import net.madvirus.eval.api.personaleval.*;
import net.madvirus.eval.api.personaleval.colleague.UpdateColleagueCompetencyEvalCommand;
import net.madvirus.eval.api.personaleval.first.FirstCompetencyEvaluatedEvent;
import net.madvirus.eval.api.personaleval.first.FirstPerformanceEvaluatedEvent;
import net.madvirus.eval.api.personaleval.first.UpdateFirstCompetencyEvalCommand;
import net.madvirus.eval.api.personaleval.self.SelfCompetencyEvaluatedEvent;
import net.madvirus.eval.api.personaleval.self.SelfPerformanceEvaluatedEvent;
import net.madvirus.eval.api.personaleval.self.UpdateSelfCompetencyEvalCommand;
import net.madvirus.eval.api.personaleval.self.UpdateSelfPerformanceEvalCommand;
import net.madvirus.eval.springconfig.SecurityConfig;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class CreationHelper {
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


    private static interface CreatorFunction<T> {

        T func(List<ItemEval> commons, List<ItemEval> leaderships, List<ItemEval> ams);
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

    private static SecurityConfig.CookieValueEncryptor encryptor = new SecurityConfig.CookieValueEncryptor("secretkey", "01234567890abcde");


    public static Cookie authCookie(String userId) throws UnsupportedEncodingException {
        return new Cookie(SecurityConfig.AUTHCOOKIENAME,
                encryptor.encrypt(URLEncoder.encode(userId, "UTF-8")));
    }

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


}
