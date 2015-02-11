package net.madvirus.eval.api.personaleval;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.madvirus.eval.api.evalseaon.RateeType;
import net.madvirus.eval.api.personaleval.colleague.ColleagueCompetencyEvaluatedEvent;
import net.madvirus.eval.api.personaleval.colleague.ColleagueRaterOperator;
import net.madvirus.eval.api.personaleval.first.*;
import net.madvirus.eval.api.personaleval.self.*;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;

import java.util.List;
import java.util.Optional;

@JsonIgnoreProperties("eventContainer")
public class PersonalEval extends AbstractAnnotatedAggregateRoot<String> {
    @AggregateIdentifier
    private String id; // EvalSeason ID + "-" + 개인ID
    private String evalSeasonId;
    private String userId;
    private PerformanceEvaluation perfEval;
    private CompetencyEvaluation compeEval;
    private Optional<TotalEval> firstTotalEval;
    private RateeType rateeType;
    private String firstRaterId;
    private String secondRaterId;

    public PersonalEval() {
    }

    public PersonalEval(String evalSeasonId, String userId, RateeType rateeType, String firstRaterId, String secondRaterId) {
        apply(new PersonalEvaluationCreatedEvent(
                createId(evalSeasonId, userId), evalSeasonId, userId, rateeType,
                firstRaterId, secondRaterId));
    }

    public static String createId(String evalSeasonId, String userId) {
        return evalSeasonId + "-" + userId;
    }

    @EventSourcingHandler
    public void on(PersonalEvaluationCreatedEvent event) {
        this.id = event.getPersonalEvalId();
        this.evalSeasonId = event.getEvalSeasonId();
        this.userId = event.getUserId();
        this.perfEval = new PerformanceEvaluation();
        this.compeEval = new CompetencyEvaluation();
        this.firstTotalEval = Optional.empty();
        this.rateeType = event.getRateeType();

        this.firstRaterId = event.getFirstRaterId();
        this.secondRaterId = event.getSecondRaterId();
    }

    public SelfRaterOperator getSelfRaterOperator() {
        return new SelfRaterOperator(this);
    }

    @EventSourcingHandler
    public void on(SelfPerformanceEvaluatedEvent event) {
        List<PerformanceItemAndSelfEval> perfItemAndSelfEvals = event.getPerformanceItemAndSelfEval();
        perfEval.updateSelfPerfEval(perfItemAndSelfEvals, event.isDone());
    }

    @EventSourcingHandler
    public void on(SelfCompetencyEvaluatedEvent event) {
        compeEval.updateSelfEval(event.getEvalSet());
    }

    public ColleagueRaterOperator getColleagueRaterOperator() {
        return new ColleagueRaterOperator(this);
    }

    @EventSourcingHandler
    public void on(ColleagueCompetencyEvaluatedEvent event) {
        compeEval.updateColleagueCompetencyEval(event.getColleagueRaterId(), event.getEvalSet());
    }

    public FirstRaterOperator getFirstRaterOperator() {
        return new FirstRaterOperator(this);
    }

    @EventSourcingHandler
    public void on(FirstPerformanceEvaluatedEvent event) {
        perfEval.updateFirstPerfEval(event.getItemEvals(), event.getTotalEval());
    }

    @EventSourcingHandler
    public void on(FirstCompetencyEvaluatedEvent event) {
        compeEval.updateFirstCompeEval(event.getEvalSet());
    }

    @EventSourcingHandler
    public void on(SelfPerformanceEvalRejectedEvent event) {
        perfEval.rejectSelfEval();
    }

    @EventSourcingHandler
    public void on(SelfCompetencyEvalRejectedEvent event) {
        compeEval.rejectSelfEval();
    }


    public String getId() {
        return id;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public String getUserId() {
        return userId;
    }

    public RateeType getRateeType() {
        return rateeType;
    }


    public List<PerformanceItemAndSelfEval> getPerfItemAndSelfEvals() {
        return perfEval.getItemAndSelfEvals();
    }

    public boolean checkFirstRater(String firstRaterId) {
        // TODO EvalSeason을 통해 검사하도록 수정해야 하나?
        return this.firstRaterId.equals(firstRaterId);
    }

    public List<PerformanceItemAndAllEval> getPerfItemAndAllEvals() {
        return perfEval.getItemAndAllEvals();
    }

    public Optional<CompetencyEvalSet> getColleagueCompeEvalSet(String colleagueId) {
        return compeEval.getColleagueEvalOf(colleagueId);
    }

    public AllCompeEvals getAllCompeEvals() {
        return compeEval.getAllCompeEvals();
    }

    public void applyEvent(Object event) {
        apply(event);
    }

    public boolean isSelfPerfEvalDone() {
        return perfEval.getSelfEvalSet()
                .flatMap(evalSet -> Optional.of(evalSet.isDone())).orElse(false);
    }

    public boolean isSelfCompeEvalDone() {
        return compeEval.getSelfEvalSet()
                .flatMap(evalSet -> Optional.of(evalSet.isDone())).orElse(false);
    }

    public Optional<CompetencyEvalSet> getSelfCompeEvalSet() {
        return compeEval.getSelfEvalSet();
    }

    public boolean isColleagueCompeEvalDone(String colleagueId) {
        return compeEval.getColleagueEvalOf(colleagueId)
                .flatMap(evalSet -> Optional.of(evalSet.isDone()))
                .orElse(false);
    }

    public boolean isFirstEvalDone() {
        return firstTotalEval.flatMap(eval -> Optional.of(eval.isDone())).orElse(false);
    }

    public Optional<PerformanceEvalSet> getFirstPerfEvalSet() {
        return perfEval.getFirstEvalSet();
    }


    public Optional<CompetencyEvalSet> getFirstCompeEvalSet() {
        return compeEval.getFirstEvalSet();
    }

    public Optional<TotalEval> getFirstTotalEval() {
        return firstTotalEval;
    }

    public boolean isFirstPerfEvalHad() {
        return perfEval.getFirstEvalSet().flatMap(evalSet -> Optional.of(true)).orElse(false);
    }

    public boolean isFirstCompeEvalHad() {
        return perfEval.getFirstEvalSet().flatMap(evalSet -> Optional.of(true)).orElse(false);
    }

    public void updateFirstTotalEval(TotalEval totalEval) {
        apply(new FirstTotalEvaluatedEvent(id, totalEval));
    }

    @EventSourcingHandler
    public void on(FirstTotalEvaluatedEvent event) {
        firstTotalEval = Optional.of(event.getTotalEval());
        if (event.getTotalEval().isDone()) {
            perfEval.updateFirstPerfEvalDone();
            compeEval.updateFirstCompeEvalDone();
        }
    }
}
