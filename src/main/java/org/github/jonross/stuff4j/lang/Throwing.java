package org.github.jonross.stuff4j.lang;

/**
 * Analogs to common JDK functional interfaces that also allow exceptions.
 */

public class Throwing
{
    /**
     * A version of {@link java.lang.Runnable} that allows an exception to be thrown.
     */

    @FunctionalInterface
    public interface Runnable<E extends Exception>
    {
        void run() throws E;
    }

    /**
     * A version of {@link java.util.function.Supplier} that allows an exception to be thrown.
     */

    @FunctionalInterface
    public interface Supplier<T,E extends Exception>
    {
        T get() throws E;
    }

    /**
     * A version of {@link java.util.function.Consumer} that allows an exception to be thrown.
     */

    @FunctionalInterface
    public interface Consumer<T,E extends Exception>
    {
        void accept(T t) throws E;
    }

    /**
     * A version of {@link java.util.function.BiConsumer} that allows an exception to be thrown.
     */

    @FunctionalInterface
    public interface BiConsumer<T,U,E extends Exception>
    {
        void accept(T t, U u) throws E;
    }

    /**
     * A version of {@link java.util.function.Function} that allows an exception to be thrown.
     */

    @FunctionalInterface
    public interface Function<T,R,E extends Exception>
    {
        R apply(T t) throws E;
    }

    /**
     * A version of {@link java.util.function.BiFunction} that allows an exception to be thrown.
     */

    @FunctionalInterface
    public interface BiFunction<T,U,R,E extends Exception>
    {
        R apply(T t, U u) throws E;
    }
}
