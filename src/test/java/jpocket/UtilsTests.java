package jpocket;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import org.testng.annotations.Test;

import static jpocket.Utils.closing;
import static jpocket.Utils.drain;
import static jpocket.Utils.input;
import static jpocket.Utils.reader;
import static jpocket.Utils.shell;
import static jpocket.Utils.splitCsv;
import static jpocket.Utils.splitKv;
import static jpocket.Utils.splitLines;
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

    @Test
    public void temp(){
        List<String> tags = splitLines(shell("git tag").orDie().output());
    }

    void foo() {
        closing(input(new File("")), f -> drain(f));
        // $.closing($.input(new File("")), $::drain);
    }
}


