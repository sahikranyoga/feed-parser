package com.sahikran.parser;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.sahikran.model.FeedItem;

public class Result {
    /**
     * Stores News Feed item's attributes from each known page such as:
     * 1. Item Date
     * 2. Item text
     * 3. Page URL the item was found
     * and links that were found in each page
     */
    private final List<FeedItem> feedItems;

    private final String pageUrl;

    private final List<String> links;

    private Result(List<FeedItem> feedItems, String pageUrl, List<String> links){
        this.feedItems = feedItems;
        this.pageUrl = pageUrl;
        this.links = links;
    }

    public List<String> getLinks(){
        return links;
    }

    public List<FeedItem> getFeedItems(){
        return feedItems;
    }

    public String getPageUrl(){
        return pageUrl;
    }

    public static final class Builder {
        private final Set<String> links = new HashSet<>();
        private final Set<FeedItem> feedItemSet = new HashSet<>();
        private String pageUrl;
        
        /**
         * add itemDate, itemText and itemUrl to create an object of FeedItem
         * @param itemDate
         * @param itemText
         * @param itemUrl
         * @return {@link Result.Builder}
         */
        public Builder addItem(LocalDate itemDate, String itemText, String itemUrl){
            feedItemSet.add(new FeedItem.Builder()
            .addItemDate(itemDate)
            .addItemText(itemText)
            .addItemUrl(itemUrl)
            .build());
            return this;
        }

        /**
         * add feedItem object
         * @param feedItem
         * @return {@link Result.Builder}
         */
        public Builder addFeedItem(FeedItem feedItem){
            feedItemSet.add(feedItem);
            return this;
        }

        /**
         * Add all feeditems at once
         * @param feedItems
         * @return {@link Result.Builder}
         */
        public Builder addAllFeedItems(List<FeedItem> feedItems){
            feedItemSet.addAll(feedItems);
            return this;
        }

        /**
         * adds the page url on which the result has been found
         * @param pageUrl
         */
        public Builder addPageUrl(String pageUrl){
            Objects.requireNonNull(pageUrl, "Cant not be an empty page url");
            this.pageUrl = pageUrl;
            return this;
        }
        /**
         * Adds the given link, if it has not already been added.
         */
        public Builder addLink(String link) {
            Objects.requireNonNull(pageUrl, "Cant not be an empty link");
            links.add(Objects.requireNonNull(link));
            return this;
        }

        public Result build(){
            return new Result(
                feedItemSet.stream().collect(Collectors.toUnmodifiableList()),
                pageUrl,
                links.stream().collect(Collectors.toUnmodifiableList()));
        }
    }
    
}
