package com.sahikran.exception;

public class ParseServiceException extends RuntimeException {
    
    static final long serialVersionUID = 1000l;

    public ParseServiceException(String message){
        super(message);
    }

    public ParseServiceException(String message, Throwable throwable){
        super(message, throwable);
    }
}
