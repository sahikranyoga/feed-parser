package com.sahikran.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.sahikran.exception.PageParserException;
import com.sahikran.model.PageMessage;
import com.sahikran.model.Result;
import com.sahikran.parser.PageParserFactoryImpl.ParserType;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ParseServiceImplTest {

    private static final Logger log = LoggerFactory.getLogger(ParseServiceImplTest.class);

    @Autowired
    private ParseService<Result> parseService;

    @Test
    @RepeatedTest(5)
    public void whenAMessageIsPassed_returnFutureResult() throws PageParserException, InterruptedException, ExecutionException{
        PageMessage pageMessage = getSamplePageMessage();

        CompletableFuture<Result> cFuture = parseService.parse(pageMessage);
                    
        assertNotNull(cFuture);
        cFuture.thenRun(() -> {
            assertTrue(cFuture.isDone());
        });

        Result result = cFuture.get();
        assertNotNull(result);
    }

    @Test
    public void whenAMessageIsPassedAndInvokedAsync_returnResultObject() 
        throws InterruptedException, ExecutionException{
        PageMessage pageMessage = getSamplePageMessage();

        CompletableFuture<Result> cFuture = parseService.parse(pageMessage);

        cFuture.thenComposeAsync(result -> 
            CompletableFuture.runAsync(
                () -> log.info("result = " + result.getFeedItems().size())
            )
        );
        cFuture.get();
        log.info("tested executed");
        assertNotNull(cFuture);
    }

    private PageMessage getSamplePageMessage(){
        return new PageMessage.Builder()
        .setPageMessageId(1000l)
        .setPageUrl("https://www.ijoy.org.in/rss.asp?issn=0973-6131;year=2021;volume=14;issue=2;month=May-August")
        .setParserType(ParserType.RSS)
        .build();
    }

    @Test
    public void whenAnInvaludUrlIsPassed_retryTheTargetUrlTwoTimes() throws InterruptedException, ExecutionException{
        PageMessage pageMessage = new PageMessage.Builder()
                    .setPageMessageId(1000l)
                    .setPageUrl("https://www.invaliddomain.in/rss.asp?issn=0973-6131;year=2021;volume=14;issue=2;month=May-August")
                    .setParserType(ParserType.RSS)
                    .build();

        CompletableFuture<Result> cFuture = parseService.parse(pageMessage);
        assertNotNull(cFuture);
        cFuture.thenRun(() -> {
            assertTrue(cFuture.isDone());
        });

        assertThrows(ExecutionException.class, () -> {
            cFuture.get();
        });
    }
    
}
