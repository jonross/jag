package com.github.jonross.stuff4j.tbd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import java.util.Optional;

import com.github.jonross.stuff4j.io.UncheckedIO;
import com.github.jonross.stuff4j.lang.Tuple2;

import static com.github.jonross.stuff4j.Stuff4J.$;

/**
 * Prototype.
 * Subject to change.
 */

public class Flows
{
    public static Optional<URL> resource(String path, Class<?> cls) {
        return path.startsWith("/")
                ? Optional.ofNullable(cls.getClassLoader().getResource(path.substring(1)))
                : Optional.ofNullable(cls.getResource(path));
    }

    public static Reader reader(InputStream s) {
        return new InputStreamReader(s);
    }

    public static Reader reader(String s) {
        return new StringReader(s);
    }

    public static Writer writer(File f) {
        return $.get(() -> new FileWriter(f));
    }

    public static String drain(Reader r) {
        return $.apply(() -> r, __ -> UncheckedIO.copy(r, new StringWriter())._2.toString());
    }

    public static InputStream input(URL url) {
        return $.get(url::openStream);
    }

    public static InputStream input(byte[] b) {
        return new ByteArrayInputStream(b);
    }

    public static OutputStream output(File f) {
        return $.get(() -> new FileOutputStream(f));
    }

    public static byte[] drain(InputStream in) {
        return $.apply(() -> in, __ -> copy(in, new ByteArrayOutputStream())._2.toByteArray());
    }

    public static <I extends InputStream, O extends OutputStream> Tuple2<I, O> copy(I in, O out) {
        return $.get(() -> {
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
}
