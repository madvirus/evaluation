package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.command.personaleval.PersonalEval;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;

import java.util.Optional;

import static net.madvirus.eval.axon.AxonUtil.runInUOW;
import static net.madvirus.eval.command.personaleval.PersonalEval.createId;

public class PersonalEvalDataLoaderImpl implements PersonalEvalDataLoader {

    private EvalSeasonDataLoader evalSeasonDataLoader;
    private Repository<PersonalEval> personalEvalRepository;

    public PersonalEvalDataLoaderImpl(Repository<PersonalEval> personalEvalRepository, EvalSeasonDataLoader evalSeasonDataLoader) {
        this.personalEvalRepository = personalEvalRepository;
        this.evalSeasonDataLoader = evalSeasonDataLoader;
    }

    @Override
    public Optional<PersonalEvalState> getPersonalEvalStateOf(String evalSeasonId, String userId) {
        return runInUOW(() -> {
            Optional<EvalSeasonData> evalSeasonDataOpt = evalSeasonDataLoader.load(evalSeasonId);
            if (!evalSeasonDataOpt.isPresent()) {
                return Optional.empty();
            }
            if (!evalSeasonDataOpt.get().getMappingModel().containsRatee(userId)) {
                return Optional.empty();
            }
            try {
                PersonalEval personalEval = personalEvalRepository.load(createId(evalSeasonId, userId));
                return Optional.of(personalEval);
            } catch (AggregateNotFoundException e) {
                return Optional.of(PersonalEvalStateBuilder.notStarted());
            }
        });
    }

    @Override
    public Optional<SelfPerfEvalData> getSelfPerfEval(String personalEvalId) {
        return runInUOW(() -> {
            try {
                PersonalEval personalEval = personalEvalRepository.load(personalEvalId);
                return Optional.of(new SelfPerfEvalData(
                        personalEval.getId(),
                        personalEval.getUserId(),
                        personalEval.getPerfItemAndSelfEvals(), personalEval.isSelfPerfEvalDone()
                ));
            } catch (AggregateNotFoundException e) {
                return Optional.empty();
            }
        });
    }

    @Override
    public Optional<SelfCompeEvalData> getSelfCompeEval(String personalEvalId) {
        return runInUOW(() -> {
            try {
                PersonalEval personalEval = personalEvalRepository.load(personalEvalId);
                return Optional.of(new SelfCompeEvalData(
                        personalEval.getId(),
                        personalEval.getUserId(),
                        personalEval.getSelfCompeEvalSet(),
                        personalEval.isSelfCompeEvalDone()
                ));
            } catch (AggregateNotFoundException e) {
                return Optional.empty();
            }
        });
    }

}
