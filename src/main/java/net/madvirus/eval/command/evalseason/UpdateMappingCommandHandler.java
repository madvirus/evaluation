package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.api.RateeMapping;
import net.madvirus.eval.api.evalseaon.EvalSeason;
import net.madvirus.eval.api.evalseaon.UpdateMappingCommand;
import net.madvirus.eval.query.user.UserModelRepository;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class UpdateMappingCommandHandler {
    private Repository<EvalSeason> repository;
    private UserModelRepository userModelRepository;

    public UpdateMappingCommandHandler(Repository<EvalSeason> repository, UserModelRepository userModelRepository) {
        this.repository = repository;
        this.userModelRepository = userModelRepository;
    }

    @CommandHandler
    public void handle(UpdateMappingCommand command) {
        EvalSeason evalSeason = repository.load(command.getEvalSeasonId());
        checkUserId(command.getRateeMappings());
        evalSeason.updateMapping(command);
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
    }
}
