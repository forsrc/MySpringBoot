package com.forsrc.boot.solr.user.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.forsrc.boot.solr.user.repository.UserSolr;
import com.forsrc.boot.solr.user.service.UserSolrService;
import com.forsrc.pojo.User;

@Service
public class UserSolrServiceImpl implements UserSolrService {

    private UserSolr repository;

    @Autowired
    public void setRepository(UserSolr repository) {
        this.repository = repository;
    }

    @Override
    public void save(User user) throws IOException, SolrServerException {
        this.repository.save(user);
    }

    @Override
    public List<User> findByUsername(String username, Pageable pageable) throws Exception {
        return repository.findByUsername(username, pageable);
    }
}
