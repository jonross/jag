package com.github.jonross.stuff4j.tbd;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Prototype.
 * Subject to change.
 */

public class Threads
{
    private final static AtomicInteger threadSerial = new AtomicInteger();

    public final static ExecutorService DEFAULT_EXECUTOR_SERVICE = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.setName(String.format("jag-%d", threadSerial.incrementAndGet()));
        return t;
    });
}
