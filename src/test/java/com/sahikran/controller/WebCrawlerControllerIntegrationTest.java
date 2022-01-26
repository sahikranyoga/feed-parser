package com.sahikran.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.sahikran.model.CrawlerResult;
import com.sahikran.model.PageMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class WebCrawlerControllerIntegrationTest {
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @ParameterizedTest
    @MethodSource("com.sahikran.controller.WebCrawlerControllerTest#provideMutlipleMessageInputs")
    public void whenCrawlApiIsCalled_returnOKStatusCode
        (List<PageMessage> pageMessage, int expectedFeedItemCount, int expectedUrlsVisitedCount){
        ResponseEntity<CrawlerResult> response = 
        this.restTemplate.postForEntity("http://localhost:" + port + "/crawl", 
            new HttpEntity<>(pageMessage), 
            CrawlerResult.class);
        
            assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
            assertEquals(expectedFeedItemCount, response.getBody().getFeedItemsCount().size());
            assertEquals(expectedUrlsVisitedCount, response.getBody().getUrlsVisitedCount());
    }
}
