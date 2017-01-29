package com.forsrc.boot.solr.base.repository;


import java.io.Serializable;

public interface BaseSolr<E, PK extends Serializable> {

    public E save(E e);
}
