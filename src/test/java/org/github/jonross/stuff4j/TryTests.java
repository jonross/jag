package org.github.jonross.stuff4j;

import org.github.jonross.stuff4j.function.Try;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TryTests
{
    @Test
    public void testValues() {
        assertEquals(Try.to(() -> 1).get().intValue(), 1);
        assertEquals(Try.to(() -> "abc").get(), "abc");
    }

    @Test
    public void testMapping() {
        assertEquals(Try.to(() -> "abc").map(String::length).get().intValue(), 3);
        assertEquals(Try.to(() -> "abc").map(String::length).map(x -> x + 2).get().intValue(), 5);
    }
}
