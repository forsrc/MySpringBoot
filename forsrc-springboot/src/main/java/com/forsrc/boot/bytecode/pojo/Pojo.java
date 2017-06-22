package com.forsrc.boot.bytecode.pojo;

public abstract class Pojo {

    protected Long id;

    public Pojo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
