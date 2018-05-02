package jpocket;

import java.util.Map;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static jpocket.$.$;

public class StringTests {

    @Test
    public void splitOnCommas() {
        String input = " a, ,b , c";
        assertEquals($.split(input, ","), $.listOf("a", "b", "c"));
        assertEquals($.split(input, ",", 2, true), $.listOf("a", ",b , c"));
        assertEquals($.split(input, ",", 0, false), $.listOf(" a", " ", "b ", " c"));
        assertEquals($.split(input, ",", 2, false), $.listOf(" a", " ,b , c"));
    }

    @Test
    public void splitToMap() {
        Map<String,String> m;
        m = $.split("a = ,  b = 2, ", ",", "=");
        assertEquals(m.get("a"), "");
        assertEquals(m.get("b"), "2");
        assertEquals(m.size(), 2);
    }

}
