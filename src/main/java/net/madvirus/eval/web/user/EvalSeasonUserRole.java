package net.madvirus.eval.web.user;

public class EvalSeasonUserRole {
    private boolean rateeRole = false;
    private boolean firstRaterRole = false;
    private boolean secondRaterRole;
    private boolean colleagueRaterRole;

    public void addRateeRole() {
        this.rateeRole = true;
    }

    public void addFirstRaterRole() {
        this.firstRaterRole = true;
    }
    public void addSecondRaterRole() {
        this.secondRaterRole = true;
    }
    public void addColleagueRaterRole() {
        this.colleagueRaterRole = true;
    }

    public boolean isRateeRole() {
        return rateeRole;
    }

    public boolean isFirstRaterRole() {
        return firstRaterRole;
    }

    public boolean isSecondRaterRole() {
        return secondRaterRole;
    }

    public boolean isColleagueRaterRole() {
        return colleagueRaterRole;
    }

    public boolean isNoRole() {
        return !rateeRole && !firstRaterRole && !secondRaterRole && !colleagueRaterRole;
    }
}
