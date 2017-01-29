package com.forsrc.boot.solr.base.repository;


import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface BaseSolr<E, PK extends Serializable> {

    public void save(final E e) throws IOException, SolrServerException;

    public void delete(final String id) throws IOException, SolrServerException;

    public List<E> getByQuery(final String query, final Pageable pageable) throws IOException, SolrServerException;

    public String getSolrName();

    public <T> T exec(final SolrHandler<T> solrHandler) throws IOException, SolrServerException;

    public SolrDocumentList findByQuery(final String query, final Pageable page) throws IOException, SolrServerException;

    public Class<E> getEntityClass();

    public static interface SolrHandler<T> {
        public T handle(final HttpSolrClient httpSolrClient) throws IOException, SolrServerException;
    }
}
