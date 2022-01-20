package com.sahikran.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import com.sahikran.exception.CrawlServiceException;
import com.sahikran.model.CrawlerResult;
import com.sahikran.model.PageMessage;
import com.sahikran.model.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebCrawlerServiceImpl implements WebCrawlerService {

    private static final Logger log = LoggerFactory.getLogger(WebCrawlerServiceImpl.class);

    private ParseService<Result> parseService;

    private AsyncObjectStorageService asyncObjectStorageService;

    @Autowired
    public void setAsyncObjectStorageService(AsyncObjectStorageService asyncObjectStorageService){
        this.asyncObjectStorageService = asyncObjectStorageService;
    }

    @Autowired
    public void setParseService(ParseService<Result> parseService){
        this.parseService = parseService;
    }

    /**
     * Crawls each web page url in each message as per parser type and its configuration.
     * The obtained results from each url is stored in database for further analysis
     */
    @Override
    public CrawlerResult crawl(List<PageMessage> pageMessages) {
        Map<String, Integer> feedItemsCount = new ConcurrentHashMap<>();
        // for each message, read the url type and based on url type, parse the url content.
        log.info("crawling started for messages " + pageMessages.size());
        pageMessages.stream()
                    .parallel()
                    .forEach(
                        m -> {
                                log.info("crawling page message " + m.getPageUrl());
                                CompletableFuture<Result> resultFuture = parseService.parse(m);
                                resultFuture.thenComposeAsync(result -> asyncObjectStorageService.SaveAsync(result));
                                Result result;
                                try {
                                    result = resultFuture.get();
                                } catch (InterruptedException | ExecutionException e) {
                                    throw new CrawlServiceException("execption occurred when feteching Result object ", e);
                                }
                                feedItemsCount.compute(result.getPageUrl(), 
                                    (k, v) -> (v == null) ? result.getFeedItems().size() : v + result.getFeedItems().size());
                        }
                    );
        log.info("crawler completed for received page messages");
        return new CrawlerResult.Builder()
                .setUrlsVisitedCount(pageMessages.size())
                .setFeedItemsCount(feedItemsCount)
                .build();
    }

}
