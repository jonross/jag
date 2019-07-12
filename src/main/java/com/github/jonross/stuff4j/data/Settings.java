package com.github.jonross.stuff4j.data;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.ParametersAreNonnullByDefault;

import static java.util.Objects.requireNonNull;

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

@ParametersAreNonnullByDefault
public class Settings
{
    private final Function<String,?> getOne;
    private final Function<String,?> getMulti;
    private final String valueSeparator;

    /**
     * For use by {@link SettingsBuilder.Flat} and {@link SettingsBuilder.Nested}
     */

    Settings(Function<String, ?> getOne, Function<String, ?> getMulti, String valueSeparator) {
        this.getOne = getOne;
        this.getMulti = getMulti != null ? getMulti : getOne;
        this.valueSeparator = valueSeparator;
    }

    public static SettingsBuilder.Flat flat(Function<String,?> getter) {
        return new SettingsBuilder.Flat(getter);
    }

    public static SettingsBuilder.Nested flat(Map<String,?> map, String keySeparator) {
        return new SettingsBuilder.Nested(map, keySeparator);
    }

    public String get(String key) {
        return _parse(key, String.class, s -> s, false, null);
    }

    public String get(String key, String defaultValue) {
        return _parse(key, String.class, s -> s, true, defaultValue);
    }

    public int getInt(String key) {
        return _parse(key, Integer.class, Integer::parseInt, false, null);
    }

    public int getInt(String key, int defaultValue) {
        return _parse(key, Integer.class, Integer::parseInt, true, defaultValue);
    }

    public long getLong(String key) {
        return _parse(key, Long.class, Long::parseLong, false, null);
    }

    public long getLong(String key, long defaultValue) {
        return _parse(key, Long.class, Long::parseLong, true, defaultValue);
    }

    public double getDouble(String key) {
        return _parse(key, Double.class, Double::parseDouble, false, null);
    }

    public double getDouble(String key, double defaultValue) {
        return _parse(key, Double.class, Double::parseDouble, true, defaultValue);
    }

    public <T extends Enum<T>> T getEnum(String key, Class<T> enumType) {
        return _parse(key, Enum.class, value -> Enum.valueOf(enumType, value), false, null);
    }

    public <T extends Enum<T>> T getEnum(String key, Class<T> enumType, T defaultValue) {
        return _parse(key, Enum.class, value -> Enum.valueOf(enumType, value), true, defaultValue);
    }

    private <T> T _parse(String key, Class<?> resultClass, Function<String,T> parser,
                         boolean hasDefault, T defaultValue) {
        Object value = getOne.apply(key);
        if (value == null) {
            if (hasDefault) {
                return defaultValue;
            }
            throw new IllegalArgumentException("No value for key " + key);
        }
        return parser.apply(value.toString());
    }
}
