package com.sahikran.keygenservice;

import java.time.Instant;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.sahikran.exception.KeyGenException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RateKeyGenerator implements UniqueKeyGenerator {

    private static final Logger log = LoggerFactory.getLogger(RateKeyGenerator.class);

    private final int MAX_TOKENS;
    private Deque<Long> keys = new ConcurrentLinkedDeque<>();

    public RateKeyGenerator(@Value("${keygen.maxTokens}") int maxTokens){
        if(maxTokens > 1000)
            throw new KeyGenException("max token can not be more than 1000");
        this.MAX_TOKENS = maxTokens;
    }

    @Override
    public synchronized long generateUniqueKey() {
        // first request, check if there are any tokens
        // if there is any, pop the top and return
        // else generate 100 (based on maximum tokens) unique keys and store them
        // also pop and return one
        if(keys.isEmpty()){
            log.debug("generating keys");
            // generate and store
            for(int i = 0; i < MAX_TOKENS; i++){
                keys.offer((Instant.now().toEpochMilli() + i));
            }
        }
        log.debug("rate token generated for thread " + Thread.currentThread().getName());
        return keys.pop();
    }
}
