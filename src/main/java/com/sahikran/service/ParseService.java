package com.sahikran.service;

import java.util.concurrent.CompletableFuture;

import com.sahikran.exception.ParseServiceException;
import com.sahikran.model.PageMessage;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

public interface ParseService<T> {
    
    @Retryable( value = ParseServiceException.class, maxAttemptsExpression = "${retry.maxAttempts}",
    backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
    CompletableFuture<T> parse(PageMessage pageMessage);
}
