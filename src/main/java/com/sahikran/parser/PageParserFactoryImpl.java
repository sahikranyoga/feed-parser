package com.sahikran.parser;

import java.time.Duration;

public final class PageParserFactoryImpl implements PageParserFactory {

    private final String searchString;
    private final Duration timeout;

    public enum ParserType {
        RSS,
        IDENTIFIED,
        GENERAL
    }

    public PageParserFactoryImpl(String searchString, Duration timeout){
        this.searchString = searchString;
        this.timeout = timeout;
    }

    @Override
    public PageParser get(String url, ParserType parserType) {
        PageParser pageParser = null;
        switch(parserType){
            case IDENTIFIED:
                pageParser = new IdentifiedPageParser(url, searchString, timeout);
                break;
            case GENERAL:
                pageParser = new GeneralPageParser();
                break;
            case RSS:
                pageParser = new RSSPageParser(url, timeout);
                break;
            default:
                break;
        }
        return pageParser;
    }

}
