package org.github.jonross.stuff4j.tbd;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.github.jonross.stuff4j.function.Throwing;

/**
 * Prototype.
 * Subject to change.
 */

public class Time
{
    public static <T, E extends InterruptedException>
    Optional<T> waitFor(Duration duration, Throwing.BiFunction<Long, TimeUnit, T, E> waiter) {
        long start = System.currentTimeMillis();
        long remain = duration.toMillis();
        while (remain > 0) {
            try {
                return Optional.ofNullable(waiter.apply(remain, TimeUnit.MILLISECONDS));
            }
            catch (InterruptedException e) {
                long now = System.currentTimeMillis();
                remain -= now - start;
                start = now;
            }
        }
        return Optional.empty();
    }

    public static <T, E extends InterruptedException> Optional<T>
    waitOn(Duration duration, Throwing.Supplier<T, E> waiter) {
        return waitFor(duration, (t, u) -> waiter.get());
    }

    public static Duration forever() {
        return Duration.ofDays(365 * 1000);
    }
}
