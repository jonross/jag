package com.github.jonross.stuff4j.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.io.Writer;

import com.github.jonross.stuff4j.function.Throwing;
import com.github.jonross.stuff4j.lang.Trio;

import static com.github.jonross.stuff4j.Stuff4J.$;

/**
 * Wraps many common java.io operations that throw {@link IOException} to instead throw
 * {@link java.io.UncheckedIOException}.
 */

public class UncheckedIO
{
    public static FileReader reader(File file) {
        return _get(() -> new FileReader(file));
    }

    public static FileInputStream input(File file) {
        return _get(() -> new FileInputStream(file));
    }

    /**
     * Copy a {@link Reader} to a {@link Writer}.
     * @return A {@link} Trio of the reader, writer, and count of chars copied.
     */

    public static <R extends Reader, W extends Writer> Trio<R, W, Integer> copy(R r, W w) {
        return _get(() -> {
            char[] buf = new char[4096];
            int copied = 0;
            while (true) {
                int count = r.read(buf);
                if (count == -1) {
                    break;
                }
                w.write(buf, 0, count);
                copied += count;
            }
            return Trio.of(r, w, copied);
        });
    }

    private static <T,E extends IOException> T _get(Throwing.Supplier<T,E> s) {
        try {
            return s.get();
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
