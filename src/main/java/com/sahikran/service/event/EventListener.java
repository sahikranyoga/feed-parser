package com.sahikran.service.event;

public interface EventListener<T> {
    void listenToEvent(GenericEventMessage<T> eventMessage);
}
