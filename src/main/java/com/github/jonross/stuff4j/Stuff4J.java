package com.github.jonross.stuff4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;
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

    /** @see StringReader#StringReader(String) */
    public static Reader reader(String s) {
        return new StringReader(s);
    }

    /** @see InputStreamReader#InputStreamReader(InputStream) */
    public static Reader reader(InputStream s) {
        return new InputStreamReader(s);
    }

    /** @see {@link UncheckedIO#reader(File)}} */
    public FileReader reader(File file) {
        return UncheckedIO.reader(file);
    }

    /** @see {@link UncheckedIO#writer(File)}} */
    public FileWriter writer(File file) {
        return UncheckedIO.writer(file);
    }

    /** @see java.io.ByteArrayInputStream#ByteArrayInputStream(byte[]) */
    public static InputStream input(byte[] b) {
        return new ByteArrayInputStream(b);
    }

    public static InputStream input(URL url) {
        return $.get(url::openStream);
    }

    /** @see {@link UncheckedIO#input(File)}} */
    public FileInputStream input(File file) {
        return UncheckedIO.input(file);
    }

    /** @see {@link UncheckedIO#output(File)}} */
    public static OutputStream output(File file) {
        return UncheckedIO.output(file);
    }

    /** @see {@link UncheckedIO#copy(Reader, Writer)} */
    public static <R extends Reader, W extends Writer> Trio<R, W, Integer> copy(R r, W w) {
        return UncheckedIO.copy(r, w);
    }

    /** @see {@link UncheckedIO#copy(InputStream, OutputStream)} */
    public static <I extends InputStream, O extends OutputStream> Trio<I, O, Integer> copy(I in, O out) {
        return UncheckedIO.copy(in, out);
    }

    /** @see {@link UncheckedIO#drain(Reader)} */
    public static String drain(Reader r) {
        return UncheckedIO.drain(r);
    }

    /** @see {@link UncheckedIO#drain(InputStream)} */
    public static byte[] drain(InputStream in) {
        return UncheckedIO.drain(in);
    }

    // TBD if will keep these ------------------------------------------------------------------------------------------

    /** @see {@link Closeables#use} */
    public static <C extends AutoCloseable,E extends Exception>
    void use(Throwing.Supplier<C,E> open, Throwing.Consumer<C,E> c) {
        Closeables.use(open, c);
    }

    /** @see {@link Closeables#using} */
    public static <C extends AutoCloseable,R,E extends Exception>
    R apply(Throwing.Supplier<C,E> open, Throwing.Function<C,R,E> f) {
        return Closeables.using(open, f);
    }

    /** @see {@link Shell#Shell} */
    public Shell shell(String... command) {
        return new Shell(command);
    }
}
