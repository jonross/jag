package org.github.jonross.stuff4j.function;

import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * WORK IN PROGRESS
 */

public class Try<T>
{
    /** Logic to run in this step of the chain */
    private final Function<Context,Result<T>> step;

    /** Context built during chain construction, outermost will be passed to the step function later */
    private final Context context;

    /** What the first step operates on, since there is no prior step */
    private final static Object NOTHING = new Object();

    private Try(@Nonnull Context context, @Nonnull Function<Context,Result<T>> step) {
        this.context = context;
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

    public static <T,E extends Exception> Try<T> to(@Nonnull Throwing.Supplier<T,E> s) {
        // To start the chain, build a step that ignores a fake prior value and just runs the supplier
        return new Try(new Context(null), _buildStep(nil -> s.get(), nil -> new Result(NOTHING, null)));
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
     * @return Same as {@link #to(Throwing.Supplier)}
     */

    public <U,E extends Exception> Try<U> map(@Nonnull Throwing.Function<? super T, ? extends U,E> f) {
        // To run this step, fetch the result from prior step and apply the function
        return new Try(context, _buildStep(f, step));
    }

    public <U,E extends Exception> Try<U> flatMap(@Nonnull Throwing.Function<? super T, Try<? extends U>,E> f) {
        // Same logic as map, but add an extra indirection to unwrap the nested result
        Function<Context,Result<Try<? extends U>>> f2 = _buildStep(f, step);
        return new Try<>(context, c -> {
            Result<Try<? extends U>> result = f2.apply(c);
            return result.error == null ? new Result(result.value, null) : new Result(null, result.error);
        });
    }

    /**
     * Get the result of the last computation, or throw the first exception thrown by a prior step, or throw if
     * any result was null and <code>allowingNull</code> was not called during setup.
     */

    public T get() {
        Result<T> result = step.apply(context);
        if (result.error != null) {
            if (result.error instanceof RuntimeException) {
                throw ((RuntimeException) result.error);
            }
            throw new RuntimeException(result.error);
        }
        return result.value;
    }

    /**
     * Everything else in this class is boilerplate, this is the brains of the operation.  Build the Function for
     * a given step's result that transforms the result of the prior step.
     */

    private static <T,U,E extends Exception> Function<Context,Result<U>>
        _buildStep(@Nonnull Throwing.Function<? super T, ? extends U, E> f,
                   @Nonnull Function<Context,Result<T>> priorStep)
    {
        return context ->
        {
            Result<T> prior = priorStep.apply(context);
            if (prior.error != null) {
                return new Result(null, prior.error);
            }
            try {
                return new Result(f.apply(prior.value), null);
            }
            catch (Exception e) {
                if (context.logger != null) {
                    context.logger.accept(e);
                }
                return new Result(null, e);
            }
        };
    }

    /**
     * Class to carry run context recursively to each nested step.
     */

    private static class Context
    {
        final Consumer<Throwable> logger;

        Context(Consumer<Throwable> logger) {
            this.logger = logger;
        }
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