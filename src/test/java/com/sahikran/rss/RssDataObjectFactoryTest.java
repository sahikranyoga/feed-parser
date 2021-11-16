package com.sahikran.rss;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import com.sahikran.model.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RssDataObjectFactoryTest {
    
    @Test
    @DisplayName("To test creation of RSSItem object based on input data map")
    public void whenRssDataMapIsgiven_returnRssItemObject(){
        Map<String, Object> expectedDataMap = getRssDataMap();
        Item item = (Item)RssDataObjectFactory.fromPropertyMap(Item.class, expectedDataMap);
        assertEquals(expectedDataMap.get("title"), item.getTitle());
        assertEquals(expectedDataMap.get("description"), item.getDescription());
        assertEquals(expectedDataMap.get("link"), item.getLink());
        assertEquals(expectedDataMap.get("pubDate"), item.getPubDate());
        assertEquals(expectedDataMap.get("guid"), item.getGuid());
    }

    private Map<String, Object> getRssDataMap(){
        Map<String, Object> rssDataMap = new HashMap<>();
        rssDataMap.put("title", "Sleep, cognition, and yoga");
        rssDataMap.put("creator", "Usha Panjwani");
        rssDataMap.put("description", "International Journal of Yoga 2021 14(2):100-108<br><br> Stress is one of the major problems globally, associated with poor sleep quality and cognitive dysfunction");
        rssDataMap.put("link", "https://www.ijoy.org.in/text.asp?2021/14/2/100/315753");
        rssDataMap.put("pubDate", "Mon,10 May 2021");
        rssDataMap.put("guid", "https://www.ijoy.org.in/text.asp?2021/14/2/100/315753");
        return rssDataMap;
    }
}
