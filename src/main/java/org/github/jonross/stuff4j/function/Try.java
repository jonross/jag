package org.github.jonross.stuff4j.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Supplier;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Monadic exception handling; cf Vavr, Atlassian Fugue, Cyclops-React.
 */

@ParametersAreNonnullByDefault
public class Try<T>
{
    /** Logic to run in this step of the chain */
    private final Supplier<Result<T>> step;

    private Try(Supplier<Result<T>> step) {
        this.step = step;
    }

    /**
     * Begin a chain of computation steps that may throw exceptions.  See class comment for details.
     *
     * @param s Yields the first computation result.
     * @param <T> Type of the first result.
     * @param <E> Exception type that may be thrown
     *
     * @return An instance of {@link Try} that may be mapped to another computation, or directed to return its
     * result / throw an exception.
     */

    public static <T,E extends Exception> Try<T> to(Throwing.Supplier<T,E> s) {
        // To start the chain, build a step that ignores a fake, non-null prior value and just runs the supplier
        return new Try(_buildStep(nil -> s.get(), () -> new Result(new Object(), null)));
    }

    /**
     * Perform an additional computation step that may throw exceptions.  See class comment for details.  If any
     * prior step threw an exception, or returned null and <code>allowingNull</code> was not called during setup,
     * this is a no-op.
     *
     * @param f Transforms the prior step's result into the result from this step.
     * @param <U> Type of this step's result.
     * @param <E> Exception type that may be thrown
     *
     * @return Same as {@link Try#to}
     */

    public <U,E extends Exception> Try<U> map(Throwing.Function<? super T, ? extends U,E> f) {
        // To run this step, fetch the result from prior step and using the function
        return new Try(_buildStep(f, step));
    }

    /**
     * Same as {@link #map}, but accepts a function that returns a {@link Try} and removes one level of nesting
     * from the resut.
     */

    public <U,E extends Exception> Try<U> flatMap(Throwing.Function<? super T, Try<? extends U>,E> f) {
        // Same logic as map, but add an extra indirection to unwrap the nested result
        Supplier<Result<Try<? extends U>>> f2 = _buildStep(f, step);
        return new Try<>(() -> {
            Result<Try<? extends U>> result = f2.get();
            return result.error == null ? new Result(result.value, null) : new Result(null, result.error);
        });
    }

    /**
     * Get the result of the last computation, or throw the first exception thrown by a prior step, or throw if
     * any result was null and <code>allowingNull</code> was not called during setup.
     */

    public T get() {
        Result<T> result = step.get();
        if (result.error != null) {
            if (result.error instanceof RuntimeException) {
                throw ((RuntimeException) result.error);
            }
            if (result.error instanceof IOException) {
                throw new UncheckedIOException((IOException) result.error);
            }
            throw new RuntimeException(result.error);
        }
        return result.value;
    }

    /**
     * Everything else in this class is boilerplate, this is the brains of the operation.  Build the Function for
     * a given step's result that transforms the result of the prior step.
     */

    private static <T,U,E extends Exception> Supplier<Result<U>>
        _buildStep(Throwing.Function<? super T, ? extends U, E> f,
                   Supplier<Result<T>> priorStep)
    {
        return () ->
        {
            Result<T> prior = priorStep.get();
            if (prior.error != null) {
                return new Result(null, prior.error);
            }
            try {
                return new Result(f.apply(prior.value), null);
            }
            catch (Exception e) {
                return new Result(null, e);
            }
        };
    }

    /**
     * Class to carry result out of each nested step.
     */

    private static class Result<T>
    {
        @Nullable final T value;
        @Nullable final Exception error;

        Result(@Nullable T value, @Nullable Exception error) {
            this.value = value;
            this.error = error;
        }
    }
}