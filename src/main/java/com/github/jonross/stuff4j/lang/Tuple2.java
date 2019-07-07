package com.github.jonross.stuff4j.lang;

import java.util.List;

import javax.annotation.Nullable;

/**
 * The actual implementation for 2-valued tuples.
 * For details see {@link Tuple}.
 * For a less Scala-ish version see {@link Pair}.
 */

public class Tuple2<A,B> implements Tuple
{
    public final A _1;
    public final B _2;

    /**
     * Construct a tuple of two values.
     */

    public Tuple2(@Nullable A a, @Nullable B b) {
        _1 = a;
        _2 = b;
    }

    /**
     * Slightly more readable alternative to constructor.
     */

    public static <A,B> Tuple2<A,B> of(@Nullable A a, @Nullable B b)
    {
        return new Tuple2<>(a, b);
    }

    /** @see Tuple#arity */

    @Override
    public int arity() {
        return 2;
    }
    
    /** @see Tuple#toList */

    @Override
    public List<Object> toList() {
        return List.of(_1, _2);
    }

    /** @see Tuple#hashCode() */
    
    @Override
    public int hashCode() {
        return Objects.hashCode(_1, _2);
    }

    /** @see Tuple#equals(Object) */
    
    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Tuple2)) {
            return false;
        }
        Tuple2<?,?> that = (Tuple2<?,?>) o;
        return Objects.equal(this._1, that._1)
            && Objects.equal(this._2, that._2);
    }
}
