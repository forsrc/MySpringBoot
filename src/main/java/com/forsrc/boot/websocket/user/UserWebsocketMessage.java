package com.forsrc.boot.websocket.user;

public class UserWebsocketMessage {

    private Long id;
    private String name;

    public UserWebsocketMessage() {
    }

    public UserWebsocketMessage(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
