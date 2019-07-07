package org.github.jonross.stuff4j.lang;

import java.util.Arrays;
import java.util.List;

/**
 * An ad-hoc typed collection a few objects at a time.  Inspired by tuples in other libraries, this is a more
 * modest offering.  JOOL, for example, went so overboard use tuple support you can't read its Javadoc.
 * <p>
 * This is not meant to replace small value classes.  The intended use case is a combination of a few values use
 * a very limited lifetime, for example, a multi-valued function return or {@link java.util.stream.Stream#map} output.
 * <p>
 * Someday Java will have data classes :/
 */

public interface Tuple
{
    /**
     * Return the number of objects in this tuple.
     */

    int arity();
    
    /**
     * Return the objects in this tuple as a {@link List}.
     */

    List<Object> toList();
    
    /**
     * Implement {@link Object#hashCode()} by combining hash values of all the objects
     * in the tuple, using {@link Arrays#hashCode(Object[])}.
     */
    
    @Override
    public int hashCode();

    /**
     * Implement {@link Object#equals} by calling <code>equals</code> on each field in the tuple.
     * Two null fields will compare equal.
     */
    
    @Override
    public boolean equals(Object o);
}
