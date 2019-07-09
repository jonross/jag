package com.github.jonross.stuff4j.tbd;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.jonross.stuff4j.function.Throwing;
import com.github.jonross.stuff4j.function.Unchecked;

/**
 * Prototype.
 * Subject to change.
 */

public final class Nothing
{
    public final static Nothing NOTHING = new Nothing();

    private Nothing() {}

    public static Supplier<Nothing> devoid(Runnable r) {
        return () -> {
            r.run();
            return NOTHING;
        };
    }

    public static <T> Function<T, Nothing> devoid(Consumer<T> c) {
        return t -> {
            c.accept(t);
            return NOTHING;
        };
    }

    public static <T,E extends Exception> Supplier<Nothing> unvoid(Throwing.Runnable<E> r) {
        return () -> {
            Unchecked.run(r);
            return NOTHING;
        };
    }

    public static <T,E extends Exception> Function<T, Nothing> unvoid(Throwing.Consumer<T,E> c) {
        return t -> {
            Unchecked.accept(c, t);
            return NOTHING;
        };
    }
}
