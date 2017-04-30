package com.forsrc.boot.solr.user.service;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.forsrc.pojo.User;

@Repository
@Service
public interface UserSolrService {

    void save(User user) throws IOException, SolrServerException;

    List<User> findByUsername(String username, Pageable page) throws Exception;

}
