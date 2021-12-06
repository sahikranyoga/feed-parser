package com.sahikran.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private ParseService parseService;

    @Autowired
    public void setParseService(ParseService parseService){
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
        pageMessages.stream()
                    .parallel()
                    .forEach(
                        m -> {
                            try {
                                log.info("crawling page message " + m.getPageUrl());
                                // TODO instead of .get() which blocks, chain with any other method that saves into database
                                Result result = parseService.parse(m).get();
                                feedItemsCount.compute(result.getPageUrl(), 
                                    (k, v) -> (v == null) ? result.getFeedItems().size() : v + result.getFeedItems().size());
                            } catch (InterruptedException | ExecutionException e) {
                                throw new CrawlServiceException("exception occurred in fetching result object ", e);
                            }
                        }
                    );
        log.info("crawler completed for received page messages");
        return new CrawlerResult.Builder()
                .setUrlsVisitedCount(pageMessages.size())
                .setFeedItemsCount(feedItemsCount)
                .build();
    }
    
}
