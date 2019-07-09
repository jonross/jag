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
}
