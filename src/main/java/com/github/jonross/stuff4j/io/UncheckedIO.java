package com.github.jonross.stuff4j.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.io.Writer;

import com.github.jonross.stuff4j.lang.Trio;

import static com.github.jonross.stuff4j.Stuff4J.$;

/**
 * Wraps many common java.io operations that throw {@link IOException} to instead throw
 * {@link java.io.UncheckedIOException}.
 */

public class UncheckedIO
{
    /**
     * Same as {@link FileReader#FileReader(File)} but throws {@link UncheckedIOException}.
     */

    public static FileReader reader(File file) {
        return $.apply(FileReader::new, file);
    }

    /**
     * Same as {@link FileWriter#FileWriter(File)} but throws {@link UncheckedIOException}.
     */

    public static FileWriter writer(File file) {
        return $.apply(FileWriter::new, file);
    }

    /**
     * Same as {@link FileInputStream#FileInputStream(File)} but throws {@link UncheckedIOException}.
     */

    public static FileInputStream input(File file) {
        return $.apply(FileInputStream::new, file);
    }

    /**
     * Same as {@link FileOutputStream#FileOutputStream(File)} but throws {@link UncheckedIOException}.
     */

    public static FileOutputStream output(File file) {
        return $.apply(FileOutputStream::new, file);
    }

    /**
     * Completely consume a {@link Reader} and return its contents as a string.
     */

    public static String drain(Reader r) {
        return UncheckedIO.copy(r, new StringWriter())._2.toString();
    }

    /**
     * Completely consume an {@link InputStream} and return its contents as a string.
     */

    public static byte[] drain(InputStream in) {
        return UncheckedIO.copy(in, new ByteArrayOutputStream())._2.toByteArray();
    }

    /**
     * Copy a {@link Reader} to a {@link Writer}.
     * @return A {@link} Tuple3 of the reader, writer, and count of chars copied.
     */

    public static <R extends Reader, W extends Writer> Trio<R, W, Integer> copy(R r, W w) {
        return $.get(() -> {
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

    /**
     * Copy an {@link InputStream} to an {@link OutputStream}.
     * @return A {@link} Tuple3 of the input, output, and count of bytes copied.
     */

    public static <I extends InputStream, O extends OutputStream> Trio<I, O, Integer> copy(I in, O out) {
        return $.get(() -> {
            byte[] buf = new byte[4096];
            int copied = 0;
            while (true) {
                int count = in.read(buf);
                if (count == -1) {
                    break;
                }
                out.write(buf, 0, count);
                copied += count;
            }
            return Trio.of(in, out, copied);
        });
    }
}
