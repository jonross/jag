package jag;

/*
    This is Jag 2.30.1
    For motivations see https://github.com/jonross/jag

    You can get the latest version at
    https://github.com/jonross/jag/tree/master/src/main/java/jag/Jag.java?raw

    Jag 0.x aka jduffel circa 2009 - 2011, unpublished
    Jag 1.x aka underj circa 2014 - 2016, unpublished

    Jag 2.x is copyright (c) 2016 - 2018, Jonathan Ross <jonross@alum.mit.edu>

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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.Duration;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.github.jonross.stuff4j.lang.Tuple2;

import static java.lang.ProcessBuilder.Redirect.INHERIT;
import static java.lang.ProcessBuilder.Redirect.PIPE;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Jag {

    public final static Jag $ = new Jag();

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

    public Reader reader(InputStream s) {
        return new InputStreamReader(s);
    }

    public Reader reader(String s) {
        return new StringReader(s);
    }

    public Writer writer(File f) {
        return unchecked(() -> new FileWriter(f));
    }

    public String drain(Reader r) {
        return closing(r, __ -> {
            return copy(r, new StringWriter())._2.toString();
        });
    }

    public void emit(Writer w, String s) {
        closing(w, __ -> {
            unchecked(() -> w.write(s));
        });
    }

    public <R extends Reader, W extends Writer> Tuple2<R, W> copy(R r, W w) {
        return unchecked(() -> {
            char[] buf = new char[4096];
            while (true) {
                int count = r.read(buf);
                if (count == -1) {
                    break;
                }
                w.write(buf, 0, count);
            }
            return Tuple2.of(r, w);
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

    public OutputStream output(File f) {
        return unchecked(() -> new FileOutputStream(f));
    }

    public byte[] drain(InputStream in) {
        return closing(in, __ -> {
            return copy(in, new ByteArrayOutputStream())._2.toByteArray();
        });
    }

    public void emit(OutputStream out, byte[] b) {
        closing(out, __ -> {
            unchecked(() -> out.write(b));
        });
    }

    public <I extends InputStream, O extends OutputStream> Tuple2<I, O> copy(I in, O out) {
        return unchecked(() -> {
            byte[] buf = new byte[4096];
            while (true) {
                int count = in.read(buf);
                if (count == -1) {
                    break;
                }
                out.write(buf, 0, count);
            }
            return Tuple2.of(in, out);
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // help with files and resources

    public Optional<File> file(String s) {
        File file = new File(s);
        return file.exists() ? Optional.of(file) : Optional.empty();
    }

    public Optional<URL> resource(String path, Class<?> cls) {
        return path.startsWith("/")
                ? Optional.ofNullable(cls.getClassLoader().getResource(path.substring(1)))
                : Optional.ofNullable(cls.getResource(path));
    }

    public Reader reader(File f) {
        return unchecked(() -> new FileReader(f));
    }

    public InputStream input(File f) {
        return unchecked(() -> new FileInputStream(f));
    }

    public InputStream input(URL url) {
        return unchecked(() -> url.openStream());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // help with Closeables

    public <T extends Closeable> void closing(T t, Consumer<T> f) {
        try {
            f.accept(t);
        }
        finally {
            unchecked(() -> t.close());
        }
    }

    public <T extends Closeable,R> R closing(T t, Function<T,R> f) {
        try {
            return f.apply(t);
        }
        finally {
            unchecked(() -> t.close());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // shell helper

    public Shell shell(String... command) {
        return new Shell(command);
    }

    public final class Shell {

        private boolean mustSucceed = false;
        private boolean mergeStderr = false;
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

        public Shell mergeStderr() {
            mergeStderr = true;
            return this;
        }

        public int status() { return _execute(false)._1; }
        public String output() { return _execute(true)._2; }
        public Tuple2<Integer,String> result() { return _execute(true); }

        private Tuple2<Integer,String> _execute(boolean wantOutput) {
            return unchecked(() -> {
                Process p = new ProcessBuilder(command)
                        .redirectOutput(wantOutput ? PIPE : INHERIT)
                        .redirectError(wantOutput & mergeStderr ? PIPE : INHERIT)
                        .start();
                Future<String> stdoutReader =
                        executors.submit(() -> drain(reader(p.getInputStream())));
                Future<String> stderrReader = ! mergeStderr ? null :
                        executors.submit(() -> drain(reader(p.getErrorStream())));
                int exitValue = waitOn($.forever(), p::waitFor).get();
                if (p.exitValue() != 0 && mustSucceed) {
                    die("Command failed: " + Arrays.stream(command).collect(joining(" ")));
                }
                return Tuple2.of(p.exitValue(), stdoutReader.get() + (mergeStderr ? stderrReader.get() : ""));
            });
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // help with things that throw InterruptedException e.g. to implement an accurate Thread.sleep
    //
    //      $.waitFor($.duration("PT2S"), (t, u) -> Thread::sleep(t));

    public Duration duration(String s) {
        return Duration.parse(s);
    }

    public <T, E extends InterruptedException> Optional<T> waitFor(Duration duration, ThrowingBiFunction<Long, TimeUnit, T, E> waiter) {
        long start = System.currentTimeMillis();
        long remain = duration.toMillis();
        while (remain > 0) {
            try {
                return Optional.ofNullable(waiter.apply(remain, TimeUnit.MILLISECONDS));
            }
            catch (InterruptedException e) {
                long now = System.currentTimeMillis();
                remain -= now - start;
                start = now;
            }
        }
        return Optional.empty();
    }

    public <T, E extends InterruptedException> Optional<T> waitOn(Duration duration, ThrowingSupplier<T, E> waiter) {
        return waitFor(duration, (t, u) -> waiter.get());
    }

    public Duration forever() {
        return Duration.ofDays(365 * 1000);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // thread helpers

    private final static AtomicInteger threadSerial = new AtomicInteger();

    private final static ExecutorService executors = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.setName(String.format("jag-%d", threadSerial.incrementAndGet()));
        return t;
    });

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // uncategorized

    public void warn(String message) {
        System.err.println(message);
    }

    public void die(String message) {
        warn(message);
        System.exit(1);
    }

    public <T> Stream<T> stream(Iterable<T> it) {
        return StreamSupport.stream(it.spliterator(), false);
    }

    public <T> Stream<T> stream(Iterator<T> it) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED), false);
    }

}

