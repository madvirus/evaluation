package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.api.evalseaon.CreateEvalSeasonCommand;
import net.madvirus.eval.api.DuplicateIdException;
import net.madvirus.eval.api.evalseaon.EvalSeason;
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
        checkDuplicateIdExists(command);
        repository.add(new EvalSeason(command.getEvalSeasonId(), command.getName()));
    }

    private void checkDuplicateIdExists(CreateEvalSeasonCommand command) {
        try {
            EvalSeason season = repository.load(command.getEvalSeasonId());
            if (season != null) {
                throw new DuplicateIdException(command.getEvalSeasonId());
            }
        } catch (AggregateNotFoundException e) {
        }
    }
}
