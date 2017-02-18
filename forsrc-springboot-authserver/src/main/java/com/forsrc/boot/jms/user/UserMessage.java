package com.forsrc.boot.jms.user;

import java.util.UUID;

public class UserMessage {

    private UUID id;
    private String message;

    public UserMessage() {
        setId(UUID.randomUUID());
    }

    public UserMessage(String message) {
        setId(UUID.randomUUID());
        this.message = message;
    }

    public String toString() {
        return String.format("UserMessage{id = %s, message = %s}", id, message);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
