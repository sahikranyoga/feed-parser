package com.sahikran.rss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import com.sahikran.exception.RssReaderException;
import com.sahikran.model.RSSItem;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.reflect.Whitebox;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RssItemIteratorTest {

    @Mock
    private RssItemIterator.Builder rBuilder;

    @Mock
    private InputStream inputStream;

    @Mock
    private RssItemIterator rssItemIterator;

    @Test
    @DisplayName("Test the stripping of invalid prefixes on element names from xmlStreamReader")
    public void whenGivenElementNameContains_invalidPrefixes_returnValidElementName() 
    throws Exception{
        assertNotNull(rBuilder);
        String result = Whitebox.invokeMethod(rssItemIterator, "stripInvalidPrefixes", "dc:title");
        assertEquals("title", result);
    }

    private InputStream getRssFileInputStream(String rssFilePath){
        InputStream inputStream = null;
        try {
            inputStream = Files.newInputStream(Path.of(rssFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    @Test
    @DisplayName("when single item rss feed is passed, system should return single RssItem object")
    public void whenAValidSingleRssFeedXmlIsPassed_returnSingleItem() 
        throws RssReaderException, IOException
        {
        String rssFilePath = "/Users/kirankranthi/Documents/yoga-news-feed/crawler-parser/src/main/resources/single-rss-item.xml";
        
        try(RssItemIterator rssItemIterator = new RssItemIterator.Builder().build(getRssFileInputStream(rssFilePath))){
            RSSItem rssItem = rssItemIterator.next();
            assertEquals("Ethics in Yoga", rssItem.getTitle());
            assertEquals("<b>TM Srinivasan</b><br><br>International Journal of Yoga 2021 14(2):87-88<br><br>", rssItem.getDescription());
            assertEquals("https://www.ijoy.org.in/text.asp?2021/14/2/87/315760", rssItem.getGuid());
            assertEquals("https://www.ijoy.org.in/text.asp?2021/14/2/87/315760", rssItem.getLink());
            assertEquals(18757l, rssItem.getPublishedDate().toEpochDay());
            assertEquals(1, rssItem.getCreators().size());
        }
    }

    @ParameterizedTest
    @DisplayName("when rss feed item xmls are passed, should parse and return expected number of RssItems and their values should match")
    @CsvSource({
        "/Users/kirankranthi/Documents/yoga-news-feed/crawler-parser/src/main/resources/double-rss-item.xml, 3",
        "/Users/kirankranthi/Documents/yoga-news-feed/crawler-parser/src/main/resources/rss-complex.xml, 13"
    })
    public void whenComplexRssFeedXmlsArePassed_readExpectedSizeOfItems(String rssFilePath, int expected) 
        throws IOException, RssReaderException
        {
        try(RssItemIterator rssItemIterator = new RssItemIterator.Builder().build(getRssFileInputStream(rssFilePath))){
            int count = 0;
            while(rssItemIterator.hasNext()){
                String title = rssItemIterator.next().getTitle();
                assertNotNull(title);
                count++;
            }
            assertEquals(expected, count);
        } 
    }

}
