package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;
import net.madvirus.eval.api.evalseaon.FirstEvalDoneException;
import net.madvirus.eval.domain.evalseason.DistributionRule;
import net.madvirus.eval.domain.evalseason.EvalSeason;
import net.madvirus.eval.domain.personaleval.PersonalEval;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;

import java.util.Optional;

public class UpdateDistributionRuleCommandHandler {
    private Repository<EvalSeason> evalSeasonRepository;
    private Repository<PersonalEval> personalEvalRepository;

    public UpdateDistributionRuleCommandHandler(Repository<EvalSeason> evalSeasonRepository, Repository<PersonalEval> personalEvalRepository) {
        this.evalSeasonRepository = evalSeasonRepository;
        this.personalEvalRepository = personalEvalRepository;
    }

    @CommandHandler
    public void handle(UpdateDistributionRuleCommand command) {
        checkFirstEvalDone(command);
        EvalSeason evalSeason = getEvalSeason(command.getEvalSeasonId());
        evalSeason.updateDistributionRule(command.getFirstRaterId(), command.getRules());
    }

    private void checkFirstEvalDone(UpdateDistributionRuleCommand command) {
        for (DistributionRule rule : command.getRules()) {
            for (String rateeId : rule.getRateeIds()) {
                Optional<PersonalEval> evalOpt = getPersonalEval(PersonalEval.createId(command.getEvalSeasonId(), rateeId));
                evalOpt.ifPresent(e -> {
                    if (e.isFirstTotalEvalDone()) {
                        throw new FirstEvalDoneException();
                    }
                });
            }
        }
    }

    private Optional<PersonalEval> getPersonalEval(String rateeId) {
        try {
            return Optional.ofNullable(personalEvalRepository.load(rateeId));
        } catch (AggregateNotFoundException e) {
            return Optional.empty();
        }
    }

    private EvalSeason getEvalSeason(String command) {
        try {
            return evalSeasonRepository.load(command);
        } catch (AggregateNotFoundException e) {
            throw new EvalSeasonNotFoundException(e);
        }
    }
}
