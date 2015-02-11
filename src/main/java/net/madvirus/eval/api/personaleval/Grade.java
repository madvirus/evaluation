package net.madvirus.eval.api.personaleval;

public enum Grade {
    S(5), A(4), B(3), C(2), D(1);

    private final int number;

    Grade(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
