package jpocket;

/*
    This is JPocket 0.6.25
    For motivations see https://github.com/jonross/jpocket

    You can get the latest version at
    https://github.com/jonross/jpocket/src/main/java/jpocket/Utils.java?raw

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

import java.io.InputStream;
import java.io.Reader;

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

    // Consume input, throwing unchecked exceptions.

    public static byte[] slurp(InputStream s) {
        return _unchecked(() -> ByteStreams.toByteArray(s));
    }

    public static String slurp(Readable r) {
        return _unchecked(() -> CharStreams.toString(r));
    }

    // Wrap any Supplier-compatible expression to convert checked exceptions to unchecked.

    private static <T,E extends Exception> T _unchecked(ThrowingSupplier<T,E> s) {
        try {
            return s.get();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
