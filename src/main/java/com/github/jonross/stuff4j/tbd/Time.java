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
    Optional<T> nointr(Duration duration, Throwing.Function<Long, T, E> waiter) {
        return nointr(duration, (t, u) -> waiter.apply(t));
    }

    public static <T, E extends InterruptedException>
    Optional<T> nointr(Duration duration, Throwing.BiFunction<Long, TimeUnit, T, E> waiter) {
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
}
