package com.sahikran.service.event;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FileEventListener implements EventListener<Set<String>> {

    private static final Logger log = LoggerFactory.getLogger(FileEventListener.class);


    @Override
    public void listenToEvent(GenericEventMessage<Set<String>> eventMessage) {
        log.info("received an event");
        // upload the files into S3 bucket
    }
    
}
