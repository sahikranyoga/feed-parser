package com.sahikran.model;

import com.sahikran.parser.PageParserFactoryImpl.ParserType;

public class PageMessage {
    
    private final long pageMessageId;
    private final String pageUrl;
    private final ParserType parserType;

    private PageMessage(long pageMessageId, String pageUrl, ParserType parserType){
        this.pageMessageId = pageMessageId;
        this.pageUrl = pageUrl;
        this.parserType = parserType;
    }

    public long getPageMessageId() {
        return pageMessageId;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public ParserType getParserType() {
        return parserType;
    }

    public static final class Builder{

        private long pageMessageId;
        private String pageUrl;
        private ParserType parserType;

        public Builder(){
            // no implementation
        }

        public Builder setPageMessageId(long pageMessageId){
            this.pageMessageId = pageMessageId;
            return this;
        }

        public Builder setPageUrl(String pageUrl){
            this.pageUrl = pageUrl;
            return this;
        }

        public Builder setParsertype(ParserType parserType){
            this.parserType = parserType;
            return this;
        }

        public PageMessage build(){
            return new PageMessage(pageMessageId, pageUrl, parserType);
        }
        
    }
}
