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
import java.time.Duration;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.github.jonross.stuff4j.Stuff4J;
import org.github.jonross.stuff4j.io.Shell;
import org.github.jonross.stuff4j.function.Throwing;
import org.github.jonross.stuff4j.lang.Tuple2;

import static java.util.stream.Collectors.joining;

public class Jag {

    private final static Stuff4J $ = new Stuff4J();

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // help with Readers and Writers

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // help with things that throw InterruptedException e.g. to implement an accurate Thread.sleep
    //
    //      $.waitFor($.duration("PT2S"), (t, u) -> Thread::sleep(t));

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    public <T> Stream<T> stream(Iterable<T> it) {
        return StreamSupport.stream(it.spliterator(), false);
    }

    public <T> Stream<T> stream(Iterator<T> it) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED), false);
    }

}

