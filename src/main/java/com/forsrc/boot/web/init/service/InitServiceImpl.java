package com.forsrc.boot.web.init.service.impl;

import com.forsrc.boot.web.init.dao.InitDao;
import com.forsrc.boot.web.init.service.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
