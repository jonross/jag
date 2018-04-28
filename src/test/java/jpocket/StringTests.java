package jpocket;

import java.util.Map;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static jpocket.$.$;

public class StringTests {

    @Test
    public void splitOnCommas() {
        assertEquals($.splitCsv(" a, , b, c"), $.list("a", "", "b", "c"));
    }

    @Test
    public void splitToMap() {
        Map<String,String> m = $.splitToMap("a =,  b = 2, ");
        assertEquals(m.get("a"), "");
        assertEquals(m.get("b"), "2");
        assertEquals(m.size(), 2);
    }

    @Test
    public void splitLinesBasic() {
        assertEquals($.splitNonblankLines("\n a  \n b \n\nc\n"), $.list("a", "b", "c"));
    }

    @Test
    public void splitLinesBlankOutput() {
        assertEquals($.splitNonblankLines("\n \n").size(), 0);
    }
}
