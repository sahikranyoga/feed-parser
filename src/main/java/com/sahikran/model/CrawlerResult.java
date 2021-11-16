package com.sahikran.model;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class CrawlerResult {
    
    private final Map<String, Integer> feedItemsCount;
    private final int urlsVisitedCount;

    public CrawlerResult(Map<String, Integer> feedItemsCount, int urlsVisitedCount){
        this.feedItemsCount = feedItemsCount;
        this.urlsVisitedCount = urlsVisitedCount;
    }

    public Map<String, Integer> getFeedItemsCount() {
        return feedItemsCount;
    }

    public int getUrlsVisitedCount() {
        return urlsVisitedCount;
    }

    public static final class Builder{

        private Map<String, Integer> feedItemsCount;
        private int urlsVisitedCount;

        public Builder setFeedItemsCount(Map<String, Integer> feedItemsCount){
            this.feedItemsCount = Objects.requireNonNull(feedItemsCount);
            return this;
        }

        public Builder setUrlsVisitedCount(int urlsVisitedCount){
            this.urlsVisitedCount = urlsVisitedCount;
            return this;
        }

        public Builder(){
            // no implementation
        }

        public CrawlerResult build(){
            return new CrawlerResult(Collections.unmodifiableMap(feedItemsCount), urlsVisitedCount);
        }

    }

}
