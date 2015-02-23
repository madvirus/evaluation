package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.api.evalseaon.CanNotDeleteMappingException;
import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;
import net.madvirus.eval.domain.evalseason.EvalSeason;
import net.madvirus.eval.domain.personaleval.PersonalEval;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;

import java.util.Optional;

public class DeleteMappingCommandHandler {
    private Repository<EvalSeason> repository;
    private Repository<PersonalEval> personalEvalRepository;

    public DeleteMappingCommandHandler(Repository<EvalSeason> repository, Repository<PersonalEval> personalEvalRepository) {
        this.repository = repository;
        this.personalEvalRepository = personalEvalRepository;
    }

    @CommandHandler
    public void handle(DeleteMappingCommand command) {
        EvalSeason evalSeason = loadEvalSeason(command);
        evalSeason.deleteMapping(command.getRateeIds());
        for (String rateeId : command.getRateeIds()) {
            Optional<PersonalEval> personalEval = loadPersonalEval(command.getEvalSeasonId(), rateeId);
            personalEval.ifPresent(eval -> {
                if (eval.isFirstTotalEvalDone()) throw new CanNotDeleteMappingException();
                eval.delete();
            });
        }
    }

    private Optional<PersonalEval> loadPersonalEval(String evalSeasonId, String rateeId) {
        try {
            return Optional.of(personalEvalRepository.load(PersonalEval.createId(evalSeasonId, rateeId)));
        } catch (AggregateNotFoundException e) {
            return Optional.empty();
        }
    }

    private EvalSeason loadEvalSeason(DeleteMappingCommand command) {
        try {
            return (EvalSeason) repository.load(command.getEvalSeasonId());
        } catch (AggregateNotFoundException e) {
            throw new EvalSeasonNotFoundException(e);
        }
    }
}
