package com.github.jonross.stuff4j;

import java.util.function.Function;

import com.github.jonross.stuff4j.data.Settings;
import com.github.jonross.stuff4j.data.SettingsBuilder;
import org.testng.annotations.Test;

import static com.github.jonross.stuff4j.SettingsTests.TestCase.from;
import static org.testng.Assert.assertThrows;
import static org.testng.AssertJUnit.assertEquals;

public class SettingsTests {

    private Settings s;

    @Test
    public void testIntFromString() {
        from("3").via(s -> s.getInt("")).expect(3);
    }

    /**
     * This encapsulates the two methods that are under test in a test case,
     */

    private static class TestCase
    {
        private SettingsBuilder builder = Settings.builder();
        private Function<SettingsBuilder,Settings> finisher;
        private Function<Settings,Object> getter;

        static TestCase from(String s) {
            return new TestCase(builder -> builder.from(s));
        }

        private TestCase(Function<SettingsBuilder,Settings> finisher) {
            this.finisher = finisher;
        }

        <T> TestCase via(Function<Settings,T> getter) {
            this.getter = settings -> (Object) getter.apply(settings);
            return this;
        }

        void expect(Object result) {
            assertEquals(getter.apply(finisher.apply(builder)), result);
        }
    }
}
