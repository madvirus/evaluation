package net.madvirus.eval.command.personaleval;

import net.madvirus.eval.domain.personaleval.PersonalEval;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModel;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModelRepository;
import net.madvirus.eval.query.user.UserModel;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import scala.Option;

import java.util.Set;

public class ReturnDraftCommandHandler {
    private EvalSeasonMappingModelRepository mappingModelRepository;
    private Repository<PersonalEval> personalEvalRepository;

    public ReturnDraftCommandHandler(EvalSeasonMappingModelRepository mappingModelRepository, Repository<PersonalEval> personalEvalRepository) {
        this.mappingModelRepository = mappingModelRepository;
        this.personalEvalRepository = personalEvalRepository;
    }

    @CommandHandler
    public void returnDraftOfFirstEval(ReturnFirstEvalDraftCommand command) {
        Option<EvalSeasonMappingModel> mappingModel = mappingModelRepository.findById(command.getEvalSeasonId());
        Set<UserModel> ratees = mappingModel.get().getRateesOfFirstRater(command.getFirstRaterId());
        ratees.forEach(ratee -> {
            try {
                PersonalEval personalEval = personalEvalRepository.load(PersonalEval.createId(command.getEvalSeasonId(), ratee.getId()));
                personalEval.returnToFirstDraft();
            } catch (AggregateNotFoundException e) {
            }
        });
    }
}
