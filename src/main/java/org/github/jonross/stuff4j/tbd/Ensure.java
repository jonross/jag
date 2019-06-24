package org.github.jonross.stuff4j.tbd;

public class Ensure {

    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new NullPointerException(message);
        }
    }
}
