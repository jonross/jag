package com.github.jonross.stuff4j;

import java.io.FilterInputStream;
import java.io.FilterReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.regex.Pattern;

import com.github.jonross.stuff4j.function.Throwing;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

class Utils {

    final static class TestReader extends FilterReader {
        private boolean closed;
        TestReader(Reader s) {
            super(s);
        }
        public void close() throws IOException {
            super.close();
            closed = true;
        }
        boolean wasClosed() {
            return closed;
        }
    }

    final static class TestInputStream extends FilterInputStream {
        private boolean closed;
        TestInputStream(InputStream s) {
            super(s);
        }
        public void close() throws IOException {
            super.close();
            closed = true;
        }
        boolean wasClosed() {
            return closed;
        }
    }

    static void assertThrows(Class<? extends Exception> klass, String message, Throwing.Runnable r) {
        try {
            r.run();
            fail("Expected exception of type " + klass);
        }
        catch (Exception e) {
            assertTrue(klass.isAssignableFrom(e.getClass()),
                    "Expected an instance of " + klass + " but got " + e);
            assertTrue(Pattern.compile(message).matcher(e.getMessage()).matches(),
                    "Expected message to match '" + message + "' but got " + e.getMessage());
        }
    }
}
