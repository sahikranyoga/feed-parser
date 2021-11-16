package com.sahikran.parser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

import com.sahikran.exception.PageParserException;
import com.sahikran.exception.RestHandlerException;
import com.sahikran.exception.RssReaderException;
import com.sahikran.httputil.RestHandler;
import com.sahikran.model.FeedItem;
import com.sahikran.model.RSSItem;
import com.sahikran.rss.RssReaderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public final class RSSPageParser implements PageParser {

    private static final Logger log = LoggerFactory.getLogger(RSSPageParser.class);

    private RestHandler restHandler;

    private final String pageUrl;

    private final Duration timeout;

    @Autowired
    @Qualifier("httpsRestHandler")
    public void setRestHandler(RestHandler restHandler){
        this.restHandler = restHandler;
    }

    public RSSPageParser(String pageUrl, Duration timeout){
        this.pageUrl = pageUrl;
        this.timeout = timeout;
    }

    @Override
    public Result parse() throws PageParserException {
        log.info("Parsing RSS input");
        // creates an instance of RssReader using build method.
        // takes the url string as an input and fetches an inputStream from the file/url it reads.
        // passes this inputStream to RssReader instance to fetch List<RssItem>
        // iterates over this list and adds to Result as FeedItems
        // as an intermediate operation, we can save this List>RssItem> into database for future use
        Stream<RSSItem> rssItemsStream = null;
        try {
            // create async method to fetch the url content, post chain to process the response and calls the RssReader to return Stream<RssItem>
            rssItemsStream = restHandler.getHttpClient(true, timeout)
                        .thenComposeAsync(restHandler.invokeARestCall(pageUrl))
                        .thenComposeAsync(getRssFeedItems())
                        .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new PageParserException("exception when fetching RssItems", e);
        }
        log.info("Reading input RSS file and fetching Feed items completed, copying into Result object...");

        // create a result object at a url page level
        // Output:
        // the Result object should be newly created using the Builder. 
        // once the Result object is created, the underlying lists for FeedItem and links are not modifiable
        // if in case, we need to merge multiple Result Objects, then a separate merge algorithm to be used to create a new merge List
        return new Result.Builder()
                        .addPageUrl(pageUrl)
                        .addAllFeedItems(rssItemsStream.map(this::toFeedItem).collect(Collectors.toList()))
                        .build();
        // security related operations: 
        // removing unwanted characters, white spaces on inputStream
    }

    private FeedItem toFeedItem(RSSItem rssItem){
        return new FeedItem.Builder()
            .addItemDate(rssItem.getPublishedDate())
            .addItemText(rssItem.getTitle())
            .addItemUrl(rssItem.getLink())
            .build();
    }
    
    private Function<HttpResponse<InputStream>, CompletableFuture<Stream<RSSItem>>> getRssFeedItems(){
        return response -> {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    var inputStream = response.body();
                    // handle gZip input stream
                    if(response.headers().firstValue("Content-Encoding").equals(Optional.of("gzip"))){
                        inputStream = new GZIPInputStream(inputStream);
                    }
                    inputStream = new BufferedInputStream(inputStream);
                    return RssReaderFactory.readRssStream(inputStream);
                } catch (RssReaderException | IOException e) {
                    throw new RestHandlerException("Failed to fetch feed items ", e);
                }
            });
        };
    }
     
    
}
