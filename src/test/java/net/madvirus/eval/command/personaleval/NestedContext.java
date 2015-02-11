package net.madvirus.eval.command.personaleval;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class NestedContext {
    public static void nestedRun(String name, Runnable runnable) {
        runnable.run();
    }
    public static <T> void nestedRun(String name, Supplier<T> fixtureCreator, Consumer<T> consumer) {
        consumer.accept(fixtureCreator.get());
    }
}
