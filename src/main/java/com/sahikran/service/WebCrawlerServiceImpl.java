package com.sahikran.service;

import java.util.List;

import com.sahikran.model.CrawlerResult;
import com.sahikran.model.PageMessage;

import org.springframework.stereotype.Service;

@Service
public class WebCrawlerServiceImpl implements WebCrawlerService {

    /**
     * Crawls each web page url in each message as per parser type and its configuration.
     * The obtained results from each url is stored in database for further analysis
     */
    @Override
    public CrawlerResult crawl(List<PageMessage> pageMessages) {
        return null;
        // TODO Auto-generated method stub
        
    }
    
}
