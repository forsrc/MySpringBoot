package com.forsrc.boot.solr.user.repository.impl;


import com.forsrc.boot.solr.user.repository.UserSolr;
import com.forsrc.pojo.User;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class UserSolrImpl implements UserSolr {

    private HttpSolrClient httpSolrClient;

    @Override
    public List<User> findByUsername(String username, Pageable page) {


        return null;
    }

    @Override
    public Page<User> findByEmail(String value, Pageable page) {
        return null;
    }


    @Override
    public User save(User user) {

        return user;
    }

    public HttpSolrClient getHttpSolrClient() {
        return httpSolrClient;
    }

    @Autowired
    public void setHttpSolrClient(HttpSolrClient httpSolrClient) {
        this.httpSolrClient = httpSolrClient;
    }
}
