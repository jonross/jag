package jpocket;

/*
    This is JPocket 0.6.25
    For motivations see https://github.com/jonross/jpocket

    You can get the latest version at
    https://github.com/jonross/jpocket/tree/master/src/main/java/jpocket/Utils.java?raw

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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.ProcessBuilder.Redirect.INHERIT;
import static java.lang.ProcessBuilder.Redirect.PIPE;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public final class $ extends Base {

    public final static $ $ = new $();

    private final static ExecutorService executors = Executors.newCachedThreadPool();

    // Versions of Supplier, Consumer, BiConsumer, Function, and BiFunction that can throw exceptions.

    @FunctionalInterface
    public interface ThrowingSupplier<T,E extends Exception> {
        T get() throws E;
    }

    @FunctionalInterface
    public interface ThrowingConsumer<T,E extends Exception> {
        void accept(T t) throws E;
    }

    @FunctionalInterface
    public interface ThrowingBiConsumer<T,U,E extends Exception> {
        void accept(T t, U u) throws E;
    }

    @FunctionalInterface
    public interface ThrowingFunction<T,R,E extends Exception> {
        R apply(T t) throws E;
    }

    @FunctionalInterface
    public interface ThrowingBiFunction<T,U,R,E extends Exception> {
        R apply(T t, U u) throws E;
    }

    @FunctionalInterface
    public interface ThrowingRunnable<E extends Exception> {
        void run() throws E;
    }

    // Create / read input, throwing unchecked exceptions.

    public File file(String s) {
        return new JPFile(s);
    }

    public Reader reader(File f) {
        return unchecked(() -> new JPFileReader(f));
    }

    public Reader reader(String s) {
        return new JPStringReader(s);
    }

    public Reader reader(InputStream s) {
        return new JPInputStreamReader(s);
    }

    public Reader buffered(Reader r) {
        return new JPBufferedReader(r);
    }

    public InputStream input(File f) {
        return unchecked(() -> new JPFileInputStream(f));
    }

    public InputStream input(byte[] b) {
        return new JPByteArrayInputStream(b);
    }

    public InputStream buffered(InputStream s) {
        return new JPBufferedInputStream(s);
    }

    public String drain(Reader r) {
        StringWriter w = new StringWriter();
        copy(r, w);
        return w.toString();
    }

    public byte[] drain(InputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copy(in, out);
        return out.toByteArray();
    }

    public void copy(Reader r, Writer w) {
        unchecked(() -> _copy(r, (buf, len) -> w.write(buf, 0, len)));
    }

    public void copy(InputStream in, OutputStream out) {
        unchecked(() -> _copy(in, (buf, len) -> out.write(buf, 0, len)));
    }

    // Operate on a Closeable and automatically close it when done.

    public <T extends Closeable,R> R closing(T t, Function<T,R> f) {
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

    public <T,E extends Exception> T unchecked(ThrowingSupplier<T,E> s) {
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

    public <E extends Exception> void unchecked(ThrowingRunnable<E> r) {
        unchecked(() -> { r.run(); return null; });
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

        @Override
        public boolean equals(Object o) {
            if (! (o instanceof Pair)) {
                return false;
            }
            Pair<?,?> that = (Pair<?,?>) o;
            return Objects.equals(this.first, that.first) &&
                    Objects.equals(this.second, that.second);
        }

        @Override public int hashCode() {
            return Objects.hash(first, second);
        }
    }

    // Common string splits

    public List<String> splitCsv(String s) {
        if (s.trim().equals("")) {
            return Collections.emptyList();
        }
        return Arrays.stream(s.split(","))
                .map(String::trim)
                .collect(toList());
    }

    public Map<String,String> splitKv(String s) {
        Map<String,String> m = new HashMap<>();
        Arrays.stream(s.split(","))
                .filter(kv -> kv.contains("="))
                .forEach(kv -> {
                    String[] a = kv.split("=", 2);
                    m.put(a[0].trim(), a[1].trim());
                });
        return m;
    }

    public List<String> splitLines(String s) {
        List<String> lines = Arrays.stream(s.trim().split("\n")).map(String::trim).collect(toList());
        return lines.size() == 1 && lines.get(0).equals("") ? Collections.emptyList() : lines;
    }

    //
    //

    public Optional<Integer> toInt(String s) {
        return numeric(s, Integer::parseInt);
    }

    public Optional<Long> toLong(String s) {
        return numeric(s, Long::parseLong);
    }

    public Optional<Float> toFloat(String s) {
        return numeric(s, Float::parseFloat);
    }

    public Optional<Double> toDouble(String s) {
        return numeric(s, Double::parseDouble);
    }

    private static <T extends Number> Optional<T> numeric(String s, Function<String,T> f) {
        try {
            return s == null ? Optional.empty() : Optional.of(f.apply(s));
        }
        catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    // Run shell commands, returning exit status, output or both.
    // If command is a single string with spaces it is run with bash -c.

    public Shell shell(String... command) {
        return new Shell(command);
    }

    public final class Shell {

        private boolean mustSucceed = false;
        private String[] command;

        Shell(String[] command) {
            if (command.length == 1 && command[0].contains(" ")) {
                command = new String[]{"bash", "-c", command[0]};
            }
            this.command = command;
        }

        public Shell orDie() {
            mustSucceed = true;
            return this;
        }

        public int status() { return _execute(false).first; }
        public String output() { return _execute(true).second; }
        public Pair<Integer,String> result() { return _execute(true); }

        private Pair<Integer,String> _execute(boolean wantOutput) {
            return unchecked(() -> {
                Process p = new ProcessBuilder(command).redirectOutput(wantOutput ? PIPE : INHERIT).start();
                Future<?> f = executors.submit(() -> calmly(() -> p.waitFor()));
                try {
                    String stdout = closing(reader(p.getInputStream()), r -> drain(r));
                    if (p.exitValue() != 0 && mustSucceed) {
                        die("Command failed: " + Arrays.stream(command).collect(joining(" ")));
                    }
                    return Pair.of(p.exitValue(), stdout);
                }
                finally {
                    f.get();
                }
            });
        }

    }

    public <T,E extends InterruptedException> T calmly(ThrowingSupplier<T,E> s) {
        while (true) {
            try {
                return s.get();
            }
            catch (InterruptedException e) {
            }
        }
    }

    public <E extends InterruptedException> void calmly(ThrowingRunnable<E> r) {
        unchecked(() -> { r.run(); return null; });
    }

    public void warn(String message) {
        System.err.println(message);
    }

    public void die(String message) {
        warn(message);
        System.exit(1);
    }

    public String sprintf(String format, Object... args) {
        return String.format(format, args);
    }

    protected static void _copy(InputStream in, ThrowingBiConsumer<byte[],Integer,IOException> out) throws IOException {
        byte[] buf = new byte[4096];
        while (true) {
            int count = in.read(buf);
            if (count == -1) {
                return;
            }
            out.accept(buf, count);
        }
    }

    protected static void _copy(Reader r, ThrowingBiConsumer<char[],Integer,IOException> out) throws IOException {
        char[] buf = new char[4096];
        while (true) {
            int count = r.read(buf);
            if (count == -1) {
                return;
            }
            out.accept(buf, count);
        }
    }

    public <T,E extends InterruptedException> Optional<T> wait(Duration duration, ThrowingSupplier<T,E> s) {
        long start = System.currentTimeMillis();
        while (! duration.isNegative() && ! duration.isZero()) {
            try {
                return Optional.ofNullable(s.get());
            }
            catch (InterruptedException e) {
                long end = System.currentTimeMillis();
                duration = duration.minusMillis(end - start);
                start = end;
            }
        }
        return Optional.empty();
    }

    public <E extends InterruptedException> void wait(Duration duration, ThrowingRunnable<E> r) {
        wait(duration, () -> { r.run(); return null; });
    }

    public Duration forever() {
        return Duration.ofDays(365 * 1000);
    }

    // Stream support

    public <T> Stream<T> stream(Iterable<T> it) {
        return StreamSupport.stream(it.spliterator(), false);
    }

    public <T> Stream<T> stream(Iterator<T> it) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED), false);
    }

}

class Base {

    // For future extension of java.io expressions

    public static class JPFile extends File {
        public JPFile(String pathname) {
            super(pathname);
        }
    }

    public static class JPFileReader extends FileReader {
        public JPFileReader(File file) throws FileNotFoundException {
            super(file);
        }
    }

    public static class JPStringReader extends StringReader {
        public JPStringReader(String s) {
            super(s);
        }
    }

    public static class JPInputStreamReader extends InputStreamReader {
        public JPInputStreamReader(InputStream in) {
            super(in);
        }
    }

    public static class JPBufferedReader extends BufferedReader {
        public JPBufferedReader(Reader in) {
            super(in);
        }
    }

    public static class JPFileInputStream extends FileInputStream {
        public JPFileInputStream(File file) throws FileNotFoundException {
            super(file);
        }
    }

    public static class JPByteArrayInputStream extends ByteArrayInputStream {
        public JPByteArrayInputStream(byte[] buf) {
            super(buf);
        }
    }

    public static class JPBufferedInputStream extends BufferedInputStream {
        public JPBufferedInputStream(InputStream in) {
            super(in);
        }
    }
}
