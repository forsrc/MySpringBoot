package com.forsrc.boot.solr.user.service.impl;



import com.forsrc.boot.solr.user.repository.UserSolr;
import com.forsrc.boot.solr.user.service.UserSolrService;
import com.forsrc.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;

public class UserSolrServiceImpl implements UserSolrService {

    private UserSolr repository;


    @Autowired
    public void setRepository(UserSolr repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        return this.repository.save(user);
    }
}
