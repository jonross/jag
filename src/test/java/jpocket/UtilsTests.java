package jpocket;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

import com.google.common.base.Joiner;
import org.testng.annotations.Test;

import static jpocket.Utils.consume;
import static jpocket.Utils.drain;
import static jpocket.Utils.input;
import static jpocket.Utils.reader;
import static jpocket.Utils.splitCsv;
import static jpocket.Utils.splitKv;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class UtilsTests {

    @Test
    public void drainReader() throws IOException {
        Reader r = reader("foo");
        assertEquals(drain(r), "foo");
        assertTrue(r.ready());
    }

    @Test
    public void drainInput() throws IOException {
        MyInputStream s = new MyInputStream(input("foo".getBytes()));
        assertEquals(drain(s), "foo".getBytes());
        assertFalse(s.isClosed());
    }

    @Test(expectedExceptions = {IOException.class},
          expectedExceptionsMessageRegExp = "Stream closed")
    public void consumeReader() throws IOException {
        Reader r = reader("foo");
        assertEquals(consume(r), "foo");
        r.ready();
    }

    @Test
    public void consumeInput() throws IOException {
        MyInputStream s = new MyInputStream(input("foo".getBytes()));
        assertEquals(consume(s), "foo".getBytes());
        assertTrue(s.isClosed());
    }

    private final static class MyInputStream extends FilterInputStream {
        private boolean closed;
        MyInputStream(InputStream s) {
            super(s);
        }
        public void close() throws IOException {
            super.close();
            closed = true;
        }
        boolean isClosed() {
            return closed;
        }
    }

    @Test
    public void splitOnCommas() {
        assertEquals(Joiner.on(':').join(splitCsv(" a, , b, c")), "a::b:c");
    }

    @Test
    public void splitToMap() {
        Map<String,String> m = splitKv("a =,  b = 2, ");
        assertEquals(m.get("a"), "");
        assertEquals(m.get("b"), "2");
        assertEquals(m.size(), 2);
    }
}
