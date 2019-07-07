package com.github.jonross.stuff4j.lang;

import javax.annotation.Nullable;

/**
 * A more Java-ish implementation of {@link Tuple3}
 */

public class Trio<A,B,C> extends Tuple3<A,B,C>
{
    public Trio(@Nullable A a, @Nullable B b, @Nullable C c) {
        super(a, b, c);
    }

    public static <A,B,C> Trio<A,B,C> of(@Nullable A a, @Nullable B b, @Nullable C c) {
        return new Trio<>(a, b, c);
    }

    public A first() {
        return _1;
    }

    public B second() {
        return _2;
    }

    public C third() {
        return _3;
    }
}
