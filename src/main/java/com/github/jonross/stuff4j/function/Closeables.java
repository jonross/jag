package com.github.jonross.stuff4j.function;

/**
 * Similar to {@link Unchecked}, this invokes common functional interfaces while also automatically closing
 * an {@link AutoCloseable} resource.
 */

public class Closeables
{
    /**
     * Run code in the context of an open resource, which automatically closes when the work finishes.
     * To return a value, use {@link #using} instead.
     */

    public static <C extends AutoCloseable,E extends Exception>
    void use(Throwing.Supplier<C,E> open, Throwing.Consumer<C,E> c) {
        Unchecked.run(() -> {
            try (C resource = Unchecked.get(open)) {
                c.accept(resource);
            }
        });
    }

    /**
     * Run code in the context of an open resource, which automatically closes when the work finishes.
     * To not return a value, use {@link #use} instead.
     */

    public static <C extends AutoCloseable,R,E extends Exception>
    R using(Throwing.Supplier<C,E> open, Throwing.Function<C,R,E> f) {
        return Unchecked.get(() -> {
            try (C resource = Unchecked.get(open)) {
                return f.apply(resource);
            }
        });
    }
}
