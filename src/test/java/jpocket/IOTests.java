package jpocket;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import jpocket.Utils.TestInputStream;
import jpocket.Utils.TestReader;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import static jpocket.$.$;

public class IOTests {

    @Test
    public void drainReader() throws IOException {
        TestReader r = new TestReader($.reader("foo"));
        assertEquals($.drain(r), "foo");
        assertTrue(r.wasClosed());
    }

    @Test
    public void drainInputStream() throws IOException {
        TestInputStream in = new TestInputStream($.input("foo".getBytes()));
        assertEquals($.drain(in), "foo".getBytes());
        assertTrue(in.wasClosed());
    }

    @Test
    public void testFiles() throws IOException {
        File f = File.createTempFile("jpocket-temp", "txt");
        f.deleteOnExit();
        $.emit($.writer(f), "foo");
        assertEquals($.drain($.reader(f)), "foo");
        $.emit($.output(f), "bar".getBytes());
        assertEquals($.drain($.input(f)), "bar".getBytes());
    }

    @Test
    public void testResource() {
        assertFalse($.resource("noresource.txt", getClass()).isPresent());
        assertEquals($.drain($.input($.resource("resource.txt", getClass()).get())),
                "This is a resource.\n".getBytes());
        assertEquals($.drain($.input($.resource("/jpocket/resource.txt", getClass()).get())),
                "This is a resource.\n".getBytes());
    }
}


