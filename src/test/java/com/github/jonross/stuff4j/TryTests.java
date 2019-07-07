package org.github.jonross.stuff4j;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.github.jonross.stuff4j.function.Throwing;
import org.github.jonross.stuff4j.function.Try;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
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

    @Test(description = "RuntimeExceptions are rethrown as-is")
    public void testRuntimeException() {
        _assertThrows("I failed", RuntimeException.class,
                () -> Try.to(() -> _raise(new RuntimeException("I failed"))).get());
    }

    @Test(description = "Other exceptions are wrapped")
    public void testOtherExceptions() {
        _assertThrows("java.lang.Exception: I failed", RuntimeException.class,
                () -> Try.to(() -> _raise(new Exception("I failed"))).get());
        _assertThrows("java.io.IOException: I failed", UncheckedIOException.class,
                () -> Try.to(() -> _raise(new IOException("I failed"))).get());
    }

    @Test(description = "Exceptions block further execution")
    public void testBlocked() {
        AtomicInteger x = new AtomicInteger(0);
        Try.to(() -> _raise(new Exception())).map(nil -> x.incrementAndGet()).orElse(() -> 1);
        assertEquals(x.get(), 0);
    }

    @Test(description = "orElse works as expected")
    public void testOrElse() {
        assertEquals(Try.to(() -> "foo").map(x -> x + x).orElse(() -> "bar"), "foofoo");
        assertEquals(Try.to(() -> "foo").map(x -> _raise(new Exception())).orElse(() -> "bar"), "bar");
    }

    @Test(description = "orThrow remaps when expected to")
    public void testOrThrow() {
        class A extends Exception {
            A(String message) { super(message); }
            A(Throwable t) { super(t); }
        }
        class B extends A {
            B(String message) { super(message); }
            B(Throwable t) { super(t); }
        }
        _assertThrows("I failed", B.class,
                () -> Try.to(() -> _raise(new B("I failed"))).orThrow(A.class, A::new));
        _assertThrows("org.github.jonross.stuff4j.TryTests$1A: I failed", B.class,
                () -> Try.to(() -> _raise(new A("I failed"))).orThrow(B.class, B::new));
    }

    @Test(description = "isSuccess and isFailure cause evaluation, once",
        dataProvider = "trueAndFalse")
    public void testIsSuccessFailure(boolean assertSuccess) {
        AtomicInteger x = new AtomicInteger(0);
        var t = Try.to(() -> x.incrementAndGet());
        assertEquals(x.get(), 0);
        if (assertSuccess) {
            assertTrue(t.isSuccess());
        }
        else {
            assertFalse(t.isFailure());
        }
        assertEquals(x.get(), 1);
        // second time, no effect on x
        if (assertSuccess) {
            assertTrue(t.isSuccess());
        }
        else {
            assertFalse(t.isFailure());
        }
        assertEquals(x.get(), 1);
    }

    @Test(description = "onSuccess and onFailure work as expected",
        dataProvider = "trueAndFalse")
    public void testOnSuccessFailure(boolean assertSuccess)
    {
        AtomicInteger onSuccess = new AtomicInteger(0);
        AtomicInteger onFailure = new AtomicInteger(0);
        var t = assertSuccess
                ? Try.to(() -> "foo").map(x -> x)
                : Try.to(() -> "foo").map(x -> _raise(new Exception()));
        t = t.onSuccess(x -> onSuccess.incrementAndGet());
        t = t.onFailure(e -> onFailure.incrementAndGet());
        t.orElse(() -> null);
        if (assertSuccess) {
            assertEquals(onSuccess.get(), 1);
            assertEquals(onFailure.get(), 0);
        }
        else {
            assertEquals(onSuccess.get(), 0);
            assertEquals(onFailure.get(), 1);
        }
    }

    @DataProvider
    public Object[][] trueAndFalse() {
        return new Object[][] { {true}, {false} };
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
