package net.madvirus.eval.web.dataloader;

import java.util.Optional;

public interface PersonalEvalDataLoader {
    Optional<PersonalEvalState> getPersonalEvalStateOf(String evalSeasonId, String userId);

    Optional<SelfPerfEvalData> getSelfPerfEval(String personalEvalId);

    Optional<SelfCompeEvalData> getSelfCompeEval(String personalEvalId);
}
