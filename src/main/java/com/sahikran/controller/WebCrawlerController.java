package com.sahikran.controller;

import java.util.List;

import com.sahikran.model.CrawlerResult;
import com.sahikran.model.PageMessage;
import com.sahikran.service.WebCrawlerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/crawl")
public class WebCrawlerController {
    
    @Autowired
    private WebCrawlerService webCrawlerService;

    @PostMapping
    public ResponseEntity<CrawlerResult> crawl(@RequestBody List<PageMessage> pageMessages){
        return new ResponseEntity<>(webCrawlerService.crawl(pageMessages), HttpStatus.CREATED); 
    }

}
