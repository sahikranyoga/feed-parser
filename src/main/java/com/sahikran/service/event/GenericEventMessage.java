package com.sahikran.service.event;

public class GenericEventMessage<T> {
    
    private final T eventMessage;
    
    protected boolean isSuccess;

    public GenericEventMessage(T eventMessage){
        this.eventMessage = eventMessage;
    }

    public T getWhat() {
        return eventMessage;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((eventMessage == null) ? 0 : eventMessage.hashCode());
        result = prime * result + (isSuccess ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GenericEventMessage<T> other = (GenericEventMessage<T>) obj;
        if (eventMessage == null) {
            if (other.eventMessage != null)
                return false;
        } else if (!eventMessage.equals(other.eventMessage))
            return false;
        if (isSuccess != other.isSuccess)
            return false;
        return true;
    }
    
}
