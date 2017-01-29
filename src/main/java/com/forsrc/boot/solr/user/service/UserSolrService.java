package com.forsrc.boot.solr.user.service;


import com.forsrc.pojo.User;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Repository
@Service
public interface UserSolrService {

    public void save(User user) throws IOException, SolrServerException;

    List<User> findByUsername(String username, Pageable page) throws IOException, SolrServerException, Exception;

}
