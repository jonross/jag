package org.github.jonross.stuff4j.function;

import java.util.function.Consumer;
import java.util.function.Function;

import static org.github.jonross.stuff4j.Stuff4J.$;

/**
 * Prototype.
 * Subject to change.
 *
 * An alernative to that pesky "void" thing, the bane of function signatures everywhere.
 */

public final class Null
{
    public final static Null NULL = new Null();

    private Null () {}

    public static <T> Function<T,Null> devoid(Consumer<T> c) {
        return t -> {
            c.accept(t);
            return NULL;
        };
    }

    public static <T,E extends Exception> Function<T,Null> unvoid(Throwing.Consumer<T,E> c) {
        return t -> {
            $.accept(c, t);
            return NULL;
        };
    }
}
