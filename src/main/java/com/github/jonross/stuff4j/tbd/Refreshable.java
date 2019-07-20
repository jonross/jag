package com.github.jonross.stuff4j.tbd;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.github.jonross.stuff4j.function.Throwing;

import static com.github.jonross.stuff4j.function.Throwables.raise;

public class Refreshable<T, E extends Exception> implements Supplier<T>
{
    private volatile T currentValue;
    private Throwing.Supplier<T,? extends Exception> supplier;
    private Consumer<Exception> errorCallback;

    public static Builder every(Duration interval) {
        return new Builder<>(interval, () -> raise(new IllegalStateException("Refreshable is empty")), null);
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
        private Consumer<Exception> errorCallback;

        private Builder(Duration interval, Throwing.Supplier<T,E> supplier, Consumer<Exception> callback) {
            this.interval = interval;
            this.supplier = () -> null;
        }

        private <T2,E2 extends Exception> Builder<T2,E2> Builder(Duration interval, Throwing.Supplier<T2,E2> supplier) {
            return new Builder(interval, supplier, errorCallback);

        }

        public Builder<T,E> onBackgroundError(Consumer<E> errorCallback) {
            return new Builder(interval, supplier, errorCallback);
        }

        public Supplier<T> init() throws E {
            return new Refreshable<T,E>(supplier.get(), supplier, interval, null);
        }
    }
}
