package net.madvirus.eval.testhelper;

import net.madvirus.eval.domain.personaleval.PersonalEval;

public class PersonalEvalHelper {
    public static PersonalEval createPersonalEvalWithSelfDone(
            String evalSeasonId, String rateeId,
            boolean firstPerfCompeHad, boolean firstTotalDone) {
        PersonalEval rateeEval = new PersonalEval();
        rateeEval.on(EventHelper.personalEvalCreatedEvent(evalSeasonId, rateeId, "first", "second"));
        rateeEval.on(EventHelper.selfPerfEvaluatedEvent(evalSeasonId, rateeId, true));
        rateeEval.on(EventHelper.selfCompeEvaluatedEvent(evalSeasonId, rateeId, true));
        if (firstPerfCompeHad) {
            rateeEval.on(EventHelper.firstPerfEvaluatedEvent(evalSeasonId, rateeId));
            rateeEval.on(EventHelper.firstCompeEvaluatedEvent(evalSeasonId, rateeId, "first"));
        }
        if (firstTotalDone) {
            rateeEval.on(EventHelper.firstTotalEvaluatedEvent(evalSeasonId, true));
        }
        return rateeEval;
    }
    public static PersonalEval createPersonalEvalWithSelfDone(
            String evalSeasonId, String rateeId, String firstRaterId, String secondRaterId,
            boolean firstPerfCompeHad, boolean firstTotalDone) {
        PersonalEval rateeEval = new PersonalEval();
        rateeEval.on(EventHelper.personalEvalCreatedEvent(evalSeasonId, rateeId, firstRaterId, secondRaterId));
        rateeEval.on(EventHelper.selfPerfEvaluatedEvent(evalSeasonId, rateeId, true));
        rateeEval.on(EventHelper.selfCompeEvaluatedEvent(evalSeasonId, rateeId, true));
        if (firstPerfCompeHad) {
            rateeEval.on(EventHelper.firstPerfEvaluatedEvent(evalSeasonId, rateeId));
            rateeEval.on(EventHelper.firstCompeEvaluatedEvent(evalSeasonId, rateeId, firstRaterId));
        }
        if (firstTotalDone) {
            rateeEval.on(EventHelper.firstTotalEvaluatedEvent(evalSeasonId, true));
        }
        return rateeEval;
    }

    public static PersonalEval createPersonalEvalWithFirstDone(
            String evalSeasonId, String rateeId, boolean secondPerfCompeHad) {
        return createFirstDonePersonalEvalWithSecondEvalDoneOption(evalSeasonId, rateeId, secondPerfCompeHad, false);
    }


    public static PersonalEval createPersonalEvalWithFirstDoneAndNoSecondEval(
            String evalSeasonId, String rateeId, String firstRaterId) {
        return createFirstDonePersonalEvalWithSecondEvalDoneOption(evalSeasonId, rateeId, firstRaterId, false, false);
    }

    public static PersonalEval createPersonalEvalWithFirstDone(
            String evalSeasonId, String rateeId, String firstRaterId, boolean secondPerfCompeHad) {
        return createFirstDonePersonalEvalWithSecondEvalDoneOption(evalSeasonId, rateeId, firstRaterId, secondPerfCompeHad, false);
    }

    public static PersonalEval createFirstDonePersonalEvalWithSecondEvalDoneOption(
            String evalSeasonId, String rateeId,
            boolean secondPerfCompeHad, boolean secondTotalDoneOption) {
        return createFirstDonePersonalEvalWithSecondEvalDoneOption(evalSeasonId, rateeId, "first", secondPerfCompeHad, secondTotalDoneOption);
    }

    public static PersonalEval createFirstDonePersonalEvalWithSecondEvalDoneOption(
            String evalSeasonId, String rateeId, String firstRaterId,
            boolean secondPerfCompeHad, boolean secondTotalDoneOption) {
        PersonalEval rateeEval = new PersonalEval();
        rateeEval.on(EventHelper.personalEvalCreatedEvent(evalSeasonId, rateeId, firstRaterId, "second"));
        rateeEval.on(EventHelper.selfPerfEvaluatedEvent(evalSeasonId, rateeId, true));
        rateeEval.on(EventHelper.selfCompeEvaluatedEvent(evalSeasonId, rateeId, true));
        rateeEval.on(EventHelper.firstPerfEvaluatedEvent(evalSeasonId, rateeId));
        rateeEval.on(EventHelper.firstCompeEvaluatedEvent(evalSeasonId, rateeId, firstRaterId));
        rateeEval.on(EventHelper.firstTotalEvaluatedEvent(evalSeasonId, true));
        if (secondPerfCompeHad) {
            rateeEval.on(EventHelper.secondPerfEvaluatedEvent(evalSeasonId, rateeId));
            rateeEval.on(EventHelper.secondCompeEvaluatedEvent(evalSeasonId, rateeId, firstRaterId));
        }
        if (secondTotalDoneOption) {
            rateeEval.on(EventHelper.secondTotalEvaluatedEvent(evalSeasonId, true));
        }
        return rateeEval;
    }

    public static PersonalEval createPersonalEvalWithSecondDone(String evalSeasonId, String rateeId) {
        return createFirstDonePersonalEvalWithSecondEvalDoneOption(evalSeasonId, rateeId, true, true);
    }
}
