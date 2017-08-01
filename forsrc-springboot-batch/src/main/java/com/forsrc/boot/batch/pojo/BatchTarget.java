package com.forsrc.boot.batch.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Table(name = "t_batch_target")
public class BatchTarget {

    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @JsonProperty("parent_id")
    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "children", length = 1000, nullable = false, columnDefinition = "VARCHAR(100) DEFAULT ''")
    private String children;
 
    @Column(name = "name", length = 100, nullable = false, columnDefinition = "VARCHAR(100) DEFAULT ''")
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start")
    private Date start;

    @Column(name = "end")
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

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
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
        return "BatchTarget [id=" + id + ", parentId=" + parentId + ", children=" + children
                + ", name=" + name + ", start=" + start + ", end=" + end + "]";
    }

}
