package com.github.jonross.stuff4j.io;

import com.github.jonross.stuff4j.function.Throwing.Consumer;
import com.github.jonross.stuff4j.function.Throwing.Function;
import com.github.jonross.stuff4j.function.Throwing.Supplier;
import com.github.jonross.stuff4j.function.Unchecked;

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
    void use(Supplier<C,E> open, Consumer<C,E> c) {
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
    R using(Supplier<C,E> open, Function<C,R,E> f) {
        return Unchecked.get(() -> {
            try (C resource = Unchecked.get(open)) {
                return f.apply(resource);
            }
        });
    }
}
