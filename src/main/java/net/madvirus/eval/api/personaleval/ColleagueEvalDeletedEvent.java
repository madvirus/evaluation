package net.madvirus.eval.api.personaleval;

import java.util.List;

public class ColleagueEvalDeletedEvent {
    private String personalEvalid;
    private List<String> colleagueIds;

    protected ColleagueEvalDeletedEvent() {}
    public ColleagueEvalDeletedEvent(String personalEvalid, List<String> colleagueIds) {
        this.personalEvalid = personalEvalid;
        this.colleagueIds = colleagueIds;
    }

    public String getPersonalEvalid() {
        return personalEvalid;
    }

    public List<String> getColleagueIds() {
        return colleagueIds;
    }
}
