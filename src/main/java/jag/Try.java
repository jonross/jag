package jag;

import java.util.function.Supplier;

import org.github.jonross.stuff4j.lang.Throwing;

/**
 * WORK IN PROGRESS
 */

public class Try<T>
{
    /** Logic to run in this step of the chain */
    private final Supplier<Result<T>> step;

    /** Settings specified at the start of the chain */
    private final TrySetup setup;

    /** What the first step operates on, since there is no prior step */
    private final static Object NOTHING = new Object();

    private Try(Supplier<Result<T>> step, TrySetup setup)
    {
        this.step = step;
        this.setup = setup;
    }

    public static <T,E extends Exception> Try<T> to(Throwing.Supplier<T,E> s)
    {
        return TrySetup.to(s);
    }

    public <U,E extends Exception> Try<U> map(Throwing.Function<? super T, ? extends U,E> f)
    {
        // To run this step, fetch the result from prior step and apply the function
        return new Try(() -> _resolve(f, step), setup);
    }

    private static <T,U,E extends Exception> Result<U>
        _resolve(Throwing.Function<? super T, ? extends U, E> f, Supplier<Result<T>> priorStep)
    {
        Result<T> prior = priorStep.get();
        try
        {
            return new Result(f.apply(prior.value), null);
        }
        catch (Exception e)
        {
            return new Result(null, e);
        }
    }

    private static class Result<T>
    {
        T value;
        Exception error;

        Result(T value, Exception error)
        {
            this.value = value;
            this.error = error;
        }
    }

    public final static class TrySetup
    {
        private final static int log;

        private TrySetup()
        {

        }
        public static <T,E extends Exception> Try<T> to(Throwing.Supplier<T,E> s)
        {
            // To start the chain, behave like map() but supply a dummy result for the prior step
            return new Try(() -> _resolve(nil -> s.get(), () -> new Result(NOTHING, null)), new TrySetup());
        }

    }
}
