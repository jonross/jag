package org.github.jonross.stuff4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.github.jonross.stuff4j.function.Null;
import org.github.jonross.stuff4j.function.Unchecked;
import org.github.jonross.stuff4j.tbd.Time;

import static java.util.stream.Collectors.toList;
import static org.github.jonross.stuff4j.Stuff4J.$;

public class Samples {

    public void paths() {
        List<Path> paths = new ArrayList<>();
        paths.stream()
                .flatMap(path -> $.apply(Files::readAllLines, path).stream())
                .collect(toList());

    }

    public void sleep() {
        // Time.nointr(Duration.ofSeconds(2), Null.unvoid(Thread::sleep));
    }
}
