package com.sahikran.exception;

public class ObjectStorageException extends RuntimeException {
    
    static final long serialVersionUID = 1000l;

    public ObjectStorageException(String message){
        super(message);
    }

    public ObjectStorageException(String message, Throwable throwable){
        super(message, throwable);
    }
}
