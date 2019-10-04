package com.github.jonross.stuff4j.tbd;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.github.jonross.stuff4j.function.Throwing;

public class Refreshable<T, E extends Exception> implements Supplier<T>
{
    private volatile T currentValue;
    private Throwing.Supplier<T,? extends Exception> supplier;
    private Consumer<Exception> errorCallback;

    public static <T,E extends Exception> Builder<T,E> every(Duration interval, Throwing.Supplier<T,E> supplier) {
        return new Builder<>(interval, supplier, null);
    }

    public T get() {
        return currentValue;
    }

    private static class SingletonHolder
    {
        static ScheduledExecutorService DEFAULT_POOL =
                Executors.newScheduledThreadPool(0, Executors.defaultThreadFactory());
    }

    private Refreshable(T initialValue, Throwing.Supplier<T,E> supplier,
                        Duration interval, ScheduledExecutorService executor) throws E {
        currentValue = initialValue;
        this.supplier = supplier;
        (executor != null ? executor : SingletonHolder.DEFAULT_POOL)
                .scheduleWithFixedDelay(this::refresh, interval.toMillis(), interval.toMillis(), TimeUnit.MILLISECONDS);
    }

    private void refresh() {
        try {
            currentValue = supplier.get();
        }
        catch (Exception e) {
            if (errorCallback != null) {
                errorCallback.accept(e);
            }
        }
    }

    public static class Builder<T,E extends Exception> {

        private final Duration interval;
        private Throwing.Supplier<T,E> supplier;
        private BiConsumer<Integer,Exception> errorCallback;

        private Builder(Duration interval, Throwing.Supplier<T,E> supplier, BiConsumer<Integer,Exception> errorCallback) {
            this.interval = interval;
            this.supplier = supplier;
            this.errorCallback = errorCallback;
        }

        public Builder<T,E> onBackgroundError(BiConsumer<Integer, E> errorCallback) {
            return new Builder(interval, supplier, errorCallback);
        }

        public Supplier<T> init() throws E {
            return new Refreshable<T,E>(supplier.get(), supplier, interval, null);
        }
    }
}
