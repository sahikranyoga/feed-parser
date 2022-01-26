package com.sahikran.service.event;

public interface EventService<T> {
    GenericEventMessage<T> publishEvent(GenericEventMessage<T> t);
}
