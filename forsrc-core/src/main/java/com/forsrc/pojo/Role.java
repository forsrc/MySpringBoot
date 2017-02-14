package com.forsrc.pojo;

import java.text.MessageFormat;
import java.util.Date;

public class Role implements java.io.Serializable {

    private Long id;
    private String name;
    private Long parentId;
    private Date updateOn;
    private Date createOn;
    private int status; // 0: delete; 1: OK; 2: NG
    private int version;

    public Role() {
    }

    public Role(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
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

    public Date getUpdateOn() {
        return updateOn;
    }

    public void setUpdateOn(Date updateOn) {
        this.updateOn = updateOn;
    }

    public Date getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return MessageFormat.format("'{'\"id\" : \"{0}\", \"userId\" : \"{1}\"'}'", id, name);
    }
}