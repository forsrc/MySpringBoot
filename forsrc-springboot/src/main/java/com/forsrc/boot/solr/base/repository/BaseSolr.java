package com.forsrc.boot.solr.base.repository;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.data.domain.Pageable;

public interface BaseSolr<E, PK extends Serializable> {

    void save(final E e) throws IOException, SolrServerException;

    void delete(final String id) throws IOException, SolrServerException;

    List<E> getByQuery(final String query, final Pageable pageable) throws IOException, SolrServerException;

    String getSolrName();

    <T> T exec(final SolrHandler<T> solrHandler) throws IOException, SolrServerException;

    SolrDocumentList findByQuery(final String query, final Pageable page) throws IOException, SolrServerException;

    Class<E> getEntityClass();

    interface SolrHandler<T> {
        T handle(final HttpSolrClient httpSolrClient) throws IOException, SolrServerException;
    }
}
