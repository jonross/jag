package com.github.jonross.stuff4j;

import java.util.Map;
import java.util.function.Function;

import com.github.jonross.stuff4j.data.Settings;
import com.github.jonross.stuff4j.data.SettingsBuilder;
import org.testng.annotations.Test;

import static com.github.jonross.stuff4j.SettingsTests.TestCase.flat;
import static com.github.jonross.stuff4j.SettingsTests.TestCase.from;
import static com.github.jonross.stuff4j.SettingsTests.TestCase.nest;
import static com.github.jonross.stuff4j.Utils.map;
import static java.util.Collections.singletonMap;
import static org.testng.Assert.assertThrows;
import static org.testng.AssertJUnit.assertEquals;

public class SettingsTests {

    private Settings s;

    @Test
    public void testInt() {
        from(flat("x", "3")).via(m -> m.getInt("x")).expect(3);
        from(flat("x", 3)).via(m -> m.getInt("x")).expect(3);
        from(nest(map("x", map("y", "3")))).via(m -> m.getInt("x.y")).expect(3);
        from(nest(map("x", map("y", "3")))).via(m -> m.getInt("x.y")).expect(3);
    }

    /**
     * This encapsulates the two methods that are under test in a test case,
     */

    static class TestCase
    {
        private SettingsBuilder builder;
        private Function<Settings,Object> getter;

        private TestCase(SettingsBuilder builder) {
            this.builder = builder;
        }

        static TestCase from(SettingsBuilder builder) {
            return new TestCase(builder);
        }

        static SettingsBuilder.Flat flat(String key, Object value) {
            return Settings.flat(singletonMap(key, value)::get);
        }

        static SettingsBuilder.Nested nest(Map<String,Object> map) {
            return Settings.nested(map, ".");
        }

        TestCase via(Function<Settings,Object> getter) {
            this.getter = settings -> getter.apply(settings);
            return this;
        }

        void expect(Object result) {
            assertEquals(getter.apply(builder.build()), result);
        }
    }
}
