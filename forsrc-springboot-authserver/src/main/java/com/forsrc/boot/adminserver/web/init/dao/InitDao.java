package com.forsrc.boot.adminserver.web.init.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface InitDao {

    void initDb() throws Exception;
}
