package jpocket;

import java.io.FilterInputStream;
import java.io.FilterReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

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

}
