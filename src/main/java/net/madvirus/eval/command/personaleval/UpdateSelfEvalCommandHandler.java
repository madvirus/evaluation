package net.madvirus.eval.command.personaleval;

import net.madvirus.eval.api.RateeMapping;
import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;
import net.madvirus.eval.api.evalseaon.RateeNotFoundException;
import net.madvirus.eval.api.personaleval.PersonalEval;
import net.madvirus.eval.api.personaleval.self.UpdateSelfCompetencyEvalCommand;
import net.madvirus.eval.api.personaleval.self.UpdateSelfPerformanceEvalCommand;
import net.madvirus.eval.api.evalseaon.EvalSeason;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;

import static net.madvirus.eval.api.personaleval.PersonalEval.createId;

public class UpdateSelfEvalCommandHandler {
    private Repository<PersonalEval> personalEvalRepository;
    private Repository<EvalSeason> evalSeasonRepository;

    public UpdateSelfEvalCommandHandler(Repository<PersonalEval> personalEvalRepository, Repository<EvalSeason> evalSeasonRepository) {
        this.personalEvalRepository = personalEvalRepository;
        this.evalSeasonRepository = evalSeasonRepository;
    }

    @CommandHandler
    public void handle(UpdateSelfPerformanceEvalCommand command) {
        PersonalEval personalEval = getPersonalEvalOrThrowExIfInvalid(
                command.getEvalSeasonId(),
                command.getUserId());
        personalEval.getSelfRaterOperator().updateSelfPerfomanceEvaluation(command);
    }

    @CommandHandler
    public void handle(UpdateSelfCompetencyEvalCommand command) {
        PersonalEval personalEval = getPersonalEvalOrThrowExIfInvalid(
                command.getEvalSeasonId(),
                command.getRateeId());
        personalEval.getSelfRaterOperator().updateSelfCompetencyEvaluation(command);
    }

    private PersonalEval getPersonalEvalOrThrowExIfInvalid(String evalSeasonId, String userId) {
        EvalSeason evalSeason = null;
        try {
            evalSeason = evalSeasonRepository.load(evalSeasonId);
        } catch (AggregateNotFoundException e) {
            throw new EvalSeasonNotFoundException();
        }

        if (!evalSeason.containsRatee(userId)) {
            throw new RateeNotFoundException();
        }

        String personalEvalId = createId(evalSeasonId, userId);
        PersonalEval personalEval = null;
        try {
            personalEval = personalEvalRepository.load(personalEvalId);
        } catch (AggregateNotFoundException e) {
            RateeMapping rateeMapping = evalSeason.getRateeMapping(userId);

            personalEval = new PersonalEval(evalSeasonId, userId,
                    rateeMapping.getType(),
                    rateeMapping.getFirstRaterId(),
                    rateeMapping.getSecondRaterId());
            personalEvalRepository.add(personalEval);
        }
        return personalEval;
    }
}
