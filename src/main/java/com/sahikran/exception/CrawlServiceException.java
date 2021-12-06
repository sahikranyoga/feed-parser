package com.sahikran.exception;

public class CrawlServiceException extends RuntimeException {
    
    static final long serialVersionUID = 1000l;

    public CrawlServiceException(String message){
        super(message);
    }

    public CrawlServiceException(String message, Throwable throwable){
        super(message, throwable);
    }
}
