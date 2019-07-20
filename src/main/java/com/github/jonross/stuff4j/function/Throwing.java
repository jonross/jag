package com.github.jonross.stuff4j.function;

/**
 * Analogs to JDK functional interfaces that also allow checked exceptions.  The exception parameter in these
 * these types extends <code>Exception</code>, not <code>Throwable</code>, because Java code outside the JDK
 * should not catch or throw <code>Throwable</code>.
 */

public final class Throwing
{
    private Throwing() {/* no instances */}

    /**
     * A version of {@link java.lang.Runnable} that allows a checked exception to be thrown.
     */

    @FunctionalInterface
    public interface Runnable<E extends Exception> {
        void run() throws E;
    }

    /**
     * A version of {@link java.util.function.Supplier} that allows a checked exception to be thrown.
     */

    @FunctionalInterface
    public interface Supplier<T,E extends Exception> {
        T get() throws E;
    }

    /**
     * A version of {@link java.util.function.Consumer} that allows a checked exception to be thrown.
     */

    @FunctionalInterface
    public interface Consumer<T,E extends Exception> {
        void accept(T t) throws E;
    }

    /**
     * A version of {@link java.util.function.BiConsumer} that allows a checked exception to be thrown.
     */

    @FunctionalInterface
    public interface BiConsumer<T,U,E extends Exception> {
        void accept(T t, U u) throws E;
    }

    /**
     * A version of {@link java.util.function.Function} that allows a checked exception to be thrown.
     */

    @FunctionalInterface
    public interface Function<T,R,E extends Exception> {
        R apply(T t) throws E;
    }

    /**
     * A version of {@link java.util.function.BiFunction} that allows a checked exception to be thrown.
     */

    @FunctionalInterface
    public interface BiFunction<T,U,R,E extends Exception> {
        R apply(T t, U u) throws E;
    }
}
