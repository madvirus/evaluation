package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.api.RateeMapping;
import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;
import net.madvirus.eval.api.evalseaon.FirstEvalDoneException;
import net.madvirus.eval.domain.evalseason.EvalSeason;
import net.madvirus.eval.domain.personaleval.PersonalEval;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModel;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModelRepository;
import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.query.user.UserModelRepository;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import scala.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class UpdateMappingCommandHandler {
    private Repository<EvalSeason> repository;
    private Repository<PersonalEval> personalEvalRepository;
    private UserModelRepository userModelRepository;
    private EvalSeasonMappingModelRepository mappingModelRepository;

    public UpdateMappingCommandHandler(
            Repository<EvalSeason> repository,
            Repository<PersonalEval> personalEvalRepository,
            UserModelRepository userModelRepository,
            EvalSeasonMappingModelRepository mappingModelRepository) {
        this.repository = repository;
        this.personalEvalRepository = personalEvalRepository;
        this.userModelRepository = userModelRepository;
        this.mappingModelRepository = mappingModelRepository;
    }

    @CommandHandler
    public void handle(UpdateMappingCommand command) {
        EvalSeason evalSeason = loadEvalSeason(command.getEvalSeasonId());
        List<RateeMapping> rateeMappings = command.getRateeMappings();
        checkUserId(rateeMappings);

        evalSeason.updateMapping(rateeMappings, (firstRaters -> firstRaters.forEach(firstRaterId -> {
            Option<EvalSeasonMappingModel> mappingModelOpt = mappingModelRepository.findById(command.getEvalSeasonId());
            if (mappingModelOpt.nonEmpty()) {
                EvalSeasonMappingModel mappingModel = mappingModelOpt.get();
                Set<UserModel> ratees = mappingModel.getRateesOfFirstRater(firstRaterId);
                ratees.forEach(ratee -> {
                    try {
                        PersonalEval eval = personalEvalRepository.load(PersonalEval.createId(command.getEvalSeasonId(), ratee.getId()));
                        if (!eval.isFirstEvalSkipTarget() && eval.isFirstTotalEvalDone()) {
                            throw new FirstEvalDoneException();
                        }
                    } catch (AggregateNotFoundException e) {
                    }
                });
            }
        })));

        rateeMappings.forEach(rateeMapping -> {
            Optional<PersonalEval> personalEval = loadPersonalEval(command, rateeMapping);
            personalEval.ifPresent(pe -> pe.getMappingOperator().applyUpdatedMapping(rateeMapping));
        });
    }

    private Optional<PersonalEval> loadPersonalEval(UpdateMappingCommand command, RateeMapping rateeMapping) {
        try {
            return Optional.of(personalEvalRepository.load(PersonalEval.createId(command.getEvalSeasonId(), rateeMapping.getRateeId())));
        } catch (AggregateNotFoundException e) {
            return Optional.empty();
        }
    }

    private EvalSeason loadEvalSeason(String evalSeasonId) {
        try {
            return repository.load(evalSeasonId);
        } catch (AggregateNotFoundException e) {
            throw new EvalSeasonNotFoundException(e);
        }
    }

    private void checkUserId(List<RateeMapping> rateeMappings) {
        List<String> notFoundIds = new ArrayList<>();
        rateeMappings.forEach(mapping -> {
            if (userModelRepository.findByName(mapping.getRateeId()) == null) {
                notFoundIds.add(mapping.getRateeId());
            }
            if (mapping.hasFirstRater() && userModelRepository.findByName(mapping.getFirstRaterId()) == null) {
                notFoundIds.add(mapping.getFirstRaterId());
            }
            if (userModelRepository.findByName(mapping.getSecondRaterId()) == null) {
                notFoundIds.add(mapping.getSecondRaterId());
            }
            mapping.getColleagueRaterIds().forEach(colleagueId -> {
                if (userModelRepository.findByName(colleagueId) == null) {
                    notFoundIds.add(colleagueId);
                }
            });
        });
        // TODO UserID가 존재하지 않는 경우의 익셉션 처리 없음
    }
}
