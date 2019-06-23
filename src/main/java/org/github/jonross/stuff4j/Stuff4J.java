package org.github.jonross.stuff4j;

import org.github.jonross.stuff4j.lang.Tuple1;
import org.github.jonross.stuff4j.lang.Tuple2;
import org.github.jonross.stuff4j.lang.Tuple3;
import org.github.jonross.stuff4j.lang.Tuple4;

/**
 * This class provides a JQuery-like global value that you may import as a shortcut to many Stuff4J utilities.
 * You can also create your own instance of this and name it anything you want.
 */

public class Stuff4J {

    public final static Stuff4J $ = new Stuff4J();

    /** @see {@link Tuple1#of} */
    public <A> Tuple1<A> tuple(A a) {
        return Tuple1.of(a);
    }

    /** @see {@link Tuple2#of} */
    public <A,B> Tuple2<A,B> tuple(A a, B b) {
        return Tuple2.of(a, b);
    }

    /** @see {@link Tuple3#of} */
    public <A,B,C> Tuple3<A,B,C> tuple(A a, B b, C c) {
        return Tuple3.of(a, b, c);
    }

    /** @see {@link Tuple4#of} */
    public <A,B,C,D> Tuple4<A,B,C,D> tuple(A a, B b, C c, D d) {
        return Tuple4.of(a, b, c, d);
    }
}
