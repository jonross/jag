package jpocket;

import java.util.Map;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static jpocket.$.$;

public class StringTests {

    @Test
    public void splitOnCommas() {
        String input = " a, ,b , c";
        assertEquals($.split(input, ",", 0, true), $.list("a", "b", "c"));
        assertEquals($.split(input, ",", 2, true), $.list("a", ",b , c"));
        assertEquals($.split(input, ",", 0, false), $.list(" a", " ", "b ", " c"));
        assertEquals($.split(input, ",", 2, false), $.list(" a", " ,b , c"));
    }

    @Test
    public void splitToMap() {
        Map<String,String> m;
        m = $.split("a = ,  b = 2, ", ",", "=", true);
        assertEquals(m.get("a"), "");
        assertEquals(m.get("b"), "2");
        assertEquals(m.size(), 2);
        m = $.split("a = ,  b = 2, ", ",", "=", false);
        assertEquals(m.get("a"), " ");
        assertEquals(m.get("b"), " 2");
        assertEquals(m.size(), 2);
    }

}
