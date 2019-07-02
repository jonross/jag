package org.github.jonross.stuff4j;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.sql.SQLException;

import org.github.jonross.stuff4j.function.Throwing;
import org.github.jonross.stuff4j.function.Try;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class TryTests
{
    @Test(description = "Basic mappings and fetch")
    public void testBasics() {
        assertEquals(Try.to(() -> 1).get().intValue(), 1);
        assertEquals(Try.to(() -> "abc").get(), "abc");
        assertEquals(Try.to(() -> "abc").map(String::length).get().intValue(), 3);
        assertEquals(Try.to(() -> "abc").map(String::length).map(x -> x + 2).get().intValue(), 5);
    }

    @Test(description = "Exceptions are trapped")
    public void testException() {
        // This will be re-thrown as-is
        _assertThrows("I failed", RuntimeException.class,
                () -> Try.to(() -> _raise(new RuntimeException("I failed"))).get());
        // This will be wrapped
        _assertThrows("java.lang.Exception: I failed", RuntimeException.class,
                () -> Try.to(() -> _raise(new Exception("I failed"))).get());
        // This will be wrapped in UIOE
        _assertThrows("java.io.IOException: I failed", UncheckedIOException.class,
                () -> Try.to(() -> _raise(new IOException("I failed"))).get());

    }

    private static void _assertThrows(String message, Class<? extends Exception> type,
                                      Throwing.Supplier<?,? extends Exception> s) {
        try {
            s.get();
            fail("Expected exception to be thrown");
        }
        catch (Exception e) {
            assertEquals(e.getClass(), type);
            assertEquals(e.getMessage(), message);
        }
    }

    private static <E extends Exception> Object _raise(E e) throws E {
        throw e;
    }
}
