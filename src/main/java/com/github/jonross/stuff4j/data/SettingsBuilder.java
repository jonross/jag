package com.github.jonross.stuff4j.data;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static java.util.Objects.requireNonNull;

@ParametersAreNonnullByDefault
public abstract class SettingsBuilder<T extends SettingsBuilder<T>>
{
    protected String valueSeparator;

    SettingsBuilder() {
        this(",");
    }

    SettingsBuilder(String valueSeparator) {
        this.valueSeparator = requireNonNull(valueSeparator);
    }

    public abstract Settings build();

    /** Enable subclass covariance on base class method return types */
    protected abstract T getThis();

    T withValueSeparator(String valueSeparator) {
        this.valueSeparator = requireNonNull(valueSeparator);
        return getThis();
    }

    public final static class Flat extends SettingsBuilder<Flat>
    {
        private Function<String,?> getOne;
        @Nullable
        private Function<String, List<?>> getMulti;

        Flat(Function<String,?> getOne) {
            this.getOne = requireNonNull(getOne);
            this.getMulti = null;
        }

        public Flat withMultiValueGetter(Function<String,List<?>> getMulti) {
            this.getMulti = requireNonNull(getMulti);
            return this;
        }

        public Settings build() {
            return new Settings(getOne, getMulti, valueSeparator);
        }

        @Override
        protected Flat getThis() {
            return this;
        }
    }

    public final static class Nested extends SettingsBuilder<Nested>
    {
        private Map<String,?> map;
        private String keySeparator;
        private boolean failFastOnValueConflict;

        Nested(Map<String,?> map, String keySeparator) {
            this.map = requireNonNull(map);
            this.keySeparator = requireNonNull(keySeparator);
        }

        Nested failFastOnValueConflict(boolean failFastOnValueConflict) {
            this.failFastOnValueConflict = failFastOnValueConflict;
            return this;
        }

        @Override
        protected Nested getThis() {
            return this;
        }

        public Settings build() {
            var pattern = Pattern.compile(Pattern.quote(keySeparator));
            return new Settings(s -> {
                Object node = map;
                String[] parts = pattern.split(s);
                if (parts.length > 1) {
                    for (int i = 0; i < parts.length - 1; i++) {
                        node = ((Map<String,?>) node).get(parts[i]);
                        if (! (node instanceof Map)) {
                            if (failFastOnValueConflict) {
                                throw new IllegalArgumentException("Nested lookup found non-map at key section '"
                                        + parts[i] + "'");
                            }
                            return null;
                        }
                    }
                }
                return ((Map<String,?>) node).get(parts[parts.length - 1]);
            }, null, valueSeparator);
        }
    }
}

