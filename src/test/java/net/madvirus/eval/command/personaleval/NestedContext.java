package net.madvirus.eval.command.personaleval;

public class NestedContext {
    public static void nestedRun(String name, Runnable runnable) {
        runnable.run();
    }
}
