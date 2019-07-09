package com.github.jonross.stuff4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.github.jonross.stuff4j.function.Unchecked;
import com.github.jonross.stuff4j.tbd.Nothing;
import com.github.jonross.stuff4j.tbd.Time;

import static java.util.stream.Collectors.toList;
import static com.github.jonross.stuff4j.Stuff4J.$;
import static org.testng.Assert.assertTrue;

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

    public void rs() {
        ResultSet resultSet = null;
        Function<String,Object> columnGetter = Unchecked.function(col -> resultSet.getObject(col));
    }
}
