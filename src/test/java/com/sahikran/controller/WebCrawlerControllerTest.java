package com.sahikran.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sahikran.model.CrawlerResult;
import com.sahikran.model.PageMessage;
import com.sahikran.parser.PageParserFactoryImpl.ParserType;
import com.sahikran.service.WebCrawlerService;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;

import javax.print.attribute.standard.Media;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WebCrawlerController.class)
public class WebCrawlerControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WebCrawlerService webCrawlerService;

    @Test
    public void whenASingleRssPageMessagePassed_returnsAValidResultObject() 
        throws Exception{
        PageMessage pageMessage = new PageMessage.Builder()
                                    .setPageMessageId(1000l)
                                    .setPageUrl("https://www.ijoy.org.in/rss.asp?issn=0973-6131;year=2021;volume=14;issue=2;month=May-August")
                                    .setParsertype(ParserType.RSS)
                                    .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String inputJson = objectMapper.writeValueAsString(List.of(pageMessage));
        mockMvc.perform(post("/crawl")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson)
                .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(""));

        verify(webCrawlerService, times(1)).crawl(List.of(pageMessage));

    }
}
