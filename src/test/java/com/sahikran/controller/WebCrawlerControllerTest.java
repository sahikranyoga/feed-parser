package com.sahikran.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahikran.model.CrawlerResult;
import com.sahikran.model.PageMessage;
import com.sahikran.parser.PageParserFactoryImpl.ParserType;
import com.sahikran.service.WebCrawlerService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.HashMap;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WebCrawlerController.class)
public class WebCrawlerControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WebCrawlerService webCrawlerService;

    @Test
    @DisplayName("pass multiple messages to fetch CrawlerResult output from the service implementation. " +
    "The crawler service is expected to parse messages for url type " + 
    "and pass it to the respective parser to fetch the results and save into database")
    public void whenASingleRssPageMessagePassed_returnsAValidResultObject() 
        throws Exception{
        
        ObjectMapper objectMapper = new ObjectMapper();
        String inputJson = objectMapper.writeValueAsString(getSingleMockMessage());

        when(webCrawlerService.crawl(any())).thenReturn(new CrawlerResult.Builder()
                                                        .setFeedItemsCount(new HashMap<>())
                                                        .setUrlsVisitedCount(100)
                                                        .build());

        mockMvc.perform(post("/crawl")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{}"));

        verify(webCrawlerService, times(1)).crawl(any());
        // verify the method call to saveIntoDatabase for single time
        
        // verify if the result has the expected count of feed items

    }

    public static List<PageMessage> getSingleMockMessage(){
        PageMessage pageMessage = new PageMessage.Builder()
                    .setPageMessageId(1000l)
                    .setPageUrl("https://www.ijoy.org.in/rss.asp?issn=0973-6131;year=2021;volume=14;issue=2;month=May-August")
                    .setParsertype(ParserType.RSS)
                    .build();
        return List.of(pageMessage);
    }
}
