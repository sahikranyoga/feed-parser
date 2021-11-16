package com.sahikran.httputil;

import java.io.IOException;
import java.net.URISyntaxException;

public interface HtmlReader<T> {
    T readHtmlbody() throws URISyntaxException, IOException;
}
