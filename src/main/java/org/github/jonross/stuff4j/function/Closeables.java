package org.github.jonross.stuff4j.function;

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

    public static <C extends AutoCloseable,E1 extends Exception,E2 extends Exception>
    void use(Throwing.Supplier<C,E1> open, Throwing.Consumer<C,E2> c) {
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

    public static <C extends AutoCloseable,R,E1 extends Exception,E2 extends Exception>
    R using(Throwing.Supplier<C,E1> open, Throwing.Function<C,R,E2> f) {
        return Unchecked.get(() -> {
            try (C resource = Unchecked.get(open)) {
                return f.apply(resource);
            }
        });
    }
}