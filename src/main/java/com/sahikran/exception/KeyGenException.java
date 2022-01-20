package com.sahikran.exception;

public class KeyGenException extends RuntimeException {
    
    static final long serialVersionUID = 2000l;

    public KeyGenException(String message){
        super(message);
    }

    public KeyGenException(String message, Throwable throwable){
        super(message, throwable);
    }
}
