package com.forsrc.boot.batch.pojo;

import java.util.Arrays;
import java.util.Date;

public class BatchTarget {

    private Long id;
    private Long parentId;
    private Long[] children;
    private String name;
    private Date start;
    private Date end;

    public BatchTarget() {
    }

    public BatchTarget(Long id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long[] getChildren() {
        return children;
    }

    public void setChildren(Long[] children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "BatchTarget [id=" + id + ", parentId=" + parentId + ", children=" + Arrays.toString(children)
                + ", name=" + name + ", start=" + start + ", end=" + end + "]";
    }

}
