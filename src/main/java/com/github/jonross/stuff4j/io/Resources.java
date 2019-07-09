package com.github.jonross.stuff4j.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Optional;

import com.github.jonross.stuff4j.function.Unchecked;

/**
 * An abstraction on classpath resources (and maybe the filesystem, later on.)  Similar to Spring's Resource
 * interface but with less abstraction and more convenience.
 */

public abstract class Resources {

    /**
     * Return a {@link Resources} instance that evaluates paths relative to a class.
     * @see {@link ClasspathResources}.
     */

    public static ClasspathResources relativeTo(Class<?> klass) {
        return new ClasspathResources(klass);
    }

    /**
     * Find a resource.  See the implementation class for details.
     * @return
     */

    public abstract Optional<URL> find(String name);

    /**
     * Calls {@link #find} then attempts to open what was found (or not.)
     * @throws UncheckedIOException If {@link #find} did not return a resource, or if the open failed.
     */

    public abstract InputStream open(String name) throws UncheckedIOException;

    /**
     * Return resources from the classpath.
     */

    public static class ClasspathResources extends Resources {

        private final Class<?> klass;

        private ClasspathResources(Class<?> klass) {
            this.klass = klass;
        }

        /**
         * @param name If this starts with "/", search relative to the classloader of the class specified;
         *             if not, search relative to the class specified.
         */

        public Optional<URL> find(String name) {
            return name.startsWith("/")
                    ? Optional.ofNullable(klass.getClassLoader().getResource(name.substring(1)))
                    : Optional.ofNullable(klass.getResource(name));
        }

        /** {@inheritDoc} */
        @Override
        public InputStream open(String name) {
            var url = find(name).orElseThrow(() -> {
                var message = String.format("Resource %s not found relative to %s", name, klass.getName());
                throw Unchecked.wrap(new IOException(message));
            });
            return Unchecked.apply(URL::openStream, url);
        }
    }
}
