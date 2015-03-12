package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.domain.evalseason.EvalSeason;

public class EvalSeasonSimpleData {
    private EvalSeason evalSeason;
    private String id;
    private String name;
    private boolean opened;
    private boolean colleagueEvalutionStarted;
    private boolean firstEvaluationStarted;

    public EvalSeasonSimpleData(EvalSeason evalSeason) {
        this.id = evalSeason.getId();
        this.name = evalSeason.getName();
        this.opened = evalSeason.isOpened();
        this.colleagueEvalutionStarted = evalSeason.isColleagueEvalutionStarted();
        this.firstEvaluationStarted = evalSeason.isFirstEvaluationStarted();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isOpened() {
        return opened;
    }

    public boolean isColleagueEvalutionStarted() {
        return colleagueEvalutionStarted;
    }

    public boolean isFirstEvaluationStarted() {
        return firstEvaluationStarted;
    }
}
