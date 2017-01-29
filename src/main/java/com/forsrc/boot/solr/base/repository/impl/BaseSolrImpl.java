package com.forsrc.boot.solr.base.repository.impl;


import com.forsrc.boot.solr.base.repository.BaseSolr;
import com.forsrc.pojo.User;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseSolrImpl<E, PK extends Serializable> implements BaseSolr<E, PK> {

    @Resource
    protected Environment env;

    public HttpSolrClient getHttpSolrClient() throws MalformedURLException, IllegalStateException {
        String url = env.getRequiredProperty("solr.host") + getSolrName();
        //System.out.println("---> " + url);
        return new HttpSolrClient(url);
    }


    @Override
    public E save(E e) {
        return null;
    }

    @Override
    public <T> T exec(SolrHandler<T> solrHandler) throws Exception {
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
                httpSolrClient.close();
            }
        }

    }

    @Override
    public SolrDocumentList findByQuery(final String query, final Pageable pageable) throws Exception {
        final List<User> list = new ArrayList<>();
        return exec(new SolrHandler<SolrDocumentList>() {
            @Override
            public SolrDocumentList handle(HttpSolrClient httpSolrClient) throws IOException, SolrServerException, IllegalAccessException, InvocationTargetException, InstantiationException {
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
}
