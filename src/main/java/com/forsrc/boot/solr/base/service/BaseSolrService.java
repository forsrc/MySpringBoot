package com.forsrc.boot.solr.base.service;


import java.io.Serializable;

public interface BaseSolrService<T, PK extends Serializable> {

    public T save(T t);

    public void delete(T t);
}
