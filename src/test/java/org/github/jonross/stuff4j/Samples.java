package org.github.jonross.stuff4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.github.jonross.stuff4j.tbd.Nothing;
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
        // Nope, no, no.  Thread.sleep might be just too fucking broken.
        Time.nointr(Duration.ofMillis(1), (t, u) -> Nothing.unvoid(() -> Thread.sleep(t)));
    }
}
