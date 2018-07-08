package jpocket;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static jpocket.Jag.$;

public class MathTests {

    @Test
    public void toInts() {
        assertEquals((int) $.toInt("3").getAsInt(), 3);
        assertEquals((int) $.toInt("x").orElse(4), 4);
        assertEquals((int) $.toInt(null).orElse(5), 5);
    }

    @Test
    public void toLongs() {
        assertEquals((long) $.toLong("3").getAsLong(), 3L);
        assertEquals((long) $.toLong("x").orElse(4L), 4L);
        assertEquals((long) $.toLong(null).orElse(5L), 5L);
    }

    @Test
    public void toDoubles() {
        assertEquals($.toDouble("3.0").getAsDouble(), 3.0);
        assertEquals($.toDouble("x").orElse(4.0), 4.0);
        assertEquals($.toDouble(null).orElse(5.0), 5.0);
    }

}
