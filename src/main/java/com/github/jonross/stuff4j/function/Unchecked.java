package com.github.jonross.stuff4j.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Convert from {@link Throwing} versions of JDK functional interfaces, which can throw checked exceptions,
 * to the JDK ones, which cannot.  These are especially useful in {@link java.util.stream.Stream} processing,
 * since most methods on <code>Stream</code> do not allow exceptions from lambdas.
 * <p>
 * This is a last-resort option to exceptions in streams; the preferred approach is to use {@Link Try}.
 */

public class Unchecked
{
    /**
     * All Stuff4J conversions of checked exceptions to unchecked go through this method.  A {@link RuntimeException}
     * is returned as-is.  An {@link IOException} is wrapped in {@link java.io.UncheckedIOException}.  All other
     * checked exceptions are wrapped in {@link RuntimeException}.
     */

    public static RuntimeException wrap(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        if (e instanceof IOException) {
            return new UncheckedIOException((IOException) e);
        }
        return new RuntimeException(e);
    }

    /**
     * Convert a runnable that throws checked exceptions to one that wraps hhem using {@link #wrap}.
     */

    public static <E extends Exception> Runnable runnable(Throwing.Runnable<E> r) {
        return new Runnable(){
            @Override
            public void run() {
                try {
                    r.run();
                }
                catch (Exception e) {
                    throw wrap(e);
                }
            }
        };
    }

    /**
     * Convert a supplier that throws checked exceptions to one that wraps hhem using {@link #wrap}.
     */

    public static <T,E extends Exception> Supplier<T> supplier(Throwing.Supplier<T,E> s) {
        return new Supplier<>(){
            @Override
            public T get() {
                try {
                    return s.get();
                }
                catch (Exception e) {
                    throw wrap(e);
                }
            }
        };
    }

    /**
     * Convert a consumer that throws checked exceptions to one that wraps hhem using {@link #wrap}.
     */

    public static <T,E extends Exception> Consumer<T> consumer(Throwing.Consumer<T,E> c) {
        return new Consumer<>(){
            @Override
            public void accept(T t) {
                try {
                    c.accept(t);
                }
                catch (Exception e) {
                    throw wrap(e);
                }
            }
        };
    }

    /**
     * Convert a bi-consumer that throws checked exceptions to one that wraps hhem using {@link #wrap}.
     */

    public static <T,U,E extends Exception> BiConsumer<T,U> biConsumer(Throwing.BiConsumer<T,U,E> c) {
        return new BiConsumer<>(){
            @Override
            public void accept(T t, U u) {
                try {
                    c.accept(t, u);
                }
                catch (Exception e) {
                    throw wrap(e);
                }
            }
        };
    }

    /**
     * Convert a function that throws checked exceptions to one that wraps hhem using {@link #wrap}.
     */

    public static <T,R,E extends Exception> Function<T,R> function(Throwing.Function<T,R,E> f) {
        return new Function<>(){
            @Override
            public R apply(T t) {
                try {
                    return f.apply(t);
                }
                catch (Exception e) {
                    throw wrap(e);
                }
            }
        };
    }

    /**
     * Convert a bi-function that throws checked exceptions to one that wraps hhem using {@link #wrap}.
     */

    public static <T,U,R,E extends Exception> BiFunction<T,U,R> biFunction(Throwing.BiFunction<T,U,R,E> f) {
        return new BiFunction<>(){
            @Override
            public R apply(T t, U u) {
                try {
                    return f.apply(t, u);
                }
                catch (Exception e) {
                    throw wrap(e);
                }
            }
        };
    }

    /**
     * Call a throwing runnable from a call site that cannot throw checked exceptions.
     */

    public static <E extends Exception> void run(Throwing.Runnable<E> r) {
        runnable(r).run();
    }

    /**
     * Call a throwing supplier from a call site that cannot throw checked exceptions.
     */

    public static <T,E extends Exception> T get(Throwing.Supplier<T,E> s) {
        return supplier(s).get();
    }

    /**
     * Call a throwing consumer from a call site that cannot throw checked exceptions.
     */

    public static <T,E extends Exception> void accept(Throwing.Consumer<T,E> c, T t) {
        consumer(c).accept(t);
    }

    /**
     * Call a throwing bi-consumer from a call site that cannot throw checked exceptions.
     */

    public static <T,U,E extends Exception> void accept(Throwing.BiConsumer<T,U,E> c, T t, U u) {
        biConsumer(c).accept(t, u);
    }

    /**
     * Call a throwing function from a call site that cannot throw checked exceptions.
     */

    public static <T,R,E extends Exception> R apply(Throwing.Function<T,R,E> f, T t) {
        return function(f).apply(t);
    }

    /**
     * Call a throwing bi-function from a call site that cannot throw checked exceptions.
     */

    public static <T,U,R,E extends Exception> R apply(Throwing.BiFunction<T,U,R,E> f, T t, U u) {
        return biFunction(f).apply(t, u);
    }
}
