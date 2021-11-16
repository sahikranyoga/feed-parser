package com.sahikran.rss;

import java.io.InputStream;
import java.util.Iterator;
import java.util.stream.Stream;

@FunctionalInterface
public interface RssReader<T> {
    
    Stream<T> read(InputStream inputStream, Iterator<T> iterator);
}
