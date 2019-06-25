package org.github.jonross.stuff4j.tbd;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.github.jonross.stuff4j.lang.Throwing;
import org.github.jonross.stuff4j.lang.Throwing.Function;

/**
 * WORK IN PROGRESS
 */

public class Try<T>
{
    /** Logic to run in this step of the chain */
    private final Supplier<Result<T>> step;

    /** Description of the logic */
    private final String description;

    /** What numbered step this is in the chain */
    private final int serial;

    /** Settings specified at the start of the chain */
    private final TrySetup setup;

    /** What the first step operates on, since there is no prior step */
    private final static Object NOTHING = new Object();

    private Try(String description, int serial, Supplier<Result<T>> step, TrySetup setup)
    {
        this.serial = serial;
        this.description = description != null ? description : "step " + serial;
        this.step = step;
        this.setup = setup;
    }

    /**
     * Specify a logger for errors on named steps, before the first invocation of {@link #to(String, Throwing.Supplier)}
     */

    public static TrySetup withErrorsTo(@Nonnull BiConsumer<String, ? extends Exception> logger)
    {
        return new TrySetup().withErrorsTo(logger);
    }

    /**
     * Indicate that <code>null</code> is a valid return from a step function, before the first invocation of
     * {@link #to(Throwing.Supplier)}.
     */

    public static TrySetup allowingNull()
    {
        return new TrySetup().allowingNull();
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

    public static <T,E extends Exception> Try<T> to(@Nonnull Throwing.Supplier<T,E> s)
    {
        return new TrySetup().to(s);
    }

    /**
     * Same as {@link #to(Throwing.Supplier)}, except that if the step throws an error, it will be logged via the
     * function passed to {@link #withErrorsTo}.  The message will be <code>"Failed to " + description</code>.
     */

    public static <T,E extends Exception> Try<T> to(@Nullable String description, @Nonnull Throwing.Supplier<T,E> s)
    {
        return new TrySetup().to(description, s);
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

    public <U,E extends Exception> Try<U> map(@Nonnull Throwing.Function<? super T, ? extends U,E> f)
    {
        return map(null, f);
    }

    /**
     * Same as {@link #map(Function)}, except that if the step throws an error, it will be logged via the
     * function passed to {@link #withErrorsTo}.  The message will be <code>"Failed to " + description</code>.
     */

    public <U,E extends Exception> Try<U> map(@Nullable String description, @Nonnull Throwing.Function<? super T, ? extends U,E> f)
    {
        // To run this step, fetch the result from prior step and apply the function
        return new Try(description, 1 + serial, () -> _buildStep(setup, f, step), setup);
    }

    /**
     * Get the result of the last computation, or throw the first exception thrown by a prior step, or throw if
     * any result was null and <code>allowingNull</code> was not called during setup.
     */

    public T get()
    {
        Result<T> result = step.get();
        if (result.error != null)
        {
            if (result.error instanceof RuntimeException)
            {
                throw ((RuntimeException) result.error);
            }
            throw new RuntimeException(result.error);
        }
        return result.value;
    }

    /**
     * This builder class is the actual implementation of the similarly named static setup methods that can
     * be called on {@link Try}.  This allows chaining of setup methods before we begin chaining computations,
     * e.g.
     * <pre>
     *     Try.allowingNull()
     *         .withErrorsTo(MYLOG::error)
     *         .to(...
     * </pre>
     */

    public final static class TrySetup
    {
        final @Nullable BiConsumer<String,? extends Exception> logger;
        final boolean allowNull;

        private TrySetup()
        {
            this(null, false);
        }

        private TrySetup(@Nullable BiConsumer<String,? extends Exception> logger, boolean allowNull)
        {
            this.logger = logger;
            this.allowNull = allowNull;
        }

        /**
         * @see Try#withErrorsTo(BiConsumer)
         */

        public TrySetup withErrorsTo(@Nonnull BiConsumer<String,? extends Exception> logger)
        {
            return new TrySetup(logger, allowNull);
        }

        /**
         * @see Try#allowingNull()
         */

        public TrySetup allowingNull()
        {
            return new TrySetup(logger, true);
        }

        /**
         * @see Try#to(Throwing.Supplier)
         */

        public <T,E extends Exception> Try<T> to(@Nonnull Throwing.Supplier<T,E> s)
        {
            return to(null, s);
        }

        /**
         * @see Try#to(String, Throwing.Supplier)
         */

        public <T,E extends Exception> Try<T> to(@Nullable String description, @Nonnull Throwing.Supplier<T,E> s)
        {
            // To start the chain, behave like map() but supply a dummy result for the prior step
            return new Try(null, 1, () -> _buildStep(this, nil -> s.get(),
                    () -> new Result(description, NOTHING, null)), this);
        }
    }

    /**
     * Everything else in this class is boilerplate, this is the brains of the operation.  Build the Supplier for
     * a given step's result that transforms the result of the prior step.
     */

    private static <T,U,E extends Exception> Result<U>
    _buildStep(@Nonnull TrySetup setup, @Nonnull Throwing.Function<? super T, ? extends U, E> f,
               @Nonnull Supplier<Result<T>> priorStep)
    {
        Result<T> prior = priorStep.get();
        try
        {
            U newValue = f.apply(prior.value);
            if (newValue == null && ! setup.allowNull)
            {
                throw new NullPointerException("null result from")
            }
            return new Result(description, )
            return newValue != null || setup.allowNull
                    ? new Result(newValue, null)
                    : new Result(null, new NullPointerException("foo"));
        }
        catch (Exception e)
        {
            return new Result(null, e);
        }
    }

    private static class Result<T>
    {
        @Nonnull final String description;
        @Nullable final T value;
        @Nullable final Exception error;

        Result(@Nonnull String description, @Nullable T value, @Nullable Exception error)
        {
            this.description = description != null ? description : "step 1"; // special case for setup time
            this.value = value;
            this.error = error;
        }
    }
}
