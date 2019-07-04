package org.github.jonross.stuff4j.tbd;

/**
 * Prototype.
 * Subject to change.
 */

public class Ensure {

    public static <T> T notNull(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    public static <T> T notNull(T obj, String message) {
        if (obj == null) {
            throw new NullPointerException(message);
        }
        return obj;
    }
}
