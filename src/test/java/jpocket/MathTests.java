package jpocket;

import org.testng.annotations.Test;

import static jpocket.Utils.toDouble;
import static jpocket.Utils.toFloat;
import static jpocket.Utils.toInt;
import static jpocket.Utils.toLong;
import static org.testng.Assert.assertEquals;

public class MathTests {

    @Test
    public void toInts() {
        assertEquals((int) toInt("3").get(), 3);
        assertEquals((int) toInt("x").orElse(4), 4);
        assertEquals((int) toInt(null).orElse(5), 5);
    }

    @Test
    public void toLongs() {
        assertEquals((long) toLong("3").get(), 3L);
        assertEquals((long) toLong("x").orElse(4L), 4L);
        assertEquals((long) toLong(null).orElse(5L), 5L);
    }

    @Test
    public void toFloats() {
        assertEquals(toFloat("3.0").get(), 3.0f);
        assertEquals(toFloat("x").orElse(4.0f), 4f);
        assertEquals(toFloat(null).orElse(5.0f), 5f);
    }

    @Test
    public void toDoubles() {
        assertEquals(toDouble("3.0").get(), 3.0);
        assertEquals(toDouble("x").orElse(4.0), 4.0);
        assertEquals(toDouble(null).orElse(5.0), 5.0);
    }

}
