package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.query.user.UserModel;

import java.util.*;

public class EvalSeasonAllEvalStates {
    private String evalSeasonId;
    private List<EvalStateData> allEvalStates = new ArrayList<>();

    public EvalSeasonAllEvalStates(String evalSeasonId) {
        this.evalSeasonId = evalSeasonId;
    }

    public void add(EvalStateData evalStateData) {
        allEvalStates.add(evalStateData);
    }

    public List<EvalStateData> getAllEvalStates() {
        return allEvalStates;
    }

    public static enum State {
        NONE, DOING, DONE
    }
    public static class RaterState {
        private UserModel rater;
        private State state;

        public RaterState(UserModel rater, State state) {
            this.rater = rater;
            this.state = state;
        }

        public UserModel getRater() {
            return rater;
        }

        public State getState() {
            return state;
        }
    }

    public static class EvalStateData {
        private UserModel ratee;
        private UserModel firstRater;
        private boolean firstSkip;
        private UserModel secondRater;

        private boolean selfStarted;
        private boolean selfPerfEvalDone;
        private boolean selfCompeEvalDone;

        private boolean firstPerfEvalHad;
        private boolean firstCompeEvalHad;
        private boolean firstDone;

        private boolean secondPerfEvalHad;
        private boolean secondCompeEvalHad;
        private boolean secondDone;

        private Map<UserModel, State> colleagueDone;

        public EvalStateData(UserModel ratee, List<UserModel> colleagueRaters, UserModel firstRater, UserModel secondRater) {
            this.ratee = ratee;
            this.firstRater = firstRater;
            this.secondRater = secondRater;

            this.selfPerfEvalDone = false;
            this.selfCompeEvalDone = false;
            this.firstDone = false;
            this.firstSkip = false;
            this.secondDone = false;
            this.colleagueDone = new TreeMap<>(new Comparator<UserModel>() {
                @Override
                public int compare(UserModel o1, UserModel o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            colleagueRaters.forEach(collUser -> colleagueDone.put(collUser, State.NONE));
        }

        public EvalStateData() {
            this.colleagueDone = new TreeMap<>(new Comparator<UserModel>() {
                @Override
                public int compare(UserModel o1, UserModel o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }

        public UserModel getRatee() {
            return ratee;
        }
        public RaterState getSelf() {
            if (selfStarted) {
                return new RaterState(ratee, isSelfCompeEvalDone() && isSelfPerfEvalDone() ? State.DONE : State.DOING);
            } else {
                return new RaterState(ratee, State.NONE);
            }
        }
        public RaterState getFirst() {
            if (firstDone) {
                return new RaterState(firstRater, State.DONE);
            } else {
                return new RaterState(firstRater, firstCompeEvalHad || firstPerfEvalHad ? State.DOING : State.NONE);
            }
        }
        public RaterState getSecond() {
            if (secondDone) {
                return new RaterState(secondRater, State.DONE);
            } else {
                return new RaterState(secondRater, secondPerfEvalHad || secondCompeEvalHad ? State.DOING : State.NONE);
            }
        }

        public List<RaterState> getColleagues() {
            List<RaterState> result = new ArrayList<>();
            colleagueDone.forEach((user, done) -> {
                result.add(new RaterState(user, done));
            });
            return result;
        }

        public boolean isFirstSkip() {
            return firstSkip;
        }

        public boolean isSelfPerfEvalDone() {
            return selfPerfEvalDone;
        }

        public boolean isSelfCompeEvalDone() {
            return selfCompeEvalDone;
        }

        public static EvalStateData notYetStarted(UserModel ratee, List<UserModel> colleagueRaters, UserModel firstRater, UserModel secondRater) {
            return new EvalStateData(ratee, colleagueRaters, firstRater, secondRater);
        }

        public static Builder self(UserModel ratee, boolean selfPerfEvalDone, boolean selfCompeEvalDone) {
            return new Builder().self(ratee, selfPerfEvalDone, selfCompeEvalDone);
        }

        public static class Builder {

            private final EvalStateData stateData;

            public Builder() {
                stateData = new EvalStateData();
            }

            public Builder self(UserModel ratee, boolean selfPerfEvalDone, boolean selfCompeEvalDone) {
                stateData.ratee = ratee;
                stateData.selfStarted = true;
                stateData.selfPerfEvalDone = selfPerfEvalDone;
                stateData.selfCompeEvalDone = selfCompeEvalDone;
                return this;
            }

            public Builder first(UserModel firstRater, boolean firstPerfEvalHad, boolean firstCompeEvalHad, boolean firstDone) {
                stateData.firstRater = firstRater;
                stateData.firstDone = firstDone;
                stateData.firstPerfEvalHad = firstPerfEvalHad;
                stateData.firstCompeEvalHad = firstCompeEvalHad;
                return this;
            }

            public Builder second(UserModel secondRater, boolean secondPerfEvalHad, boolean secondCompeEvalHad, boolean secondDone) {
                stateData.secondRater = secondRater;
                stateData.secondPerfEvalHad = secondPerfEvalHad;
                stateData.secondCompeEvalHad = secondCompeEvalHad;
                stateData.secondDone = secondDone;
                return this;
            }

            public Builder firstSkip() {
                stateData.firstSkip = true;
                return this;
            }

            public Builder colleague(UserModel collUser, State colleagueEvalState) {
                stateData.colleagueDone.put(collUser, colleagueEvalState);
                return this;
            }

            public EvalStateData build() {
                return stateData;
            }
        }
    }

}
