package com.sahikran.keygenservice;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RateKeyGeneratorTest {

    private static final Logger log = LoggerFactory.getLogger(RateKeyGeneratorTest.class);
    
    private final UniqueKeyGenerator uniqueKeyGenerator = new RateKeyGenerator(100);

    @Test
    public void whenTenThreadsRequestForTokens_returnAllUniqueTokens(){

        Set<Thread> allThreads = new HashSet<>();
        Set<Long> keysGenerated = new ConcurrentSkipListSet<>();
        int numberOfThreads = 200;

        for(int i = 0; i < numberOfThreads; i++){
            Thread thread = new Thread( new Runnable() {
                public void run(){
                    long key = uniqueKeyGenerator.generateUniqueKey();
                    if(!keysGenerated.add(key)){
                        log.info("duplicate key " + key);
                    }
                    //log.info("key generated is " + key);
                    assertNotNull(key);
                }
            });
            thread.setName("keyGenThread_" + i);
            allThreads.add(thread);
        }

        allThreads.forEach(
            t -> t.start()
        );

        log.info("Threads started");

        allThreads.forEach(
            t -> {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        );

        log.info("number of keys generated are " + keysGenerated.size());
        assertTrue(keysGenerated.size() == numberOfThreads);
        log.info("Threads ended");
        
    }
}
