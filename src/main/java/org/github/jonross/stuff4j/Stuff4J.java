package org.github.jonross.stuff4j;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.github.jonross.stuff4j.function.Throwing;
import org.github.jonross.stuff4j.function.Unchecked;
import org.github.jonross.stuff4j.lang.Tuple1;
import org.github.jonross.stuff4j.lang.Tuple2;
import org.github.jonross.stuff4j.lang.Tuple3;
import org.github.jonross.stuff4j.lang.Tuple4;

/**
 * This class provides a JQuery-like global value that you may import as a shortcut to many Stuff4J utilities.
 * You can also create your own instance of this and name it anything you want.
 */

public final class Stuff4J {

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

    /** @see {@link Unchecked#runnable */
    public <E extends Exception> Runnable runnable(Throwing.Runnable<E> r) {
        return Unchecked.runnable(r);
    }

    /** @see {@link Unchecked#supplier */
    public <T,E extends Exception> Supplier<T> supplier(Throwing.Supplier<T,E> s) {
        return Unchecked.supplier(s);
    }

    /** @see {@link Unchecked#consumer */
    public <T,E extends Exception> Consumer<T> consumer(Throwing.Consumer<T,E> c) {
        return Unchecked.consumer(c);
    }

    /** @see {@link Unchecked#biConsumer */
    public <T,U,E extends Exception> BiConsumer<T,U> biConsumer(Throwing.BiConsumer<T,U,E> c) {
        return Unchecked.biConsumer(c);
    }

    /** @see {@link Unchecked#function */
    public <T,R,E extends Exception> Function<T,R> function(Throwing.Function<T,R,E> f) {
        return Unchecked.function(f);
    }

    /** @see {@link Unchecked#biFunction */
    public <T,U,R,E extends Exception> BiFunction<T,U,R> biFunction(Throwing.BiFunction<T,U,R,E> f) {
        return Unchecked.biFunction(f);
    }

    /** @see {@link Unchecked#run */
    public <E extends Exception> void run(Throwing.Runnable<E> r) {
        Unchecked.run(r);
    }

    /** @see {@link Unchecked#get} */
    public <T,E extends Exception> T get(Throwing.Supplier<T,E> s) {
        return Unchecked.get(s);
    }

    /** @see {@link Unchecked#accept} */
    public <T,E extends Exception> void accept(Throwing.Consumer<T,E> c, T t) {
        Unchecked.accept(c, t);
    }

    /** @see {@link Unchecked#accept} */
    public <T,U,E extends Exception> void accept(Throwing.BiConsumer<T,U,E> c, T t, U u) {
        Unchecked.accept(c, t, u);
    }

    /** @see {@link Unchecked#apply} */
    public <T,R,E extends Exception> R apply(Throwing.Function<T,R,E> f, T t) {
        return Unchecked.apply(f, t);
    }

    /** @see {@link Unchecked#apply} */
    public <T,U,R,E extends Exception> R apply(Throwing.BiFunction<T,U,R,E> f, T t, U u) {
        return Unchecked.apply(f, t, u);
    }
}
