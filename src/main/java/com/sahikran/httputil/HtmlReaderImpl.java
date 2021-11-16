package com.sahikran.httputil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HtmlReaderImpl implements HtmlReader<Element> {

    private final String pageFilePath;
    private final Duration timeout;

    public HtmlReaderImpl(String pageFilePath, Duration timeout){
        this.pageFilePath = pageFilePath;
        this.timeout = timeout;
    }

    @Override
    public Element readHtmlbody() 
        throws URISyntaxException, IOException{
        
        Document document = parseDocument(pageFilePath);
        
        Element body = document.select("body").first();

        if(body.children().isEmpty()){
            throw new RuntimeException("no body content found in the html");
        }
        return body;
    }

    private Document parseDocument(String htmlFilePath) throws IOException, URISyntaxException{
        // if it is a local file, then re-instantiate the URI object by converting the file path to URI
        URI uri = new URI(Paths.get(htmlFilePath).toUri().toString());

        if(!isLocalFile(uri)){
            return Jsoup.parse(uri.toURL(), (int) timeout.toMillis());
        }

        // to read the local file
        try(InputStream inputStream = Files.newInputStream(Path.of(uri))){
            return Jsoup.parse(inputStream, StandardCharsets.UTF_8.name(), "");
        }
    }

    /**
    * Returns true if and only if the given {@link URI} represents a local file.
    */
    private static boolean isLocalFile(URI uri) {
        return uri.getScheme() != null && uri.getScheme().equals("file");
    }
}
