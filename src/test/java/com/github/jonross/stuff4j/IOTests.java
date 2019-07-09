package com.github.jonross.stuff4j;

import java.io.File;
import java.io.IOException;

import com.github.jonross.stuff4j.Utils.TestInputStream;
import com.github.jonross.stuff4j.Utils.TestReader;
import com.github.jonross.stuff4j.io.Resources;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class IOTests {

    private final Stuff4J $ = new Stuff4J();

    @Test
    public void drainReader() throws IOException {
        TestReader r = new TestReader($.reader("foo"));
        assertEquals($.drain(r), "foo");
        assertFalse(r.wasClosed());
    }

    @Test
    public void drainInputStream() throws IOException {
        TestInputStream in = new TestInputStream($.input("foo".getBytes()));
        assertEquals($.drain(in), "foo".getBytes());
        assertFalse(in.wasClosed());
    }

    @Test
    public void testFiles() throws IOException {
        File f = File.createTempFile("jag-temp", "txt");
        f.deleteOnExit();
        $.use(() -> $.writer(f), w -> w.write("foo"));
        assertEquals($.drain($.reader(f)), "foo");
        $.use(() -> $.output(f), out -> out.write("bar".getBytes()));
        assertEquals($.drain($.input(f)), "bar".getBytes());
    }

    @Test
    public void testResource() {
        var r = Resources.relativeTo(getClass());
        assertFalse(r.find("noresource.txt").isPresent());
        assertEquals($.drain(r.open("resource.txt")),
                "This is a resource.\n".getBytes());
        assertEquals($.drain(r.open("/com/github/jonross/stuff4j/resource.txt")),
                "This is a resource.\n".getBytes());
    }
}


