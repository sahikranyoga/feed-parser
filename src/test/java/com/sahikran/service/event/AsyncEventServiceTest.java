package com.sahikran.service.event;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AsyncEventServiceTest {
    
    @Autowired
    private EventService<Set<String>> asynEventService;

    @Test
    public void whenAnEventIsPublished_verifyCallToListener(){
        Set<String> files = new HashSet<>();
        files.add("32322442.json");
        GenericEventMessage<Set<String>> genericEventMessage = new GenericEventMessage<>(files);
        assertTrue(asynEventService.publishEvent(genericEventMessage).isSuccess());
    }

}
