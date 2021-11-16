package com.sahikran.service;

import java.util.List;

import com.sahikran.model.CrawlerResult;
import com.sahikran.model.PageMessage;

import org.springframework.stereotype.Service;

@Service
public interface WebCrawlerService {
    CrawlerResult crawl(List<PageMessage> pageMessages);
}
