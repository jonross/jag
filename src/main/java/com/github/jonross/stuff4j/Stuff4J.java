package com.github.jonross.stuff4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.jonross.stuff4j.function.Closeables;
import com.github.jonross.stuff4j.function.Throwing;
import com.github.jonross.stuff4j.function.Unchecked;
import com.github.jonross.stuff4j.io.Shell;
import com.github.jonross.stuff4j.io.UncheckedIO;
import com.github.jonross.stuff4j.lang.Pair;
import com.github.jonross.stuff4j.lang.Trio;
import com.github.jonross.stuff4j.lang.Tuple1;
import com.github.jonross.stuff4j.lang.Tuple2;
import com.github.jonross.stuff4j.lang.Tuple3;
import com.github.jonross.stuff4j.lang.Tuple4;

/**
 * This class provides a JQuery-like global value that you may import as a shortcut to many Stuff4J utilities.
 * You can also create your own instance of this and name it anything you want.
 */

public final class Stuff4J {

    public final static Stuff4J $ = new Stuff4J();

    // Tuple shortcuts -------------------------------------------------------------------------------------------------

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

    /** @see {@link Pair#of} */
    public <A,B> Pair<A,B> pair(A a, B b) {
        return Pair.of(a, b);
    }

    /** @see {@link Trio#of} */
    public <A,B,C> Trio<A,B,C> trio(A a, B b, C c) {
        return Trio.of(a, b, c);
    }

    // Unchecked adapter shortcuts -------------------------------------------------------------------------------------

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

    // Unchecked invoker shortcuts -------------------------------------------------------------------------------------

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

    // java.io and UncheckedIO shortcuts -------------------------------------------------------------------------------

    /** @see {@link UncheckedIO#reader(File)}} */
    public FileReader reader(File file) {
        return UncheckedIO.reader(file);
    }

    /** @see {@link UncheckedIO#input(File)}} */
    public FileInputStream input(File file) {
        return UncheckedIO.input(file);
    }

    // TBD if will keep these ------------------------------------------------------------------------------------------

    /** @see {@link Closeables#use} */
    public static <C extends AutoCloseable,E1 extends Exception,E2 extends Exception>
    void use(Throwing.Supplier<C,E1> open, Throwing.Consumer<C,E2> c) {
        Closeables.use(open, c);
    }

    /** @see {@link Closeables#using} */
    public static <C extends AutoCloseable,R,E1 extends Exception,E2 extends Exception>
    R apply(Throwing.Supplier<C,E1> open, Throwing.Function<C,R,E2> f) {
        return Closeables.using(open, f);
    }

    /** @see {@link Shell#Shell} */
    public Shell shell(String... command) {
        return new Shell(command);
    }
}
