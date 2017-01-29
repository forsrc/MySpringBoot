package com.forsrc.boot.solr.base.repository;


import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public interface BaseSolr<E, PK extends Serializable> {

    public E save(E e);

    public String getSolrName();

    public <T> T exec(SolrHandler<T> solrHandler) throws IOException, SolrServerException, Exception;

    public SolrDocumentList findByQuery(final String query, Pageable page) throws Exception;

    public static interface SolrHandler<T> {
        public T handle(HttpSolrClient httpSolrClient) throws IOException, SolrServerException, IllegalAccessException, InvocationTargetException, InstantiationException;
    }
}
