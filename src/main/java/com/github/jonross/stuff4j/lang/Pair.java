package com.github.jonross.stuff4j.lang;

import javax.annotation.Nullable;

/**
 * A more Java-ish implementation of {@link Tuple2}
 */

public class Pair<A,B> extends Tuple2<A,B>
{
    public Pair(@Nullable A a, @Nullable B b) {
        super(a, b);
    }

    public static <A,B> Pair<A,B> of(@Nullable A a, @Nullable B b) {
        return new Pair<>(a, b);
    }

    public A first() {
        return _1;
    }

    public B second() {
        return _2;
    }
}
