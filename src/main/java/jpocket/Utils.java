package jpocket;

/*
    This is JPocket 0.6.25
    For motivations see https://github.com/jonross/jpocket

    You can get the latest version at
    https://github.com/jonross/jpocket/tree/master/src/main/java/jpocket/Utils.java?raw

    The dependencies (for Gradle) are

    compile 'com.fasterxml.jackson.core:jackson-databind:2.8.8'
    compile 'com.google.guava:guava:19.0'
    compile 'org.slf4j:slf4j-api:1.7.21'
    compile 'org.slf4j:slf4j-log4j12:1.7.21'

    Copyright (c) 2016 - 2018, Jonathan Ross <jonross@alum.mit.edu>

    Permission is hereby granted, free of charge, to any person obtaining a
    copy of this software and associated documentation files (the "Software"),
    to deal in the Software without restriction, including without limitation
    the rights to use, copy, modify, merge, publish, distribute, sublicense,
    and/or sell copies of the Software, and to permit persons to whom the
    Software is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included
    in all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
    THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
    FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
    DEALINGS IN THE SOFTWARE.
 */

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.google.common.base.Splitter;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;

public class Utils {

    // Versions of Supplier, Consumer, Function, and BiFunction that can throw exceptions.

    @FunctionalInterface
    public interface ThrowingSupplier<T,E extends Exception> {
        T get() throws E;
    }

    @FunctionalInterface
    public interface ThrowingConsumer<T,E extends Exception> {
        void accept(T t) throws E;
    }

    @FunctionalInterface
    public interface ThrowingFunction<T,R,E extends Exception> {
        R apply(T t) throws E;
    }

    @FunctionalInterface
    public interface ThrowingBiFunction<T,U,R,E extends Exception> {
        R apply(T t, U u) throws E;
    }

    // Create / read input, throwing unchecked exceptions.
    // The "drain" forms don't close their input, while the "consume" forms do.

    public static File file(String s) {
        return new File(s);
    }

    public static Reader reader(File file) {
        return unchecked(() -> new FileReader(file));
    }

    public static Reader reader(InputStream s) {
        return new InputStreamReader(s);
    }

    public static Reader buffer(Reader r) {
        return new BufferedReader(r);
    }

    public static InputStream buffer(InputStream s) {
        return new BufferedInputStream(s);
    }

    public static byte[] drain(InputStream s) {
        return unchecked(() -> ByteStreams.toByteArray(s));
    }

    public static String drain(Readable r) {
        return unchecked(() -> CharStreams.toString(r));
    }

    public static byte[] consume(InputStream s) {
        return closing(s, x -> unchecked(() -> ByteStreams.toByteArray(x)));
    }

    public static <R extends Readable & Closeable> String consume(R r) {
        return closing(r, x -> unchecked(() -> CharStreams.toString(r)));
    }

    public static <T extends Closeable,R> R closing(T t, Function<T,R> f) {
        try {
            return f.apply(t);
        }
        finally {
            try {
                t.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Wrap any Supplier-compatible expression, converting checked exceptions to unchecked.

    private static <T,E extends Exception> T unchecked(ThrowingSupplier<T,E> s) {
        try {
            return s.get();
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // The canonical 2-tuple... how many of these are there

    public final static class Pair<A,B> {

        public final A first;
        public final B second;

        public Pair(A a, B b) {
            first = a;
            second = b;
        }

        public static <A,B> Pair<A,B> of(A a, B b) {
            return new Pair(a, b);
        }
    }

    // Common string splits

    public List<String> splitCsv(String s) {
        return Splitter.on(',').trimResults().splitToList(s);
    }

    public Map<String,String> splitKv(String s) {
        return Splitter.on(',').trimResults().omitEmptyStrings().withKeyValueSeparator('=').split(s);
    }
}
