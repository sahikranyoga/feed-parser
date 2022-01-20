package com.sahikran.service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.sahikran.model.FeedItem;
import com.sahikran.model.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsyncObjectStorageService {
    
    private StorageService<FeedItem> objectStorageService;

    @Autowired
    public void setStorageService(StorageService<FeedItem> objectStorageService){
        this.objectStorageService = objectStorageService;
    }

    public CompletableFuture<Boolean> SaveAsync(Result result){
            CompletableFuture<Boolean> resultFuture = 
                    CompletableFuture.supplyAsync(() -> 
                        objectStorageService.save(result.getFeedItems())
                    );
            return resultFuture;
    }
}
