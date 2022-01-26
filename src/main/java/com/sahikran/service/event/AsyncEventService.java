package com.sahikran.service.event;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class AsyncEventService<T> implements EventService<T> {

    private static final Logger log = LoggerFactory.getLogger(AsyncEventService.class);

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher){
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public GenericEventMessage<T> publishEvent(GenericEventMessage<T> genericEventMessage) {
        log.info("publisher called");
        applicationEventPublisher.publishEvent(genericEventMessage);
        genericEventMessage.setSuccess(true);
        return genericEventMessage;
    }
    
    
}
