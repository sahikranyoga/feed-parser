package com.sahikran.service;

import java.util.List;
import java.util.Optional;

import com.sahikran.model.FeedItem;

import org.springframework.stereotype.Service;

@Service
public class FeedServiceImpl implements FeedService {

    @Override
    public List<FeedItem> searchForFeedsByDates(Optional<String> publishedStarteDate,
            Optional<String> publishedEndDate) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
