package com.forsrc.boot.solr.user.repository;


import com.forsrc.boot.solr.base.repository.BaseSolr;
import com.forsrc.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserSolr extends BaseSolr<User, Long> {

    List<User> findByUsername(String username, Pageable page);

    Page<User> findByEmail(String value, Pageable page);
}
