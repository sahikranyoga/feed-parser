package com.sahikran.service;

import java.util.concurrent.CompletableFuture;

import com.sahikran.model.PageMessage;
import com.sahikran.model.Result;

public interface ParseService {
    
    CompletableFuture<Result> parse(PageMessage pageMessage);
}
