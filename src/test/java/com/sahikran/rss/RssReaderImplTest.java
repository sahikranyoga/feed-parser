package com.sahikran.rss;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sahikran.model.RSSItem;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RssReaderImplTest {

    private RSSItem rssItem;
    
    @Test
    @DisplayName("Test the date conversion reading RSS feed pubDate field")
    public void whenPubDateIsGiven_convertToInstantType(){
        String inputDate = "Mon,10 May 2021"; 
        rssItem = new RSSItem.Builder().setTitle("Yoga research").addPublishedDate(inputDate).build();
        assertEquals("2021-05-10", rssItem.getPublishedDate().toString());
        inputDate = "Fri, 01 Oct 2021 06:00:00 -0400";// this last -0400 is time zone, time offset
        rssItem = new RSSItem.Builder().setTitle("Yoga research").addPublishedDate(inputDate).build();
        assertEquals("2021-10-01", rssItem.getPublishedDate().toString());
    }
}
