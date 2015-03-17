package net.madvirus.eval.domain.personaleval;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.madvirus.eval.api.personaleval.*;
import net.madvirus.eval.api.personaleval.colleague.ColleagueCompetencyEvaluatedEvent;
import net.madvirus.eval.api.personaleval.first.*;
import net.madvirus.eval.api.personaleval.second.SecondCompetencyEvaluatedEvent;
import net.madvirus.eval.api.personaleval.second.SecondPerformanceEvaluatedEvent;
import net.madvirus.eval.api.personaleval.second.SecondTotalEvaluatedEvent;
import net.madvirus.eval.api.personaleval.self.SelfCompetencyEvaluatedEvent;
import net.madvirus.eval.api.personaleval.self.SelfPerformanceEvaluatedEvent;
import net.madvirus.eval.domain.evalseason.RateeType;
import net.madvirus.eval.domain.personaleval.operator.*;
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
    private Optional<TotalEval> secondTotalEval;
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
        this.secondTotalEval = Optional.empty();
        this.rateeType = event.getRateeType();

        this.firstRaterId = event.getFirstRaterId();
        this.secondRaterId = event.getSecondRaterId();
    }

    public SelfRaterOperator getSelfRaterOperator() {
        return new SelfRaterOperator(this);
    }

    public ColleagueRaterOperator getColleagueRaterOperator() {
        return new ColleagueRaterOperator(this);
    }

    public FirstRaterOperator getFirstRaterOperator() {
        return new FirstRaterOperator(this);
    }

    public SecondRaterOperator getSecondRaterOperator() {
        return new SecondRaterOperator(this);
    }

    public MappingOperator getMappingOperator() {
        return new MappingOperator(this, compeEval);
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

    @EventSourcingHandler
    public void on(ColleagueCompetencyEvaluatedEvent event) {
        compeEval.updateColleagueCompetencyEval(event.getColleagueRaterId(), event.getEvalSet());
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

    @EventSourcingHandler
    public void on(FirstTotalEvaluatedEvent event) {
        firstTotalEval = Optional.of(event.getTotalEval());
        if (event.getTotalEval().isDone()) {
            perfEval.updateFirstPerfEvalDone();
            compeEval.updateFirstCompeEvalDone();
        }
    }

    @EventSourcingHandler
    public void on(SecondPerformanceEvaluatedEvent event) {
        perfEval.updateSecondPerfEval(event.getItemEvals(), event.getTotalEval());
    }

    @EventSourcingHandler
    public void on(SecondCompetencyEvaluatedEvent event) {
        compeEval.updateSecondCompeEval(event.getEvalSet());
    }

    @EventSourcingHandler
    public void on(SecondTotalEvaluatedEvent event) {
        secondTotalEval = Optional.of(event.getTotalEval());
        if (event.getTotalEval().isDone()) {
            perfEval.updateSecondPerfEvalDone();
            compeEval.updateSecondCompeEvalDone();
        }
    }

    @EventSourcingHandler
    public void on(PersonalEvalDeletedEvent event) {
        markDeleted();
    }

    @EventSourcingHandler
    public void on(PersonalEvalFirstRaterChangedEvent event) {
        this.firstRaterId = event.getNewFirstRaterId();
    }

    @EventSourcingHandler
    public void on(PersonalEvalSecondRaterChangedEvent event) {
        this.secondRaterId = event.getNewSecondRaterId();
    }

    @EventSourcingHandler
    public void on(ColleagueEvalDeletedEvent event) {
        compeEval.removeColleagueRaterEval(event.getColleagueIds());
    }

    @EventSourcingHandler
    public void on(FirstDraftReturnedEvent event) {
        if (firstTotalEval.isPresent()) {
            TotalEval totalEval = firstTotalEval.get();
            if (totalEval.isDone()) {
                firstTotalEval = Optional.of(new TotalEval(totalEval.getComment(), totalEval.getGrade(), false));
                perfEval.returnFirstDraft();
                compeEval.returnFirstDraft();
            }
        }
    }

    @EventSourcingHandler
    public void on(RateeTypeUpdatedEvent event) {
        RateeType newRateeType = event.getRateeType();
        this.rateeType = newRateeType;
        compeEval.applyNewRateeType(newRateeType);
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

    public List<PerformanceItem> getPerformanceItems() {
        return perfEval.getItems();
    }

    public Optional<PerformanceEvalSet> getSelfPerfEvalSet() {
        return perfEval.getSelfEvalSet();
    }

    public boolean checkFirstRater(String firstRaterId) {
        // TODO EvalSeason을 통해 검사하도록 수정해야 하나?
        return this.firstRaterId != null && this.firstRaterId.equals(firstRaterId);
    }

    public boolean checkSecondRater(String secondRaterId) {
        return this.secondRaterId.equals(secondRaterId);
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

    public boolean isSelfEvalDone() {
        return isSelfPerfEvalDone() && isSelfCompeEvalDone();
    }

    public Optional<CompetencyEvalSet> getSelfCompeEvalSet() {
        return compeEval.getSelfEvalSet();
    }

    public boolean isColleagueCompeEvalDone(String colleagueId) {
        return compeEval.getColleagueEvalOf(colleagueId)
                .flatMap(evalSet -> Optional.of(evalSet.isDone()))
                .orElse(false);
    }

    public boolean hasColleagueCompeEval(String colleagueId) {
        return compeEval.getColleagueEvalOf(colleagueId).isPresent();
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
        return compeEval.getFirstEvalSet().flatMap(evalSet -> Optional.of(true)).orElse(false);
    }

    public boolean isFirstTotalEvalDone() {
        return isSelfEvalDone() && (isFirstEvalSkipTarget() || firstTotalEval.flatMap(totalEval -> Optional.of(totalEval.isDone())).orElse(false));
    }

    public Optional<PerformanceEvalSet> getSecondPerfEvalSet() {
        return perfEval.getSecondEvalSet();
    }

    public Optional<CompetencyEvalSet> getSecondCompeEvalSet() {
        return compeEval.getSecondEvalSet();
    }

    public Optional<TotalEval> getSecondTotalEval() {
        return secondTotalEval;
    }

    public boolean isSecondPerfEvalHad() {
        return perfEval.getSecondEvalSet().flatMap(evalSet -> Optional.of(true)).orElse(false);
    }

    public boolean isSecondCompeEvalHad() {
        return compeEval.getSecondEvalSet().flatMap(evalSet -> Optional.of(true)).orElse(false);
    }

    public boolean isSecondTotalEvalDone() {
        return secondTotalEval.flatMap(totalEval -> Optional.of(totalEval.isDone())).orElse(false);
    }

    public boolean isFirstEvalSkipTarget() {
        return firstRaterId == null;
    }

    public void delete() {
        apply(new PersonalEvalDeletedEvent(this.id));
    }

    public String getFirstRaterId() {
        return firstRaterId;
    }

    public String getSecondRaterId() {
        return secondRaterId;
    }

    public void returnToFirstDraft() {
        if (isSecondTotalEvalDone()) {
            throw new AlreadySecondEvalDoneException();
        }
        apply(new FirstDraftReturnedEvent(this.id));
    }

    public Double getFirstMark() {
        if (!(isFirstPerfEvalHad() && isFirstCompeEvalHad()) || isFirstEvalSkipTarget())
            return null;
        else
            return MarkCalculator.calculate(getRateeType(),
                    getFirstPerfEvalGrade(),
                    getAllCompeEvals().getFirstEvalSet().getCommonsAverage(),
                    getAllCompeEvals().getFirstEvalSet().getLeadershipAverage(),
                    getAllCompeEvals().getFirstEvalSet().getAmAverage()
            );
    }

    public Grade getFirstPerfEvalGrade() {
        return getFirstPerfEvalSet()
                .flatMap(eval -> Optional.of(eval.getTotalEval().getGrade())).orElse(null);
    }

    public Grade getFirstCompeEvalGrade() {
        return getFirstCompeEvalSet().flatMap(eval -> Optional.of(eval.getTotalEval().getGrade())).orElse(null);
    }

    public Grade getSecondPerfEvalGrade() {
        return getSecondPerfEvalSet().flatMap(eval -> Optional.of(eval.getTotalEval().getGrade())).orElse(null);
    }

    public Grade getSecondCompeEvalGrade() {
        return getSecondPerfEvalSet().flatMap(eval -> Optional.of(eval.getTotalEval().getGrade())).orElse(null);
    }

    public Double getSecondMark() {
        if (!(isSecondPerfEvalHad() && isSecondCompeEvalHad()))
            return null;
        else
            return MarkCalculator.calculate(getRateeType(),
                    getSecondPerfEvalGrade(),
                    getAllCompeEvals().getSecondEvalSet().getCommonsAverage(),
                    getAllCompeEvals().getSecondEvalSet().getLeadershipAverage(),
                    getAllCompeEvals().getSecondEvalSet().getAmAverage()
            );
    }
}
