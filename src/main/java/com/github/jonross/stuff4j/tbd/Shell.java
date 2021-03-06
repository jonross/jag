package com.github.jonross.stuff4j.tbd;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.Future;

import com.github.jonross.stuff4j.function.Unchecked;
import com.github.jonross.stuff4j.io.UncheckedIO;
import com.github.jonross.stuff4j.lang.Tuple2;

import static java.lang.ProcessBuilder.Redirect.INHERIT;
import static java.lang.ProcessBuilder.Redirect.PIPE;
import static java.util.stream.Collectors.joining;

/**
 * Prototype.
 * Subject to change.
 */

public final class Shell {

    private boolean mustSucceed = false;
    private boolean mergeStderr = false;
    private String[] command;

    public Shell(String[] command) {
        if (command.length == 1 && command[0].contains(" ")) {
            command = new String[]{"bash", "-c", command[0]};
        }
        this.command = command;
    }

    public Shell orDie() {
        mustSucceed = true;
        return this;
    }

    public Shell mergeStderr() {
        mergeStderr = true;
        return this;
    }

    public int status() { return _execute(false)._1; }
    public String output() { return _execute(true)._2; }
    public Tuple2<Integer,String> result() { return _execute(true); }

    private Tuple2<Integer,String> _execute(boolean wantOutput) {
        return Unchecked.get(() -> {
            Process p = new ProcessBuilder(command)
                    .redirectOutput(wantOutput ? PIPE : INHERIT)
                    .redirectError(wantOutput & mergeStderr ? PIPE : INHERIT)
                    .start();
            Future<String> stdoutReader =
                    Threads.DEFAULT_EXECUTOR_SERVICE.submit(() -> new String(UncheckedIO.drain(p.getInputStream())));
            Future<String> stderrReader = ! mergeStderr ? null :
                    Threads.DEFAULT_EXECUTOR_SERVICE.submit(() -> new String(UncheckedIO.drain(p.getErrorStream())));
            int exitValue = Time.nointr(Duration.ofDays(365), (t, u) -> p.waitFor()).get();
            if (p.exitValue() != 0 && mustSucceed) {
                System.err.println("Command failed: " + Arrays.stream(command).collect(joining(" ")));
                System.exit(1);
            }
            return Tuple2.of(p.exitValue(), stdoutReader.get() + (mergeStderr ? stderrReader.get() : ""));
        });
    }
}
