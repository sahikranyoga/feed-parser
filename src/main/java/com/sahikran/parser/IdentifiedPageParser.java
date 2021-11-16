package com.sahikran.parser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;
import java.util.Queue;

import com.sahikran.exception.PageParserException;
import com.sahikran.httputil.HtmlReader;
import com.sahikran.httputil.HtmlReaderImpl;
import com.sahikran.validation.DateValidator;
import com.sahikran.validation.TextValidator;
import com.sahikran.validation.Validator;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class IdentifiedPageParser implements PageParser {

    private static final Logger log = LoggerFactory.getLogger(IdentifiedPageParser.class);

    private static final DateTimeFormatter optionaDateTimeFormatter = DateTimeFormatter.ofPattern("[dd-MMM-yyyy]");

    private static final DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                                                            .parseCaseInsensitive()
                                                            .appendOptional(optionaDateTimeFormatter)
                                                            .toFormatter(Locale.getDefault());

    private Validator<String> pageElementValidator;

    private final String pageUrl;

    private final String searchString;

    private final HtmlReader<Element> htmlReader;

    public IdentifiedPageParser(String pageUrl, String searchString, Duration timeout){
        this.pageUrl = Objects.requireNonNull(pageUrl, "input html file URI path is required");
        this.searchString = searchString;
        this.htmlReader = new HtmlReaderImpl(pageUrl, timeout);
    }

    /**
     * reads a remote or local file that has html content, traverses by level order traversal using BFS
     * and finds the item node that has related text for the provided field strings
     * @return Result object that has feedItems, links for this page
     * @throws PageParserException
     */
    public Result parse() 
            throws PageParserException {
        // create a result object at a url page level
        Result.Builder resultBuilder = new Result.Builder()
                        .addPageUrl(pageUrl);

        Element body = null;
        try {
            body = htmlReader.readHtmlbody();
        } catch (URISyntaxException | IOException e1) {
            throw new PageParserException("exception extracting the html body ", e1);
        }
        Objects.requireNonNull(body, "Body element is not found in the html page " + pageUrl);
        // using BFS style to traverse using queues
        Queue<Element> queue = new LinkedList<>();
        queue.add(body);
        // loop till queue is empty
        while(!queue.isEmpty()){
            int levelSize = queue.size();

            for(int i = 0 ; i < levelSize; i++){
                Element element = queue.poll();
                Element nextSiblingElement = element.nextElementSibling();
                if(nextSiblingElement != null){
                    pageElementValidator = new DateValidator(element.ownText(), 
                                            new TextValidator(nextSiblingElement.wholeText(), searchString, null));
                    if(pageElementValidator.validate()){
                        log.debug(element.ownText());
                        log.debug("text value found" + nextSiblingElement.wholeText());
                        log.debug("a tag value: " + nextSiblingElement.getElementsByTag("a").attr("href"));
                        resultBuilder.addItem(LocalDate.parse(element.ownText(), dateTimeFormatter), nextSiblingElement.wholeText(), nextSiblingElement.getElementsByTag("a").attr("href"));
                    }
                }
                // add the element's children to queue
                queue.addAll(element.children());
            }
        }
        return resultBuilder.build();
    }
}
