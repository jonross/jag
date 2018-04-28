package jpocket;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static jpocket.$.$;

public class ShellTests {

    @Test
    public void testOutput() {
        assertEquals($.shell("echo foo; echo bar").output(), "foo\nbar\n");
    }

    @Test
    public void testStatus() {
        assertEquals($.shell("exit 0").status(), 0);
        assertEquals($.shell("exit 3").status(), 3);
    }

    @Test
    public void testOutputAndStatus() {
        assertEquals($.shell("echo foo; echo bar").result(), $.pair(0, "foo\nbar\n"));
        assertEquals($.shell("echo foo; echo bar; exit 3").result(), $.pair(3, "foo\nbar\n"));
    }

    @Test
    public void testBigOutput() {
        assertEquals($.shell("yes yes | sed 10000q").output().length(), 40000);
    }
}
