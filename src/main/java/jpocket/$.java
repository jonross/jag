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
import java.io.FileReader;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.ProcessBuilder.Redirect.INHERIT;
import static java.lang.ProcessBuilder.Redirect.PIPE;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public final class $ {

    public final static $ $ = new $();

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // mirror common functional interfaces with versions that can throw

    @FunctionalInterface
    public interface ThrowingSupplier<T, E extends Exception> {
        T get() throws E;
    }

    @FunctionalInterface
    public interface ThrowingConsumer<T, E extends Exception> {
        void accept(T t) throws E;
    }

    @FunctionalInterface
    public interface ThrowingBiConsumer<T, U, E extends Exception> {
        void accept(T t, U u) throws E;
    }

    @FunctionalInterface
    public interface ThrowingFunction<T, R, E extends Exception> {
        R apply(T t) throws E;
    }

    @FunctionalInterface
    public interface ThrowingBiFunction<T, U, R, E extends Exception> {
        R apply(T t, U u) throws E;
    }

    @FunctionalInterface
    public interface ThrowingRunnable<E extends Exception> {
        void run() throws E;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // wrap any value-providing or void expression to hide checked exceptions

    public <T, E extends Exception> T unchecked(ThrowingSupplier<T, E> s) {
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // help with Readers and Writers

    public Reader buffered(Reader r) {
        return new BufferedReader(r);
    }

    public Reader reader(File f) {
        return unchecked(() -> new FileReader(f));
    }

    public Reader reader(InputStream s) {
        return new InputStreamReader(s);
    }

    public Reader reader(String s) {
        return new StringReader(s);
    }

    public String drain(Reader r) {
        return copy(r, new StringWriter()).second.toString();
    }

    public <R extends Reader, W extends Writer> Pair<R, W> copy(R r, W w) {
        return unchecked(() -> {
            char[] buf = new char[4096];
            while (true) {
                int count = r.read(buf);
                if (count == -1) {
                    break;
                }
                w.write(buf, 0, count);
            }
            return pair(r, w);
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // help with InputStreams and OutputStreams

    public InputStream buffered(InputStream s) {
        return new BufferedInputStream(s);
    }

    public InputStream input(byte[] b) {
        return new ByteArrayInputStream(b);
    }

    public InputStream input(File f) {
        return unchecked(() -> new FileInputStream(f));
    }

    public byte[] drain(InputStream in) {
        return copy(in, new ByteArrayOutputStream()).second.toByteArray();
    }

    public <I extends InputStream, O extends OutputStream> Pair<I, O> copy(I in, O out) {
        return unchecked(() -> {
            byte[] buf = new byte[4096];
            while (true) {
                int count = in.read(buf);
                if (count == -1) {
                    break;
                }
                out.write(buf, 0, count);
            }
            return pair(in, out);
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // help with Closeables

    // Operate on a Closeable and automatically close it when done.

    public <T extends Closeable,R> R closing(T t, Function<T,R> f) {
        try {
            return f.apply(t);
        }
        finally {
            unchecked(() -> t.close());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // support two-valued returns

    public <A, B> Pair<A, B> pair(A a, B b) {
        return new Pair(a, b);
    }

    public final static class Pair<A,B> {

        public final A first;
        public final B second;

        public Pair(A a, B b) {
            first = a;
            second = b;
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
                    f.get();
                    if (p.exitValue() != 0 && mustSucceed) {
                        die("Command failed: " + Arrays.stream(command).collect(joining(" ")));
                    }
                    return pair(p.exitValue(), stdout);
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

    public String sprintf(String format, Object... args) {
        return String.format(format, args);
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // numeric conversion

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // regex helpers

    public Pattern re(String s) {
        return Pattern.compile(s);
    }

    public Optional<String> extract(Pattern pattern, int group, String target) {
        Matcher m = pattern.matcher(target);
        return Optional.ofNullable(m.find() ? m.group(group) : null);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // thread helpers

    private final static AtomicInteger threadSerial = new AtomicInteger();

    private final static ExecutorService executors = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.setName($.sprintf("jpocket-%d", threadSerial.incrementAndGet()));
        return t;
    });

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // uncategorized

    public File file(String s) {
        return new File(s);
    }

    public void warn(String message) {
        System.err.println(message);
    }

    public void die(String message) {
        warn(message);
        System.exit(1);
    }

    public <T> List<T> list(T... a) { return Arrays.asList(a); }
    public <T> Stream<T> stream(Iterable<T> it) { return StreamSupport.stream(it.spliterator(), false); }
    public <T> Stream<T> stream(Iterator<T> it) { return StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED), false); }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // experimental, may be removed at any time

    private <T,E extends Exception> T chain(T t, ThrowingConsumer<T,E>... actions) {
        for (ThrowingConsumer<T,E> action: actions) {
            unchecked(() -> action.accept(t));
        }
        return t;
    }

}

