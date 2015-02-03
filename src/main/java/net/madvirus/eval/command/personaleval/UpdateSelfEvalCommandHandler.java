package net.madvirus.eval.command.personaleval;

import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;
import net.madvirus.eval.api.evalseaon.RateeNotFoundException;
import net.madvirus.eval.api.personaleval.UpdateSelfCompetencyEvalCommand;
import net.madvirus.eval.api.personaleval.UpdateSelfPerformanceEvalCommand;
import net.madvirus.eval.web.dataloader.EvalSeasonData;
import net.madvirus.eval.web.dataloader.EvalSeasonDataLoader;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;

import java.util.Optional;

public class UpdateSelfEvalCommandHandler {
    private Repository<PersonalEval> personalEvalRepository;
    private EvalSeasonDataLoader evalSeasonDataLoader;

    public UpdateSelfEvalCommandHandler(Repository<PersonalEval> personalEvalRepository, EvalSeasonDataLoader evalSeasonDataLoader) {
        this.personalEvalRepository = personalEvalRepository;
        this.evalSeasonDataLoader = evalSeasonDataLoader;
    }

    @CommandHandler
    public void handle(UpdateSelfPerformanceEvalCommand command) {
        PersonalEval personalEval = getPersonalEvalOrThrowExIfInvalid(
                command.getEvalSeasonId(),
                command.getUserId(),
                command.getPersonalEvalId());
        personalEval.updateSelfPerfomanceEvaluation(command);
    }

    @CommandHandler
    public void handle(UpdateSelfCompetencyEvalCommand command) {
        PersonalEval personalEval = getPersonalEvalOrThrowExIfInvalid(
                command.getEvalSeasonId(),
                command.getUserId(),
                command.getPersonalEvalId());
        personalEval.updateSelfCompetencyEvaluation(command);
    }

    private PersonalEval getPersonalEvalOrThrowExIfInvalid(String evalSeasonId, String userId, String personalEvalId) {
        Optional<EvalSeasonData> evalSeaon = evalSeasonDataLoader.load(evalSeasonId);
        if (!evalSeaon.isPresent()) {
            throw new EvalSeasonNotFoundException();
        }

        if (!evalSeaon.get().getMappingModel().containsRatee(userId)) {
            throw new RateeNotFoundException();
        }

        PersonalEval personalEval = null;
        try {
            personalEval = personalEvalRepository.load(personalEvalId);
        } catch (AggregateNotFoundException e) {
            personalEval = new PersonalEval(evalSeasonId, userId);
            personalEvalRepository.add(personalEval);
        }
        return personalEval;
    }
}
