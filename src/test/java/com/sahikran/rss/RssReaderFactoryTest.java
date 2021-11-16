package com.sahikran.rss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sahikran.exception.RssReaderException;
import com.sahikran.model.RSSItem;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RssReaderFactoryTest {
    
    @Test
    public void whenInputRssXmlFileIsPassed_toRssReader_returnStream() 
        throws RssReaderException{
        Stream<RSSItem> rStream = RssReaderFactory.readRssStream(getRssFileInputStream());
        assertNotNull(rStream);
    }

    private InputStream getRssFileInputStream(){
        String rssFilePath = "/Users/kirankranthi/Documents/yoga-news-feed/crawler-parser/src/main/resources/rss-complex.xml";
        InputStream inputStream = null;
        try {
            inputStream = Files.newInputStream(Path.of(rssFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    @Test
    @DisplayName("when a valid Rss xml file is passed to RssReader, verify if return stream can be converted into an ArrayList")
    public void whenInputRssXmlFileIsPassed_toRssReader_convertReturnedStreamIntoList() 
        throws RssReaderException {
        Stream<RSSItem> rStream = RssReaderFactory.readRssStream(getRssFileInputStream());
        List<RSSItem> lItems = new ArrayList<>(rStream.collect(Collectors.toList()));
        assertEquals(13, lItems.size());
    }

    @Test
    @DisplayName("when a valid Rss xml file is passed to RssReader, verify if return stream can be converted into an CopyOnWriteArrayList")
    public void whenInputRssXmlFileIsPassed_toRssReader_convertReturnedStreamIntoCopyOnWriteArrayList() 
        throws RssReaderException {
        Stream<RSSItem> rStream = RssReaderFactory.readRssStream(getRssFileInputStream());
        CopyOnWriteArrayList<RSSItem> copyOnWriteArrayList = new CopyOnWriteArrayList<>(rStream.collect(Collectors.toList()));
        assertEquals(13, copyOnWriteArrayList.size());
    }

}
