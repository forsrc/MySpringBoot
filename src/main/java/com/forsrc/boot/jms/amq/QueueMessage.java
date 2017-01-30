package com.forsrc.boot.jms.amq;

import java.util.UUID;

public class QueueMessage {

    private UUID id;
    private String message;

    public QueueMessage() {
        setId(UUID.randomUUID());
    }

    public String toString() {
        return String.format("QueuedItem [id=%s,message=%s]", id, message);
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UUID getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
