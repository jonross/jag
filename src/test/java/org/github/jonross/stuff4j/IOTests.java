package org.github.jonross.stuff4j;

import java.io.File;
import java.io.IOException;

import jag.Jag;
import org.github.jonross.stuff4j.Utils.TestInputStream;
import org.github.jonross.stuff4j.Utils.TestReader;
import org.github.jonross.stuff4j.io.Flows;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class IOTests {

    private final Jag $ = new Jag();
    private final Stuff4J $$ = new Stuff4J();

    @Test
    public void drainReader() throws IOException {
        TestReader r = new TestReader(Flows.reader("foo"));
        assertEquals(Flows.drain(r), "foo");
        assertTrue(r.wasClosed());
    }

    @Test
    public void drainInputStream() throws IOException {
        TestInputStream in = new TestInputStream(Flows.input("foo".getBytes()));
        assertEquals(Flows.drain(in), "foo".getBytes());
        assertTrue(in.wasClosed());
    }

    @Test
    public void testFiles() throws IOException {
        File f = File.createTempFile("jag-temp", "txt");
        f.deleteOnExit();
        $$.accept(() -> Flows.writer(f), w -> w.write("foo"));
        assertEquals(Flows.drain(Flows.reader(f)), "foo");
        $$.accept(() -> Flows.output(f), out -> out.write("bar".getBytes()));
        assertEquals(Flows.drain(Flows.input(f)), "bar".getBytes());
    }

    @Test
    public void testResource() {
        assertFalse(Flows.resource("noresource.txt", getClass()).isPresent());
        assertEquals(Flows.drain(Flows.input(Flows.resource("resource.txt", getClass()).get())),
                "This is a resource.\n".getBytes());
        assertEquals(Flows.drain(Flows.input(Flows.resource("/jag/resource.txt", getClass()).get())),
                "This is a resource.\n".getBytes());
    }
}


