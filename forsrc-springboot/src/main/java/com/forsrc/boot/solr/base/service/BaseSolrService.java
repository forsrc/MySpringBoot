package com.forsrc.boot.solr.base.service;

import java.io.Serializable;

public interface BaseSolrService<T, PK extends Serializable> {

    T save(T t);

    void delete(T t);
}
