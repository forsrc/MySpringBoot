package com.forsrc.boot.solr.base.repository.impl;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;

import com.forsrc.boot.solr.base.repository.BaseSolr;

public abstract class BaseSolrImpl<E, PK extends Serializable> implements BaseSolr<E, PK> {

    @Resource
    protected Environment env;

    public HttpSolrClient getHttpSolrClient() throws MalformedURLException, IllegalStateException {
        String url = env.getRequiredProperty("solr.host") + getSolrName();
        // System.out.println("---> " + url);
        return new HttpSolrClient(url);
    }

    @Override
    public void save(final E e) throws IOException, SolrServerException {
        exec(new SolrHandler<Void>() {
            @Override
            public Void handle(HttpSolrClient httpSolrClient) throws IOException, SolrServerException {
                httpSolrClient.addBean(e);
                System.out.println("--> httpSolrClient.addBean(e);");
                httpSolrClient.commit();
                return null;
            }
        });
    }

    @Override
    public void delete(final String id) throws IOException, SolrServerException {
        exec(new SolrHandler<Void>() {
            @Override
            public Void handle(HttpSolrClient httpSolrClient) throws IOException, SolrServerException {
                httpSolrClient.deleteById(id);
                httpSolrClient.commit();
                return null;
            }
        });
    }

    @Override
    public <T> T exec(SolrHandler<T> solrHandler) throws IOException, SolrServerException {
        HttpSolrClient httpSolrClient = null;
        try {
            httpSolrClient = getHttpSolrClient();
            return solrHandler.handle(httpSolrClient);
        } catch (SolrServerException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            if (httpSolrClient != null) {
                httpSolrClient.rollback();
                httpSolrClient.close();
            }
        }

    }

    @Override
    public SolrDocumentList findByQuery(final String query, final Pageable pageable)
            throws IOException, SolrServerException {
        return exec(new SolrHandler<SolrDocumentList>() {
            @Override
            public SolrDocumentList handle(HttpSolrClient httpSolrClient) throws IOException, SolrServerException {
                SolrQuery solrQuery = new SolrQuery();
                solrQuery.setQuery(query);
                solrQuery.setStart(pageable.getOffset());
                solrQuery.setRows(pageable.getPageSize());
                QueryResponse response = httpSolrClient.query(solrQuery);
                SolrDocumentList solrDocumentList = response.getResults();
                return solrDocumentList;
            }
        });
    }

    @Override
    public List<E> getByQuery(final String query, final Pageable pageable) throws IOException, SolrServerException {
        return exec(new SolrHandler<List<E>>() {
            @Override
            public List<E> handle(HttpSolrClient httpSolrClient) throws IOException, SolrServerException {
                SolrQuery solrQuery = new SolrQuery();
                solrQuery.setQuery(query);
                solrQuery.setStart(pageable.getOffset());
                solrQuery.setRows(pageable.getPageSize());
                QueryResponse response = httpSolrClient.query(solrQuery);
                return response.getBeans(getEntityClass());
            }
        });
    }
}
