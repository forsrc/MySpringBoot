package com.forsrc.boot.solr.user.repository;


import com.forsrc.boot.solr.base.repository.BaseSolr;
import com.forsrc.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
public interface UserSolr extends BaseSolr<User, Long> {

    List<User> findByUsername(String username, Pageable page) throws Exception;

    Page<User> findByEmail(String value, Pageable page);
}
