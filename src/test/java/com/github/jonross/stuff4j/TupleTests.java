package com.github.jonross.stuff4j;

import static com.github.jonross.stuff4j.Stuff4J.$;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.util.Arrays;
import java.util.List;

import com.github.jonross.stuff4j.lang.Tuple1;
import com.github.jonross.stuff4j.lang.Tuple2;
import com.github.jonross.stuff4j.lang.Tuple3;
import com.github.jonross.stuff4j.lang.Tuple4;
import org.testng.annotations.Test;

public class TupleTests
{
    @Test
    public void testTuple1()
    {
        Tuple1<String> t = $.tuple("a");
        assertEquals(t.hashCode(), Arrays.hashCode(new Object[]{t._1}));
        assertEquals(t.arity(), 1);

        assertEquals(t._1, "a");
        assertEquals(t.toList(), List.of("a"));
        
        assertEquals($.tuple(null), $.tuple(null));
        assertEquals($.tuple("a"), $.tuple("a"));

        assertNotEquals($.tuple("a"), $.tuple("b"));
    }

    @Test
    public void testTuple2()
    {
        Tuple2<String,Integer> t = $.tuple("a", 2);
        assertEquals(t.hashCode(), Arrays.hashCode(new Object[]{t._1, t._2}));
        assertEquals(t.arity(), 2);

        assertEquals(t._1, "a");
        assertEquals(t._2, (Integer) 2);
        assertEquals(t.toList(), List.of("a", 2));

        assertEquals($.tuple(null, null), $.tuple(null, null));
        assertEquals($.tuple(null, 2), $.tuple(null, 2));
        assertEquals($.tuple("a", null), $.tuple("a", null));
        assertEquals($.tuple("a", 2), $.tuple("a", 2));

        assertNotEquals($.tuple(null, 2), $.tuple(null, 3));
        assertNotEquals($.tuple("a", null), $.tuple("b", null));
        assertNotEquals($.tuple("a", 2), $.tuple("b", 2));
        assertNotEquals($.tuple("a", 2), $.tuple("a", 3));
    }

    @Test
    public void testTuple3()
    {
        Tuple3<String,Integer,Long> t = $.tuple("a", 2, 3L);
        assertEquals(t.hashCode(), Arrays.hashCode(new Object[]{t._1, t._2, t._3}));
        assertEquals(t.arity(), 3);

        assertEquals(t._1, "a");
        assertEquals(t._2, (Integer) 2);
        assertEquals(t._3, (Long) 3L);
        assertEquals(t.toList(), List.of("a", 2, 3L));

        assertEquals($.tuple(null, null, null), $.tuple(null, null, null));
        assertEquals($.tuple(null, 2, 3L), $.tuple(null, 2, 3L));
        assertEquals($.tuple("a", null, 3L), $.tuple("a", null, 3L));
        assertEquals($.tuple("a", 2, null), $.tuple("a", 2, null));

        assertNotEquals($.tuple(null, 2, 3L), $.tuple(null, 3, 3L));
        assertNotEquals($.tuple(null, 2, 3L), $.tuple(null, 2, 4L));
        assertNotEquals($.tuple("a", null, 3L), $.tuple("b", null, 3L));
        assertNotEquals($.tuple("a", null, 3L), $.tuple("a", null, 4L));
        assertNotEquals($.tuple("a", 2, null), $.tuple("b", 2, null));
        assertNotEquals($.tuple("a", 2, null), $.tuple("a", 3, null));
        assertNotEquals($.tuple("a", 2, 3L), $.tuple("b", 2, 3L));
        assertNotEquals($.tuple("a", 2, 3L), $.tuple("a", 3, 3L));
        assertNotEquals($.tuple("a", 2, 3L), $.tuple("a", 2, 4L));
    }

    @Test
    public void testTuple4()
    {
        Tuple4<String,Integer,Long,Double> t = $.tuple("a", 2, 3L, 4.0);
        assertEquals(t.hashCode(), Arrays.hashCode(new Object[]{t._1, t._2, t._3, t._4}));
        assertEquals(t.arity(), 4);

        assertEquals(t._1, "a");
        assertEquals(t._2, (Integer) 2);
        assertEquals(t._3, (Long) 3L);
        assertEquals(t._4, (Double) 4.0);
        assertEquals(t.toList(), List.of("a", 2, 3L, 4.0));

        assertEquals($.tuple(null, null, null, null), $.tuple(null, null, null, null));
        assertEquals($.tuple(null, 2, 3L, 4.0), $.tuple(null, 2, 3L, 4.0));
        assertEquals($.tuple("a", null, 3L, 4.0), $.tuple("a", null, 3L, 4.0));
        assertEquals($.tuple("a", 2, 3L, null), $.tuple("a", 2, 3L, null));

        assertNotEquals($.tuple(null, 2, 3L, 4.0), $.tuple(null, 3, 3L, 4.0));
        assertNotEquals($.tuple(null, 2, 3L, 4.0), $.tuple(null, 2, 4L, 4.0));
        assertNotEquals($.tuple(null, 2, 3L, 4.0), $.tuple(null, 2, 3L, 5.0));
        assertNotEquals($.tuple("a", null, 3L, 4.0), $.tuple("b", null, 3L, 4.0));
        assertNotEquals($.tuple("a", null, 3L, 4.0), $.tuple("a", null, 4L, 4.0));
        assertNotEquals($.tuple("a", null, 3L, 4.0), $.tuple("a", null, 3L, 5.0));
        assertNotEquals($.tuple("a", 2, null, 4.0), $.tuple("b", 2, null, 4.0));
        assertNotEquals($.tuple("a", 2, null, 4.0), $.tuple("a", 3, null, 4.0));
        assertNotEquals($.tuple("a", 2, null, 4.0), $.tuple("a", 2, null, 5.0));
        assertNotEquals($.tuple("a", 2, 3L, 4.0), $.tuple("b", 2, 3L, 4.0));
        assertNotEquals($.tuple("a", 2, 3L, 4.0), $.tuple("a", 3, 3L, 4.0));
        assertNotEquals($.tuple("a", 2, 3L, 4.0), $.tuple("a", 2, 4L, 4.0));
        assertNotEquals($.tuple("a", 2, 3L, 4.0), $.tuple("a", 2, 3L, 5.0));
    }
}
