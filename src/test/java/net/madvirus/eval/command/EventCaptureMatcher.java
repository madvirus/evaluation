package net.madvirus.eval.command;

import org.axonframework.domain.GenericDomainEventMessage;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventCaptureMatcher extends BaseMatcher<Iterable<GenericDomainEventMessage>> {
    private List<GenericDomainEventMessage> values = new ArrayList<>();

    @Override
    public boolean matches(Object o) {
        for (GenericDomainEventMessage t : (Iterable<GenericDomainEventMessage>)o) values.add(t);
        return true;
    }

    @Override
    public void describeTo(Description description) {
    }

    public GenericDomainEventMessage getValue() {
        return values.get(0);
    }

    public List<GenericDomainEventMessage> getValues() {
        return values;
    }

    public Object getPayload() {
        return values.get(0).getPayload();
    }

    public List<Object> getPayloads() {
        return values.stream().map(v -> v.getPayload()).collect(Collectors.toList());
    }
}
