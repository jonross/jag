package com.github.jonross.stuff4j.lang;

import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;

/**
 * The actual implementation for 1-valued tuples.
 * For details see {@link Tuple}.
 */

public class Tuple1<A> implements Tuple
{
    public final A _1;

    /**
     * Construct a tuple of one value.
     */

    public Tuple1(@Nullable A a) {
        _1 = a;
    }

    /**
     * Slightly more readable alternative to constructor.
     */

    public static <A> Tuple1<A> of(@Nullable A a)
    {
        return new Tuple1<>(a);
    }

    /** @see Tuple#arity */

    @Override
    public int arity() {
        return 1;
    }
    
    /** @see Tuple#toList */

    @Override
    public List<Object> toList() {
        return List.of(_1);
    }
    
    /** @see Tuple#hashCode() */
    
    @Override
    public int hashCode() {
        return Objects.hash(_1);
    }
    
    /** @see Tuple#equals(Object) */
    
    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Tuple1)) {
            return false;
        }
        Tuple1<?> that = (Tuple1<?>) o;
        return Objects.equals(this._1, that._1);
    }
}
