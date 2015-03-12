package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.personaleval.PersonalEvalNotFoundException;

import java.util.List;
import java.util.Set;

public interface PersonalEvalDataLoader {
    PersonalEvalState getPersonalEvalStateOf(String evalSeasonId, String rateeId);

    SelfPerfEvalData getSelfPerfEvalDataForSelfEvalForm(String evalSeasonId, String rateeId);

    CompeEvalData getSelfCompeEvalDataForSelfEvalForm(String evalSeasonId, String rateeId);

    PersonalEvalData getPersonalEval(String evalSeasonId, String rateeId) throws PersonalEvalNotFoundException;

    List<ColleagueEvalState> getColleagueEvalStates(String evalSeasonId, String colleagueId, Set<String> rateeIds);

    CompeEvalData getColleagueCompeEvalDataForEvalForm(String evalSeasonId, String rateeId, String colleagueId);

    public FirstTotalEvalData getFirstTotalEvalData(String evalSeasonId, String firstRaterId);

    SecondTotalEvalData getSecondTotalEvalData(String evalSeasonId, String id);

    List<PersonalEvalData> getAllPersonalEvalData(String evalSeasonId);
}
