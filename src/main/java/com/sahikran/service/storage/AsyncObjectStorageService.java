package com.sahikran.service.storage;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.sahikran.model.FeedItem;
import com.sahikran.model.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsyncObjectStorageService {
    
    private StorageService<FeedItem, String> objectStorageService;

    @Autowired
    public void setStorageService(StorageService<FeedItem, String> objectStorageService){
        this.objectStorageService = objectStorageService;
    }

    public CompletableFuture<Set<String>> SaveAsync(Result result){
            CompletableFuture<Set<String>> resultFuture = 
                    CompletableFuture.supplyAsync(() -> 
                        objectStorageService.save(result.getFeedItems())
                    );
            return resultFuture;
    }
}
