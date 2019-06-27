package org.github.jonross.stuff4j.function;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Convert from {@link Throwing} versions of JDK functional interfaces, which can throw checked exceptions,
 * to the JDK ones, which cannot.  These are especially useful in {@link java.util.stream.Stream} processing,
 * since most methods on <code>Stream</code> do not allow exceptions from lambdas.
 */

public class Unchecked
{
    /**
     * Convert a runnable that can throw checked exceptions to one that cannot.
     */

    public static <E extends Exception> Runnable runnable(Throwing.Runnable<E> r) {
        return new Runnable(){
            @Override
            public void run() {
                try {
                    r.run();
                }
                catch (RuntimeException e) {
                    throw e;
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    /**
     * Convert a supplier that can throw checked exceptions to one that cannot.
     */

    public static <T,E extends Exception> Supplier<T> supplier(Throwing.Supplier<T,E> s) {
        return new Supplier<>(){
            @Override
            public T get() {
                try {
                    return s.get();
                }
                catch (RuntimeException e) {
                    throw e;
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    /**
     * Convert a consumer that can throw checked exceptions to one that cannot.
     */

    public static <T,E extends Exception> Consumer<T> consumer(Throwing.Consumer<T,E> c) {
        return new Consumer<>(){
            @Override
            public void accept(T t) {
                try {
                    c.accept(t);
                }
                catch (RuntimeException e) {
                    throw e;
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    /**
     * Convert a bi-consumer that can throw checked exceptions to one that cannot.
     */

    public static <T,U,E extends Exception> BiConsumer<T,U> biConsumer(Throwing.BiConsumer<T,U,E> c) {
        return new BiConsumer<>(){
            @Override
            public void accept(T t, U u) {
                try {
                    c.accept(t, u);
                }
                catch (RuntimeException e) {
                    throw e;
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    /**
     * Convert a function that can throw checked exceptions to one that cannot.
     */

    public static <T,R,E extends Exception> Function<T,R> function(Throwing.Function<T,R,E> f) {
        return new Function<>(){
            @Override
            public R apply(T t) {
                try {
                    return f.apply(t);
                }
                catch (RuntimeException e) {
                    throw e;
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    /**
     * Convert a bi-function that can throw checked exceptions to one that cannot.
     */

    public static <T,U,R,E extends Exception> BiFunction<T,U,R> function(Throwing.BiFunction<T,U,R,E> f) {
        return new BiFunction<>(){
            @Override
            public R apply(T t, U u) {
                try {
                    return f.apply(t, u);
                }
                catch (RuntimeException e) {
                    throw e;
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
