package net.madvirus.eval.command.personaleval;

import net.madvirus.eval.api.DuplicateIdException;
import net.madvirus.eval.api.personaleval.CreatePersonalEvalCommand;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;

public class CreatePersonalEvalCommandHandler {
    private Repository<PersonalEval> repository;

    public CreatePersonalEvalCommandHandler(Repository<PersonalEval> repository) {
        this.repository = repository;
    }

    @CommandHandler
    public void on(CreatePersonalEvalCommand command) {
        try {
            String id = PersonalEval.createId(command.getEvalSeasonId(), command.getUserId());
            PersonalEval load = repository.load(id);
            throw new DuplicateIdException(id);
        } catch (AggregateNotFoundException e) {
        }
        repository.add(new PersonalEval(command.getEvalSeasonId(), command.getUserId()));
    }
}
