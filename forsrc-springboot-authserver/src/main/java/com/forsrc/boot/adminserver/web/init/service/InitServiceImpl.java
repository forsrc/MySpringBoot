package com.forsrc.boot.adminserver.web.init.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.forsrc.boot.adminserver.web.init.dao.InitDao;

@Service
@Transactional
public class InitServiceImpl implements InitService {

    @Autowired
    private InitDao initDao;

    @Override
    @Transactional(transactionManager = "transactionManager", value = "transactionManager", propagation = Propagation.REQUIRED, readOnly = false)
    public void initDb() throws Exception {
        initDao.initDb();
    }
}
