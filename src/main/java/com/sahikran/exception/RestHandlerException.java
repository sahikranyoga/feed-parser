package com.sahikran.exception;

public class RestHandlerException extends RuntimeException {
    
    public RestHandlerException(String message){
        super(message);
    }

    public RestHandlerException(String message, Throwable throwable){
        super(message, throwable);
    }
}
