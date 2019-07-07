package com.github.jonross.stuff4j;

import com.github.jonross.stuff4j.lang.Tuple2;
import org.testng.annotations.Test;

import static com.github.jonross.stuff4j.Stuff4J.$;
import static org.testng.Assert.assertEquals;

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
        assertEquals($.shell("echo foo; echo bar").result(), Tuple2.of(0, "foo\nbar\n"));
        assertEquals($.shell("echo foo; echo bar; exit 3").result(), Tuple2.of(3, "foo\nbar\n"));
    }

    @Test
    public void testBigOutput() {
        assertEquals($.shell("yes yes | sed 10000q").output().length(), 40000);
    }

    @Test
    public void testUnmergedStderr() {
        assertEquals($.shell("echo foo >&2; echo bar").output(), "bar\n");
    }

    @Test
    public void testMergedStderr() {
        assertEquals($.shell("echo foo >&2; echo bar").mergeStderr().output(), "bar\nfoo\n");
    }
}
