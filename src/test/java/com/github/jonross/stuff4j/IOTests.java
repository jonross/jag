package com.github.jonross.stuff4j;

import java.io.File;
import java.io.IOException;

import com.github.jonross.stuff4j.Utils.TestInputStream;
import com.github.jonross.stuff4j.Utils.TestReader;
import com.github.jonross.stuff4j.tbd.Flows;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class IOTests {

    private final Stuff4J $ = new Stuff4J();

    @Test
    public void drainReader() throws IOException {
        TestReader r = new TestReader($.reader("foo"));
        assertEquals(Flows.drain(r), "foo");
        assertFalse(r.wasClosed());
    }

    @Test
    public void drainInputStream() throws IOException {
        TestInputStream in = new TestInputStream($.input("foo".getBytes()));
        assertEquals(Flows.drain(in), "foo".getBytes());
        assertFalse(in.wasClosed());
    }

    @Test
    public void testFiles() throws IOException {
        File f = File.createTempFile("jag-temp", "txt");
        f.deleteOnExit();
        $.use(() -> $.writer(f), w -> w.write("foo"));
        assertEquals(Flows.drain($.reader(f)), "foo");
        $.use(() -> $.output(f), out -> out.write("bar".getBytes()));
        assertEquals(Flows.drain($.input(f)), "bar".getBytes());
    }

    @Test
    public void testResource() {
        assertFalse(Flows.resource("noresource.txt", getClass()).isPresent());
        assertEquals(Flows.drain($.input(Flows.resource("resource.txt", getClass()).get())),
                "This is a resource.\n".getBytes());
        assertEquals(Flows.drain($.input(Flows.resource("/com/github/jonross/stuff4j/resource.txt", getClass()).get())),
                "This is a resource.\n".getBytes());
    }
}


