package com.sahikran.service;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import com.sahikran.exception.PageParserException;
import com.sahikran.exception.ParseServiceException;
import com.sahikran.model.PageMessage;
import com.sahikran.model.Result;
import com.sahikran.parser.PageParser;
import com.sahikran.parser.PageParserFactory;
import com.sahikran.parser.PageParserFactoryImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ParseServiceImpl implements ParseService<Result> {

    private static final Logger log = LoggerFactory.getLogger(ParseServiceImpl.class);

    private final String searchString;

    private final Duration timeout;

    @Autowired
    public ParseServiceImpl(@Value("${parser.searchString}") String searchString, @Value("${parser.timeout}") long timeoutInSeconds){
        this.searchString = searchString;
        this.timeout = Duration.ofSeconds(timeoutInSeconds);
    }
    
    @Override
    @Async
    public CompletableFuture<Result> parse(PageMessage pageMessage) {
        log.info("initilizaing parser on thread: " + Thread.currentThread().getName());
        PageParserFactory pageParserFactory = new PageParserFactoryImpl(searchString, timeout);
        PageParser pageParser = pageParserFactory.get(pageMessage.getPageUrl(), pageMessage.getParserType());
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("calling page parser");
                return pageParser.parse();
            } catch (PageParserException e) {
                log.info("exception occurred " + e.getMessage());
                throw new ParseServiceException("exception occurred when parsing ", e);
            }
        });
    }

}
