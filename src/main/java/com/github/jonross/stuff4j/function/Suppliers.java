package com.github.jonross.stuff4j.function;

import java.io.Serializable;
import java.util.function.Supplier;

import com.github.jonross.stuff4j.tbd.Ensure;

/**
 * Helpers for working with {@link Supplier Suppliers}.
 *
 * <p>
 * The following methods and classes are from Google Guava and their copyright applies,
 * see https://github.com/google/guava<br/>
 * Copyright (C) 2007 The Guava Authors
 * <ul>
 *     <li>{@link #memoize(Supplier)}</li>
 *     <li>{@link MemoizingSupplier}</li>
 * </ul>
 */

public class Suppliers
{
    /**
     * Returns a supplier which caches the instance retrieved during the first call to {@code get()}
     * and returns that value on subsequent calls to {@code get()}. For details see
     * <a href="https://github.com/google/guava/blob/master/guava/src/com/google/common/base/Suppliers.java">
     * Suppliers.java</a>
     */

    public static <T> Supplier<T> memoize(Supplier<T> delegate) {
        return (Supplier)(delegate instanceof Suppliers.MemoizingSupplier ? delegate :
                new Suppliers.MemoizingSupplier((Supplier) Ensure.notNull(delegate)));
    }

    static class MemoizingSupplier<T> implements Supplier<T>, Serializable {
        final Supplier<T> delegate;
        transient volatile boolean initialized;
        transient T value;
        private static final long serialVersionUID = 0L;

        MemoizingSupplier(Supplier<T> delegate) {
            this.delegate = delegate;
        }

        public T get() {
            if (!this.initialized) {
                synchronized(this) {
                    if (!this.initialized) {
                        T t = this.delegate.get();
                        this.value = t;
                        this.initialized = true;
                        return t;
                    }
                }
            }

            return this.value;
        }

        public String toString() {
            String var1 = String.valueOf(String.valueOf(this.delegate));
            return (new StringBuilder(19 + var1.length())).append("Suppliers.memoize(").append(var1).append(")").toString();
        }
    }
}
