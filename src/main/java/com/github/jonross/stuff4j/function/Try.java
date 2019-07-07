package com.github.jonross.stuff4j.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Monadic exception handling; cf Vavr, Atlassian Fugue, Cyclops-React.  Lazily evaluates computational steps unless
 * method notes that it forces evaluation.
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
        if (result.error == null) {
            return result.value;
        }
        if (result.error instanceof RuntimeException) {
            throw ((RuntimeException) result.error);
        }
        if (result.error instanceof IOException) {
            throw new UncheckedIOException((IOException) result.error);
        }
        throw new RuntimeException(result.error);
    }

    /**
     * If the chain of computations was successful, return the result, otherwise return the result of invoking
     * the supplier.
     */

    public T orElse(Supplier<T> s) {
        Result<T> result = step.get();
        return result.error == null ? result.value : s.get();
    }

    /**
     * This does the same as {@link #get}, but if the class of exception held on error doesn't match the passed
     * class, wrap it using the function specified.
     */

    public <E extends Exception> T orThrow(Class<E> type, Function<? super Exception, ? extends E> wrap) throws E {
        Result<T> result = step.get();
        if (result.error == null) {
            return result.value;
        }
        if (type.isAssignableFrom(result.error.getClass())) {
            throw type.cast(result.error);
        }
        throw wrap.apply(result.error);
    }

    /**
     * Returns true if the Try contains a value, false if it contains an exception.  Note: like {@link #get} this
     * forces evaluation.
     */

    public boolean isSuccess() {
        return step.get().error == null;
    }

    /**
     * Returns false if the Try contains a value, true if it contains an exception.  Note: like {@link #get} this
     * forces evaluation.
     */

    public boolean isFailure() {
        return step.get().error != null;
    }

    /**
     * Call a consumer to peek at the result, if the Try contains a result.
     */

    public Try<T> onSuccess(Consumer<? super T> c) {
        return new Try<>(() -> {
            Result<T> result = step.get();
            if (result.error == null) {
                c.accept(result.value);
            }
            return result;
        });
    }

    /**
     * Call a consumer to peek at the error, if the Try contains an exception.
     */

    public Try<T> onFailure(Consumer<? super Exception> c) {
        return new Try<>(() -> {
            Result<T> result = step.get();
            if (result.error != null) {
                c.accept(result.error);
            }
            return result;
        });
    }

    /**
     * This is the brains of the operation.  Build the Function for a given step's result that transforms
     * the result of the prior step.
     */

    private static <T,U,E extends Exception> Supplier<Result<U>>
        _buildStep(Throwing.Function<? super T, ? extends U, E> f,
                   Supplier<Result<T>> priorStep)
    {
        // Must memoize in case e.g. caller uses isSuccess then get.
        return Suppliers.memoize(() ->
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
        });
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
