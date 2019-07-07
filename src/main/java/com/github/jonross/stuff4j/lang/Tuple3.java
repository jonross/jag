package org.github.jonross.stuff4j.lang;

import java.util.List;

import javax.annotation.Nullable;

/**
 * The actual implementation for 3-valued tuples.
 * For details see {@link Tuple}.
 */

public class Tuple3<A,B,C> implements Tuple
{
    public final A _1;
    public final B _2;
    public final C _3;

    /**
     * Construct a tuple of three values.
     */

    public Tuple3(@Nullable A a, @Nullable B b, @Nullable C c) {
        _1 = a;
        _2 = b;
        _3 = c;
    }

    /**
     * Slightly more readable alternative to constructor.
     */

    public static <A,B,C> Tuple3<A,B,C> of(@Nullable A a, @Nullable B b, @Nullable C c)
    {
        return new Tuple3<>(a, b, c);
    }

    /** @see Tuple#arity */

    @Override
    public int arity() {
        return 3;
    }
    
    /** @see Tuple#toList */

    @Override
    public List<Object> toList() {
        return List.of(_1, _2, _3);
    }

    /** @see Tuple#hashCode() */
    
    @Override
    public int hashCode() {
        return Objects.hashCode(_1, _2, _3);
    }

    /** @see Tuple#equals(Object) */
    
    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Tuple3)) {
            return false;
        }
        Tuple3<?,?,?> that = (Tuple3<?,?,?>) o;
        return Objects.equal(this._1, that._1)
            && Objects.equal(this._2, that._2)
            && Objects.equal(this._3, that._3);
    }
}
