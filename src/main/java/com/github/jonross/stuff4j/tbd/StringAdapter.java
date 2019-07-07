package com.github.jonross.stuff4j.tbd;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.annotation.Nullable;

/**
 * Prototype.
 * Subject to change.
 *
 * <p></p>Fit a string to other data types.  Useful for converting configuration settings, command-line values,
 * servlet parameters et cetera to ints, longs, enums and more.
 *
 * <p><em>Why this doesn't use <code>Optional</code></em>.  You generally want to either supply a default value,
 * or have the source string unconditionally converted to the target type; if conversion fails there is no recovery
 * (don't use the default.)  Also, {@link Optional#orElseThrow()} has its own exception type; there is no way to
 * preserve the exception arising from the conversion failure.
 */

public class StringAdapter {

    private final Function<String,String> mapper;

    /**
     * Create a default string adapter, that applies no special processing before conversion.
     */

    public StringAdapter() {
        this(key -> key);
    }

    /**
     * Create a string adapter that first applies a mapping function to the supplied string.  Use this for
     * pulling from maps, servlet parameters etc.
     */

    public StringAdapter(Function<String,String> mapper) {
        this.mapper = mapper;
    }

    public String get(String key) {
        return _parse(key, false, null, s -> s);
    }

    public int toInt(String key) {
        return _parse(key, false, null, Integer::parseInt);
    }

    public int toInt(String key, int defaultValue) {
        return _parse(key, true, defaultValue, Integer::parseInt);
    }

    public long toLong(String key) {
        return _parse(key, false, null, Long::parseLong);
    }

    public long toLong(String key, long defaultValue) {
        return _parse(key, true, defaultValue, Long::parseLong);
    }

    public double toDouble(String key) {
        return _parse(key, false, null, Double::parseDouble);
    }

    public double toDouble(String key, double defaultValue) {
        return _parse(key, true, defaultValue, Double::parseDouble);
    }

    public <T extends Enum<T>> T toEnum(String key, Class<T> enumType) {
        return _parse(key, false, null, value -> Enum.valueOf(enumType, value));
    }

    public <T extends Enum<T>> T toEnum(String key, Class<T> enumType, T defaultValue) {
        return _parse(key, true, defaultValue, value -> Enum.valueOf(enumType, value));
    }

    private <T> T _parse(String key, boolean hasDefault, T defaultValue, Function<String,T> parser) {
        String actualKey = mapper.apply(key);
        if (actualKey == null) {
            if (hasDefault) {
                return defaultValue;
            }
            throw new IllegalArgumentException("No value for key " + key);
        }
        return parser.apply(actualKey);
    }
}
