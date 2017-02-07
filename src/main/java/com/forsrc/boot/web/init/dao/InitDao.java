package com.forsrc.boot.web.init.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface InitDao {

    public void initDb() throws Exception;
}
