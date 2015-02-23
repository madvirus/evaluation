package net.madvirus.eval.command.personaleval;

import net.madvirus.eval.api.personaleval.PersonalEvalNotFoundException;
import net.madvirus.eval.api.personaleval.first.NotYetFirstPerfOrCompeEvalDoneException;
import net.madvirus.eval.api.personaleval.first.YouAreNotFirstRaterException;
import net.madvirus.eval.command.personaleval.first.UpdateFirstCompetencyEvalCommand;
import net.madvirus.eval.command.personaleval.first.UpdateFirstPerformanceEvalCommand;
import net.madvirus.eval.command.personaleval.first.UpdateFirstTotalEvalCommand;
import net.madvirus.eval.domain.personaleval.PersonalEval;
import net.madvirus.eval.domain.personaleval.TotalEval;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UpdateFirstEvalCommandHandler {

    private Repository<PersonalEval> repository;

    public UpdateFirstEvalCommandHandler(Repository<PersonalEval> repository) {
        this.repository = repository;
    }

    @CommandHandler
    public void handle(UpdateFirstPerformanceEvalCommand command) {
        try {
            PersonalEval personalEval = repository.load(PersonalEval.createId(command.getEvalSeasonId(), command.getRateeId()));
            personalEval.getFirstRaterOperator().updateFirstPerformanceEval(command);
        } catch (AggregateNotFoundException e) {
            throw new PersonalEvalNotFoundException();
        }
    }

    @CommandHandler
    public void handle(UpdateFirstCompetencyEvalCommand command) {
        try {
            PersonalEval personalEval = repository.load(PersonalEval.createId(command.getEvalSeasonId(), command.getRateeId()));
            personalEval.getFirstRaterOperator().updateFirstCompetencyEval(command);
        } catch (AggregateNotFoundException e) {
            throw new PersonalEvalNotFoundException();
        }
    }

    @CommandHandler
    public void handle(UpdateFirstTotalEvalCommand command) {
        List<PersonalEval> evals = getPersonalEvals(command);
        if (!evals.stream().allMatch(eval -> eval.checkFirstRater(command.getRaterId()))) {
            throw new YouAreNotFirstRaterException();
        }
        Map<String, PersonalEval> evalMap = new HashMap<>();
        for (PersonalEval eval : evals) {
            boolean perfHad = eval.isFirstPerfEvalHad();
            boolean compeHad = eval.isFirstCompeEvalHad();
            if (!perfHad || !compeHad)
                throw new NotYetFirstPerfOrCompeEvalDoneException();
            evalMap.put(eval.getId(), eval);
        }
        command.getEvalUpdates().forEach(update -> {
            PersonalEval personalEval = evalMap.get(PersonalEval.createId(command.getEvalSeasonId(), update.getRateeId()));
            personalEval.getFirstRaterOperator().updateFirstTotalEval(new TotalEval(update.getComment(), update.getGrade(), command.isDone()));
        });
    }

    private List<PersonalEval> getPersonalEvals(UpdateFirstTotalEvalCommand command) {
        try {
            List<PersonalEval> evals = command.getEvalUpdates().stream()
                    .map(update -> repository.load(PersonalEval.createId(command.getEvalSeasonId(), update.getRateeId())))
                    .collect(Collectors.toList());
            return evals;
        } catch(AggregateNotFoundException ex) {
            throw new PersonalEvalNotFoundException(ex);
        }
    }
}
