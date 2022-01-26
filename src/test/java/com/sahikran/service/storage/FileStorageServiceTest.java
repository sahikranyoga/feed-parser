package com.sahikran.service.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahikran.model.FeedItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class FileStorageServiceTest {

    private static final Logger log = LoggerFactory.getLogger(FileStorageServiceTest.class);
    
    @Autowired
    private FileStorageService objectStorageService;

    @Autowired
    private ObjectMapper objectMapper;

    private FeedItem feedItem;

    @BeforeEach
    void init(){
        feedItem = new FeedItem.Builder()
        .addItemDate(LocalDate.now())
        .addItemText("Bibliometric profile and density visualizing analysis of yoga intervention in type 2 diabetes: A 44 - year study on global scientific research output from 1975 to 2019")
        .addItemUrl("https://www.ijoy.org.in/text.asp?2021/14/2/89/315759")
        .build();
    }

    @Test
    public void whenAFeedItemIsPassed_returnSuccessMessageOnSavedFile(){
        // save a dummy feed item into database
        String fileName = objectStorageService.save(feedItem);
        String outputFilePath = "/Users/kirankranthi/Documents/yoga-news-feed/feed-parser/feed-files/" + fileName;
        log.info("the output file name is : " + fileName);
        assertTrue(Files.exists(Path.of(outputFilePath)));
        FeedItem actualFeedItem = null;
        try {
            actualFeedItem = objectMapper.readValue(Files.newBufferedReader(Path.of(outputFilePath)), FeedItem.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(actualFeedItem);
        assertEquals(actualFeedItem, feedItem);
    }

    @Test
    public void whenMultipleFeedItemsPassed_returnFileNamesSaved(){
        Set<String> files = objectStorageService.save(getMultipleFeeds());
        assertNotNull(files);
        assertEquals(2, files.size());
    }

    private List<FeedItem> getMultipleFeeds(){
        FeedItem feedItem1 = new FeedItem.Builder()
        .addItemDate(LocalDate.now())
        .addItemText("Bibliometric profile and density visualizing analysis of yoga intervention in type 2 diabetes: A 44 - year study on global scientific research output from 1975 to 2019")
        .addItemUrl("https://www.ijoy.org.in/text.asp?2021/14/2/89/315759")
        .build();

        FeedItem feedItem2 = new FeedItem.Builder()
        .addItemDate(LocalDate.now())
        .addItemText("Sleep, cognition, and yoga")
        .addItemUrl("https://www.ijoy.org.in/text.asp?2021/14/2/100/315753")
        .build();

        return new ArrayList<>(){{ add(feedItem1); add(feedItem2); }};
    }
}
