package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.api.CreateEvalSeasonCommand;
import net.madvirus.eval.api.DuplicateIdException;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;

public class CreateEvalSeasonCommandHandler {

    private Repository<EvalSeason> repository;

    public CreateEvalSeasonCommandHandler(Repository<EvalSeason> repository) {
        this.repository = repository;
    }

    @CommandHandler
    public void handle(CreateEvalSeasonCommand command) {
        try {
            EvalSeason season = repository.load(command.getEvalSeasonId());
            if (season != null) {
                throw new DuplicateIdException(command.getEvalSeasonId());
            }
        } catch (AggregateNotFoundException e) {
        }
        repository.add(new EvalSeason(command.getEvalSeasonId(), command.getName()));

    }
}
