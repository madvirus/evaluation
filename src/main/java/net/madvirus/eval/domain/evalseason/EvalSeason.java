package net.madvirus.eval.domain.evalseason;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.madvirus.eval.api.RateeMapping;
import net.madvirus.eval.api.evalseaon.*;
import net.madvirus.eval.command.evalseason.OpenEvaluationCommand;
import net.madvirus.eval.command.evalseason.StartColleagueEvalCommand;
import net.madvirus.eval.util.PairValues;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@JsonIgnoreProperties("eventContainer")
public class EvalSeason extends AbstractAnnotatedAggregateRoot<String> {
    @AggregateIdentifier
    private String id;
    private String name;
    private boolean opened;
    private Date creationDate;
    private boolean colleagueEvalutionStarted;

    private Mappings mappings;
    private Map<String, List<DistributionRule>> rulesMap;

    public EvalSeason() {
    }

    public EvalSeason(String id, String name) {
        apply(new EvalSeasonCreatedEvent(id, name, new Date()));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isOpened() {
        return opened;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public boolean isColleagueEvalutionStarted() {
        return colleagueEvalutionStarted;
    }

    public boolean containsRatee(String rateeId) {
        return mappings.containsRatee(rateeId);
    }

    public RateeMapping getRateeMapping(String userId) {
        return mappings.getRateeMapping(userId);
    }

    @EventSourcingHandler
    public void on(EvalSeasonCreatedEvent event) {
        this.id = event.getEvalSeasonId();
        this.name = event.getName();
        this.opened = false;
        this.creationDate = event.getCreationDate();
        this.mappings = new Mappings(this.id);
        this.rulesMap = new HashMap<>();
    }

    public void updateMapping(List<RateeMapping> newRateeMappings, Consumer<List<String>> rateeAddedFirstRaterConsumer) {
        updateDistributionRuleIfChangeFirstRater(newRateeMappings, rateeAddedFirstRaterConsumer);

        MappingUpdatedEvent mappingUpdatedEvent = mappings.updateMapping(newRateeMappings);
        apply(mappingUpdatedEvent);
    }

    private void updateDistributionRuleIfChangeFirstRater(List<RateeMapping> newRateeMappings, Consumer<List<String>> rateeAddedFirstRaterConsumer) {
        PairValues<Map<String, List<String>>, Map<String, List<String>>> values = mappings.rateeChangedFirstRater(newRateeMappings);
        Map<String, List<String>> rateeRemovedFirstRater = values.v1;
        Map<String, List<String>> rateeAddedFirstRater = values.v2;

        if (!rateeRemovedFirstRater.isEmpty()) {
            for (String firstRaterId : rateeRemovedFirstRater.keySet()) {
                if (rulesMap.containsKey(firstRaterId)) {
                    List<DistributionRule> oldDistRules = rulesMap.get(firstRaterId);
                    List<DistributionRule> newDistRules = new ArrayList<>();
                    List<String> removedRatees = rateeRemovedFirstRater.get(firstRaterId);
                    oldDistRules.forEach(rule -> {
                        if (!rule.hasRatees(removedRatees)) {
                            newDistRules.add(rule);
                        }
                    });
                    if (oldDistRules.size() != newDistRules.size()) {
                        apply(new DistributionRuleUpdatedEvent(this.id, firstRaterId, newDistRules));
                    }
                }
            }
        }
        if (!rateeAddedFirstRater.isEmpty()) {
            rateeAddedFirstRaterConsumer.accept(new ArrayList(rateeAddedFirstRater.keySet()));
        }
    }

    @EventSourcingHandler
    public void on(MappingUpdatedEvent event) {
        mappings.on(event);
    }

    @CommandHandler
    public void handle(OpenEvaluationCommand command) {
        if (opened) throw new AlreadyEvaluationOpenedException();
        apply(new EvaluationOpenedEvent(id));
    }

    @EventSourcingHandler
    public void on(EvaluationOpenedEvent event) {
        this.opened = true;
    }

    public void deleteMapping(List<String> rateeIds) {
        Optional<MappingDeletedEvent> eventOp = mappings.deleteMapping(rateeIds);
        eventOp.ifPresent(event -> apply(event));
    }

    @EventSourcingHandler
    public void on(MappingDeletedEvent event) {
        mappings.on(event);
    }

    @CommandHandler
    public void handle(StartColleagueEvalCommand command) {
        if (!opened) throw new EvalSeasonNotYetOpenedException();
        if (colleagueEvalutionStarted) throw new ColleagueEvalAlreadyStartedException();
        apply(new ColleagueEvalStartedEvent(this.id));
    }

    @EventSourcingHandler
    public void on(ColleagueEvalStartedEvent event) {
        colleagueEvalutionStarted = true;
    }

    public boolean containsColleagueRater(String rateeId, String raterId) {
        return mappings.containsColleagueRater(rateeId, raterId);
    }

    public void updateDistributionRule(String firstRaterId, List<DistributionRule> rules) {
        checkDiscreteRatee(rules);
        for (DistributionRule rule : rules) {
            checkRule(firstRaterId, rule);
        }
        apply(new DistributionRuleUpdatedEvent(this.id, firstRaterId, rules));
    }

    private void checkDiscreteRatee(List<DistributionRule> rules) {
        if (rules.size() < 2) return;
        Set<String> set = new HashSet<>();
        List<String> idBelongToManyRules = new ArrayList<>();
        for (DistributionRule rule : rules) {
            for (String rateeId : rule.getRateeIds()) {
                if (set.contains(rateeId)) {
                    idBelongToManyRules.add(rateeId);
                } else {
                    set.add(rateeId);
                }
            }
        }
        if (!idBelongToManyRules.isEmpty()) {
            throw new IdBelongToManyRulesException(idBelongToManyRules);
        }
    }

    private void checkRule(String firstRaterId, DistributionRule rule) {
        if (!rule.sameRateeNumberAndGradeCountSum()) {
            throw new BasGradeCountRuleException();
        }
        for (String rateeId : rule.getRateeIds()) {
            RateeMapping rateeMapping = mappings.getRateeMapping(rateeId);
            if (rateeMapping == null) {
                throw new RateeNotFoundException(rateeId);
            }
            if (!firstRaterId.equals(rateeMapping.getFirstRaterId())) {
                throw new NotMatchingFirstRaterException(
                        rateeId, firstRaterId
                );
            }
        }
    }

    @EventSourcingHandler
    public void on(DistributionRuleUpdatedEvent event) {
        if (event.getRules().isEmpty()) {
            rulesMap.remove(event.getFirstRaterId());
        } else {
            rulesMap.put(event.getFirstRaterId(), event.getRules());
        }
    }

    public <T> T populateRuleData(String firstRaterId, Function<List<DistributionRule>, T> creator) {
        return creator.apply(rulesMap.get(firstRaterId));
    }
}
