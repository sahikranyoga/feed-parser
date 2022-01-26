package com.sahikran.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.sahikran.parser.PageParserFactoryImpl.ParserType;

@JsonDeserialize(builder = PageMessage.Builder.class)
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (pageMessageId ^ (pageMessageId >>> 32));
        result = prime * result + ((pageUrl == null) ? 0 : pageUrl.hashCode());
        result = prime * result + ((parserType == null) ? 0 : parserType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PageMessage other = (PageMessage) obj;
        if (pageMessageId != other.pageMessageId)
            return false;
        if (pageUrl == null) {
            if (other.pageUrl != null)
                return false;
        } else if (!pageUrl.equals(other.pageUrl))
            return false;
        if (parserType != other.parserType)
            return false;
        return true;
    }

    @JsonPOJOBuilder(withPrefix = "set")
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

        public Builder setParserType(ParserType parserType){
            this.parserType = parserType;
            return this;
        }

        public PageMessage build(){
            return new PageMessage(pageMessageId, pageUrl, parserType);
        }
        
    }
}
