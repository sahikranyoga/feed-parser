package com.sahikran.controller;

import java.util.List;

import com.sahikran.model.CrawlerResult;
import com.sahikran.model.PageMessage;
import com.sahikran.service.WebCrawlerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crawl")
/**
 * This class provides APIs to crawl based on messages received from the queue.
 */
public class WebCrawlerController {
    
    private WebCrawlerService webCrawlerService;

    @Autowired
    public void setWebCrawlerService(WebCrawlerService webCrawlerService){
        this.webCrawlerService = webCrawlerService;
    }
    
    /**
     * Reads web page messages and crawls to find out feed items and child URLs to be further crawled
     * The result contains on the count of Feed items found after parsing and child URLs found if any
     * @param pageMessages
     * @return
     */
    @PostMapping
    public ResponseEntity<CrawlerResult> crawl(@RequestBody List<PageMessage> pageMessages){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        return new ResponseEntity<>(webCrawlerService.crawl(pageMessages), httpHeaders, HttpStatus.OK); 
    }

}
