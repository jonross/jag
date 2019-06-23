package org.github.jonross.stuff4j.lang;

import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Objects
{
    /**
     * @return True if <code>a</code> and <code>b</code> are both <code>null</code>, or are both non-null
     * and <code>a.equals(b)</code>.  Same as Guava <code>Objects.equal</code>.
     */

    public static boolean equal(@Nullable Object a, @Nullable Object b) {
        return a == b || (a != null && a.equals(b));
    }

    /**
     * Same as {@link Arrays#hashCode(Object[])} but supports varargs.
     */

    public static int hashCode(@Nonnull Object... items) {
        return Arrays.hashCode(items);
    }
}
