package net.madvirus.eval.command.personaleval;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.madvirus.eval.api.personaleval.*;
import net.madvirus.eval.web.dataloader.PersonalEvalState;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;

import java.util.List;

@JsonIgnoreProperties("eventContainer")
public class PersonalEval extends AbstractAnnotatedAggregateRoot<String> implements PersonalEvalState {
    @AggregateIdentifier
    private String id; // EvalSeason ID + "-" + 개인ID
    private String evalSeasonId;
    private String userId;
    private PerformanceEvaluation perfEval;
    private CompetencyEvaluation compeEval;
    private boolean started;

    protected PersonalEval() {
    }

    public PersonalEval(String evalSeasonId, String userId) {
        apply(new PersonalEvaluationCreatedEvent(createId(evalSeasonId, userId), evalSeasonId, userId));
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
        this.started = true;
    }

    public void updateSelfPerfomanceEvaluation(UpdateSelfPerformanceEvalCommand cmd) {
        if (cmd.isDone()) {
            if (cmd.getWeightSum() != 100) {
                throw new InvalidWeightSumException();
            }
        }
        apply(new SelfPerformanceEvaluatedEvent(id, cmd.isDone(),
                cmd.getItemAndEvals()));
    }

    @EventSourcingHandler
    public void on(SelfPerformanceEvaluatedEvent event) {
        List<PerformanceItemAndSelfEval> perfItemAndSelfEvals = event.getPerformanceItemAndSelfEval();
        perfEval.updateSelfPerfEval(perfItemAndSelfEvals, event.isDone());
    }

    public void updateSelfCompetencyEvaluation(UpdateSelfCompetencyEvalCommand cmd) {
        // TODO 역량 평가 타입 체크
        apply(new SelfCompetencyEvaluatedEvent(id, cmd.isDone(), cmd.getEvalSet()));
    }

    @EventSourcingHandler
    public void on(SelfCompetencyEvaluatedEvent event) {
        compeEval.updateSelfEval(event.getEvalSet(), event.isDone());
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public boolean isSelfPerfEvalDone() {
        return perfEval.isSelfEvalDone();
    }

    @Override
    public boolean isSelfCompeEvalDone() {
        return compeEval.isSelfEvalDone();

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

    public List<PerformanceItemAndSelfEval> getPerfItemAndSelfEvals() {
        return perfEval.getItemAndSelfEvals();
    }

    public CompetencyEvalSet getSelfCompeEvalSet() {
        return compeEval.getSelfEvalSet();
    }
}
