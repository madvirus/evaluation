package net.madvirus.eval.command.personaleval;

import net.madvirus.eval.api.personaleval.PersonalEval;
import net.madvirus.eval.api.personaleval.PersonalEvalNotFoundException;
import net.madvirus.eval.api.personaleval.first.RejectSelfCompetencyEvalCommand;
import net.madvirus.eval.api.personaleval.first.RejectSelfPerformanceEvalCommand;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;

public class RejectEvalCommandHandler {
    private Repository<PersonalEval> personalEvalRepository;

    public RejectEvalCommandHandler(Repository<PersonalEval> personalEvalRepository) {
        this.personalEvalRepository = personalEvalRepository;
    }

    @CommandHandler
    public void handle(RejectSelfPerformanceEvalCommand command) {
        try {
            PersonalEval personalEval = personalEvalRepository.load(PersonalEval.createId(command.getEvalSeasonId(), command.getRateeId()));
            personalEval.getFirstRaterOperator().rejectSelfPerformanceEval(command);
        } catch (AggregateNotFoundException e) {
            throw new PersonalEvalNotFoundException();
        }
    }

    @CommandHandler
    public void handle(RejectSelfCompetencyEvalCommand command) {
        try {
            PersonalEval personalEval = personalEvalRepository.load(PersonalEval.createId(command.getEvalSeasonId(), command.getRateeId()));
            personalEval.getFirstRaterOperator().rejectSelfCompetencyEval(command);
        } catch (AggregateNotFoundException e) {
            throw new PersonalEvalNotFoundException();
        }

    }
}
