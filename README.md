
Stuff4J is whatever code I currently find most useful for building Java apps, that is not already
available without fuss in Commons Lang, Guava, JOOL, et cetera.

Most of it is geared toward reducing the awful boilerplate that arises from checked exceptions.  The
latter are now more or less regarded as a failure.  With proper use of generics and lambdas we can
eliminate most of them from an application, without providing an extensive library to mirror every
method that throws.  Examples:

Merge lines from multiple files:

    paths.stream()
            .flatMap(path -> $.apply(Files::readAllLines, path).stream())
            .collect(toList());

Feel free to use.  MIT-licensed, no runtime dependencies apart from Java 9.
