package net.madvirus.eval.command.personaleval;

import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;
import net.madvirus.eval.api.evalseaon.RateeNotFoundException;
import net.madvirus.eval.api.personaleval.PersonalEvalNotFoundException;
import net.madvirus.eval.api.personaleval.colleague.YouAreNotColleagueRaterException;
import net.madvirus.eval.command.personaleval.colleague.UpdateColleagueCompetencyEvalCommand;
import net.madvirus.eval.domain.evalseason.EvalSeason;
import net.madvirus.eval.domain.personaleval.PersonalEval;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;

import static net.madvirus.eval.domain.personaleval.PersonalEval.createId;

public class UpdateColleagueEvalCommandHandler {
    private Repository<PersonalEval> personalEvalRepository;
    private Repository<EvalSeason> evalSeasonRepository;

    public UpdateColleagueEvalCommandHandler(Repository<PersonalEval> personalEvalRepository, Repository<EvalSeason> evalSeasonRepository) {
        this.personalEvalRepository = personalEvalRepository;
        this.evalSeasonRepository = evalSeasonRepository;
    }

    @CommandHandler
    public void handle(UpdateColleagueCompetencyEvalCommand command) {
        EvalSeason evalSeason = null;
        try {
            evalSeason = evalSeasonRepository.load(command.getEvalSeasonId());
        } catch (AggregateNotFoundException e) {
            throw new EvalSeasonNotFoundException();
        }

        if (!evalSeason.containsRatee(command.getRateeId())) {
            throw new RateeNotFoundException(command.getRateeId());
        }

        if (!evalSeason.containsColleagueRater(command.getRateeId(), command.getRaterId())) {
            throw new YouAreNotColleagueRaterException();
        }

        String personalEvalId = createId(command.getEvalSeasonId(), command.getRateeId());

        PersonalEval personalEval = null;
        try {
            personalEval = personalEvalRepository.load(personalEvalId);
        } catch (AggregateNotFoundException e) {
            throw new PersonalEvalNotFoundException();
        }
        personalEval.getColleagueRaterOperator().updateColleagueCompetencyEval(command);
    }
}
