package com.sahikran.exception;

public class PageParserException extends Exception {

    public PageParserException(String message){
        super(message);
    }

    public PageParserException(String message, Throwable throwable){
        super(message, throwable);
    }
}
