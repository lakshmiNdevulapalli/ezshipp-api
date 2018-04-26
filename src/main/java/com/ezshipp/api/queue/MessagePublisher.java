package com.ezshipp.api.queue;

public interface MessagePublisher {

    void publish(final String message);
}
