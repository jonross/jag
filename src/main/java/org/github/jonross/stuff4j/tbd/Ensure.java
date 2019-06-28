package org.github.jonross.stuff4j.tbd;

/**
 * Prototype.
 * Subject to change.
 */

public class Ensure {

    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new NullPointerException(message);
        }
    }
}
