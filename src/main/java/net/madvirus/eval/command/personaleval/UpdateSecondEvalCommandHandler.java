package net.madvirus.eval.command.personaleval;

import net.madvirus.eval.api.personaleval.AlreadyEvaluationDoneException;
import net.madvirus.eval.api.personaleval.PersonalEvalNotFoundException;
import net.madvirus.eval.api.personaleval.second.NotYetSecondPerfOrCompeEvalDoneException;
import net.madvirus.eval.api.personaleval.second.YouAreNotSecondRaterException;
import net.madvirus.eval.command.personaleval.common.TotalEvalUpdate;
import net.madvirus.eval.command.personaleval.second.UpdateSecondCompetencyEvalCommand;
import net.madvirus.eval.command.personaleval.second.UpdateSecondPerformanceEvalCommand;
import net.madvirus.eval.command.personaleval.second.UpdateSecondTotalEvalCommand;
import net.madvirus.eval.domain.personaleval.PersonalEval;
import net.madvirus.eval.domain.personaleval.TotalEval;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateSecondEvalCommandHandler {

    private Repository<PersonalEval> repository;

    public UpdateSecondEvalCommandHandler(Repository<PersonalEval> repository) {
        this.repository = repository;
    }

    @CommandHandler
    public void handle(UpdateSecondPerformanceEvalCommand command) {
        try {
            PersonalEval personalEval = repository.load(PersonalEval.createId(command.getEvalSeasonId(), command.getRateeId()));
            personalEval.getSecondRaterOperator().updateSecondPerformanceEval(command);
        } catch (AggregateNotFoundException e) {
            throw new PersonalEvalNotFoundException();
        }
    }

    @CommandHandler
    public void handle(UpdateSecondCompetencyEvalCommand command) {
        try {
            PersonalEval personalEval = repository.load(PersonalEval.createId(command.getEvalSeasonId(), command.getRateeId()));
            personalEval.getSecondRaterOperator().updateSecondCompetencyEval(command);
        } catch (AggregateNotFoundException e) {
            throw new PersonalEvalNotFoundException();
        }
    }

    @CommandHandler
    public void handle(UpdateSecondTotalEvalCommand command) {
        List<PersonalEval> evals = getPersonalEvals(command);
        if (!evals.stream().allMatch(eval -> eval.checkSecondRater(command.getRaterId()))) {
            throw new YouAreNotSecondRaterException();
        }
        if (evals.stream().anyMatch(eval -> eval.isSecondTotalEvalDone())) {
            throw new AlreadyEvaluationDoneException();
        }
        Map<String, PersonalEval> evalMap = new HashMap<>();
        for (PersonalEval eval : evals) {
            boolean perfHad = eval.isSecondPerfEvalHad();
            boolean compeHad = eval.isSecondCompeEvalHad();
            if (!perfHad || !compeHad)
                throw new NotYetSecondPerfOrCompeEvalDoneException();
            evalMap.put(eval.getId(), eval);
        }
        command.getEvalUpdates().forEach(update -> {
            PersonalEval personalEval = evalMap.get(PersonalEval.createId(command.getEvalSeasonId(), update.getRateeId()));
            personalEval.getSecondRaterOperator().updateSecondTotalEval(new TotalEval(update.getComment(), update.getGrade(), command.isDone()));
        });
    }

    private List<PersonalEval> getPersonalEvals(UpdateSecondTotalEvalCommand command) {
        try {
            List<PersonalEval> evals = new ArrayList<>();
            for (TotalEvalUpdate update : command.getEvalUpdates()) {
                PersonalEval load = repository.load(PersonalEval.createId(command.getEvalSeasonId(), update.getRateeId()));
                evals.add(load);
            }
            return evals;
        } catch(AggregateNotFoundException ex) {
            throw new PersonalEvalNotFoundException(ex);
        }
    }
}
