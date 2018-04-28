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

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import static jpocket.$.$;

public class UtilsTests {

    @Test
    public void drainReaderDontClose() throws IOException {
        Reader r = $.reader("foo");
        assertEquals($.drain(r), "foo");
        assertTrue(r.ready());
    }

    @Test(expectedExceptions = {IOException.class},
          expectedExceptionsMessageRegExp = "Stream closed")
    public void drainReaderAndClose() throws IOException {
        Reader r = $.reader("foo");
        assertEquals($.closing(r, r0 -> $.drain(r0)), "foo");
        assertFalse(r.ready());
    }

    @Test
    public void drainInputDontClose() throws IOException {
        MyInputStream s = new MyInputStream($.input("foo".getBytes()));
        assertEquals($.drain(s), "foo".getBytes());
        assertFalse(s.isClosed());
    }

    @Test
    public void drainInputAndClose() throws IOException {
        MyInputStream s = new MyInputStream($.input("foo".getBytes()));
        assertEquals($.closing(s, s0 -> $.drain(s0)), "foo".getBytes());
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
        _assertEntries($.splitCsv(" a, , b, c"), "a", "", "b", "c");
    }

    @Test
    public void splitToMap() {
        Map<String,String> m = $.splitToMap("a =,  b = 2, ");
        assertEquals(m.get("a"), "");
        assertEquals(m.get("b"), "2");
        assertEquals(m.size(), 2);
    }

    @Test
    public void splitLinesBasic() {
        _assertEntries($.splitNonblankLines("\n a  \n b \n\nc\n"), "a", "b", "c");
    }

    @Test
    public void splitLinesBlankOutput() {
        assertEquals($.splitNonblankLines("\n \n").size(), 0);
    }

    private static <T> void _assertEntries(Iterable<T> actual, T... expected) {
        assertEquals($.stream(actual).toArray(), expected);
    }
}


