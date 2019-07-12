package com.github.jonross.stuff4j.data;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * This class provides the parsing behind {@link Settings}.  For convenience, they can be used directly on
 * individual values.
 */

public class Parse {

    /**
     * Implementation of {@link Settings#getInt}.  Same as calling {@link Integer#parseInt} but is tolerant of
     * leading or trailing whitespace.
     */

    public static int toInt(String s) {
        return Integer.parseInt(_trim(s));
    }

    public static List<Integer> toInts(String s, String separator) {
        return _toList(s, separator, Parse::toInt);
    }

    /**
     * Implementation of {@link Settings#getLong}.  Same as calling {@link Long#parseLong} but is tolerant of
     * leading or trailing whitespace.
     */

    public static long toLong(String s) {
        return Long.parseLong(_trim(s));
    }

    public static List<Long> toLongs(String s, String separator) {
        return _toList(s, separator, Parse::toLong);
    }

    /**
     * Implementation of {@link Settings#getDouble}.  Same as calling {@link Double#parseDouble} but is tolerant of
     * leading or trailing whitespace.
     */

    public static double toDouble(String s) {
        return Double.parseDouble(_trim(s));
    }

    public static List<Double> toDoubles(String s, String separator) {
        return _toList(s, separator, Parse::toDouble);
    }

    /**
     * Implementation of {@link Settings#getEnum}.  Same as calling {@link Enum#valueOf} but is tolerant of
     * leading or trailing whitespace.
     */

    public static <T extends Enum<T>> T toEnum(String s, Class<T> klass) {
        return Enum.valueOf(klass, _trim(s));
    }

    public static <T extends Enum<T>> List<T> toEnums(String s, String separator, Class<T> klass) {
        return _toList(s, separator, x -> toEnum(x, klass));
    }

    /**
     * Implementation of <code>parseSomethings</code> methods.
     */

    private static <T> List<T> _toList(String s, String separator, Function<String,T> parser) {
        return Arrays.asList(s.split(separator))
                .stream()
                .map(parser)
                .collect(toList());
    }

    /**
     * Null-tolerant version of {@link String#trim}.  Don't hide nulls from JDK parsing methods because
     * they may not throw NPEs, whereas we would throw NPE if we tried to trim(null).
     */

    private static String _trim(String s) {
        return s != null ? s.trim() : null;
    }
}
