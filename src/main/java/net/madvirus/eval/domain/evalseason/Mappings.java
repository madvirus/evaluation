package net.madvirus.eval.domain.evalseason;

import net.madvirus.eval.api.RateeMapping;
import net.madvirus.eval.api.evalseaon.MappingDeletedEvent;
import net.madvirus.eval.api.evalseaon.MappingUpdatedEvent;
import net.madvirus.eval.command.evalseason.DeleteMappingCommand;
import net.madvirus.eval.command.evalseason.UpdateMappingCommand;
import net.madvirus.eval.util.PairValues;
import org.apache.commons.collections.map.HashedMap;

import java.util.*;

public class Mappings { //extends AbstractAnnotatedEntity {
    private Map<String, RateeMapping> mappings = new HashedMap();

    private String parentId;

    protected Mappings() {
    }

    public Mappings(String parentId) {
        this.parentId = parentId;
    }

    public MappingUpdatedEvent updateMapping(List<RateeMapping> rateeMappings) {
        return new MappingUpdatedEvent(parentId, rateeMappings);
    }

    protected void on(MappingUpdatedEvent event) {
        event.getMappings().forEach(mapping -> mappings.put(mapping.getRateeId(), mapping));
    }

    public Optional<MappingDeletedEvent> deleteMapping(List<String> rateeIds) {
        List<String> deletedIds = new ArrayList<>();
        rateeIds.forEach(rateeId -> {
            if (mappings.containsKey(rateeId)) {
                deletedIds.add(rateeId);
            }
        });
        return deletedIds.isEmpty() ?
                Optional.empty() :
                Optional.of(new MappingDeletedEvent(parentId, deletedIds));
    }

    protected void on(MappingDeletedEvent event) {
        event.getDeletedRateeIds().forEach(rateeId -> mappings.remove(rateeId));
    }

    public boolean containsRatee(String rateeId) {
        return mappings.containsKey(rateeId);
    }

    public RateeMapping getRateeMapping(String userId) {
        return mappings.get(userId);
    }

    public boolean containsColleagueRater(String rateeId, String raterId) {
        if (!mappings.containsKey(rateeId)) return false;
        return mappings.get(rateeId).hasColleagueRater(raterId);
    }

    public PairValues<Map<String, List<String>>, Map<String, List<String>>> rateeChangedFirstRater(List<RateeMapping> newRateeMappings) {
        Map<String, List<String>> rateeRemovedFirstRater = new HashMap<>();
        Map<String, List<String>> rateeAddedFirstRater = new HashMap<>();
        newRateeMappings.forEach(newMapping -> {
            RateeMapping oldMapping = mappings.get(newMapping.getRateeId());
            if (oldMapping != null) {
                if (oldMapping.hasFirstRater() && !oldMapping.getFirstRaterId().equals(newMapping.getFirstRaterId())) {
                    List<String> removedRatee = rateeRemovedFirstRater.getOrDefault(oldMapping.getFirstRaterId(), new ArrayList<>());
                    removedRatee.add(oldMapping.getRateeId());
                    rateeRemovedFirstRater.put(oldMapping.getFirstRaterId(), removedRatee);

                    if (newMapping.hasFirstRater()) {
                        List<String> addedRatee = rateeAddedFirstRater.getOrDefault(newMapping.getFirstRaterId(), new ArrayList<>());
                        addedRatee.add(newMapping.getRateeId());
                        rateeAddedFirstRater.put(newMapping.getFirstRaterId(), addedRatee);
                    }
                }
            } else {

            }
        });
        return new PairValues(rateeRemovedFirstRater, rateeAddedFirstRater);
    }
}
