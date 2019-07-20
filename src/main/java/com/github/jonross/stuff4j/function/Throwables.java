package com.github.jonross.stuff4j.function;

import com.github.jonross.stuff4j.tbd.Nothing;

public class Throwables {

    public static <E extends Exception> Nothing raise(E e) throws E {
        throw e;
    }
}
