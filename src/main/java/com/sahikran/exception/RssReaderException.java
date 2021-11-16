package com.sahikran.exception;

public class RssReaderException extends Exception {
    
    public RssReaderException(String message){
        super(message);
    }

    public RssReaderException(String message, Throwable throwable){
        super(message, throwable);
    }
}
