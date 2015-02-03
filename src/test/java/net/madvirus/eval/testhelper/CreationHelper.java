package net.madvirus.eval.testhelper;

import net.madvirus.eval.api.personaleval.*;
import net.madvirus.eval.springconfig.SecurityConfig;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class CreationHelper {
    public static UpdateSelfPerformanceEvalCommand updateSelfPerfEvalCommand(
            String personalEvalId, String evalSeasonId, String userId, boolean done, int... weights) {
        List<PerformanceItemAndSelfEval> evals = new ArrayList<>();
        for (int i = 1; i <= weights.length; i++) {
            evals.add(perfAndSelfEval("카테고리", "목표타입", "목표" + i, "결과" + i, weights[i - 1], "커멘트" + i, Grade.A));
        }
        UpdateSelfPerformanceEvalCommand command = new UpdateSelfPerformanceEvalCommand(
                personalEvalId, evalSeasonId, userId, evals, done
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
            String personalEvalId, String evalSeasonId, String userId, boolean done, boolean hasLeadership, boolean hasAm) {
        List<ItemEval> commons = IntStream.range(0, 5)
                .mapToObj(idx -> new ItemEval("공통 역량 본인 평가" + idx, Grade.A)).collect(Collectors.toList());
        List<ItemEval> leaderships = hasLeadership ?
                IntStream.range(0, 5).mapToObj(idx -> new ItemEval("리더십 역량 본인 평가" + idx, Grade.B)).collect(Collectors.toList()) :
                null;
        List<ItemEval> ams = hasAm ?
                IntStream.range(0, 4).mapToObj(idx -> new ItemEval("AM 역량 본인 평가" + idx, Grade.B)).collect(Collectors.toList()) :
                null;

        return new UpdateSelfCompetencyEvalCommand(
                personalEvalId, evalSeasonId, userId,
                new CompetencyEvalSet(commons, leaderships, ams),
                done);
    }

    private static SecurityConfig.CookieValueEncryptor encryptor = new SecurityConfig.CookieValueEncryptor("secretkey", "01234567890abcde");


    public static Cookie authCookie(String userId) throws UnsupportedEncodingException {
        return new Cookie(SecurityConfig.AUTHCOOKIENAME,
                encryptor.encrypt(URLEncoder.encode(userId, "UTF-8")));
    }
}
