
Stuff4J is whatever code I currently find most useful for building Java apps, that is not already
available without fuss in Commons Lang, Guava, JOOL, et cetera.

Most of it is geared toward reducing the awful boilerplate that arises from checked exceptions.  The
latter are now more or less regarded as a failure.  With proper use of generics and lambdas we can
eliminate most of them from an application, without providing an extensive library to mirror every
method that throws.  Examples:

Merge lines from multiple files:

    paths.stream()
            .flatMap(path -> Unchecked.apply(Files::readAllLines, path).stream())
            .collect(toList());

Adapt a ResultSet to consumers that don't handle SQLException:

    Function<String,Object> columnGetter = Unchecked.function(col -> resultSet.getObject(col));

Stuff4J also has some helpful utility types like tuples.  Specify multi-valued function outputs
without custom classes:

    return Tuple2.of("foo", 2);

Feel free to use.  MIT-licensed, no runtime dependencies apart from Java 9.
