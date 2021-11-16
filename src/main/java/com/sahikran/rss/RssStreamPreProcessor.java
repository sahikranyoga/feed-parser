package com.sahikran.rss;

import java.io.InputStream;

@FunctionalInterface
public interface RssStreamPreProcessor {
    InputStream process(InputStream inputStream);
}
