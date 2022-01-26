package com.sahikran.service;

import java.util.List;
import java.util.Optional;

import com.sahikran.model.FeedItem;

public interface FeedService {
    List<FeedItem> searchForFeedsByDates(Optional<String> publishedStarteDate, Optional<String> publishedEndDate);
}
