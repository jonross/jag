package org.github.jonross.stuff4j.lang;

import java.util.List;

import javax.annotation.Nullable;

/**
 * The actual implementation for 4-valued tuples.
 * For details see {@link Tuple}.
 */

public class Tuple4<A,B,C,D> implements Tuple
{
    public final A _1;
    public final B _2;
    public final C _3;
    public final D _4;
    
    /**
     * Construct a tuple of four values.
     */

    public Tuple4(@Nullable A a, @Nullable B b, @Nullable C c, @Nullable D d) {
        _1 = a;
        _2 = b;
        _3 = c;
        _4 = d;
    }

    /**
     * Slightly more readable alternative to constructor.
     */

    public static <A,B,C,D> Tuple4<A,B,C,D> of(@Nullable A a, @Nullable B b, @Nullable C c, @Nullable D d)
    {
        return new Tuple4<>(a, b, c, d);
    }

    /** @see Tuple#arity */

    @Override
    public int arity() {
        return 4;
    }

    /** @see Tuple#toList */

    @Override
    public List<Object> toList() {
        return List.of(_1, _2, _3, _4);
    }

    /** @see Tuple#hashCode() */
    
    @Override
    public int hashCode() {
        return Objects.hashCode(_1, _2, _3, _4);
    }

    /** @see Tuple#equals(Object) */
    
    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Tuple4)) {
            return false;
        }
        Tuple4<?,?,?,?> that = (Tuple4<?,?,?,?>) o;
        return Objects.equal(this._1, that._1)
            && Objects.equal(this._2, that._2)
            && Objects.equal(this._3, that._3)
            && Objects.equal(this._4, that._4);
    }
}
