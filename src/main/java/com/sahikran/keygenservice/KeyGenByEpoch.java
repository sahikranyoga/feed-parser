package com.sahikran.keygenservice;

import java.time.Instant;

import org.springframework.stereotype.Component;

@Component
public class KeyGenByEpoch implements UniqueKeyGenerator {

    @Override
    public synchronized long generateUniqueKey() {
        // epoch time
        Instant instant = Instant.now();
        return instant.toEpochMilli();
    }

}
