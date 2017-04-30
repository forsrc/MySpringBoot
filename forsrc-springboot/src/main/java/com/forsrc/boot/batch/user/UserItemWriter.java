package com.forsrc.boot.batch.user;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.forsrc.core.web.user.dao.UserDao;
import com.forsrc.pojo.User;

@Repository
@Transactional
public class UserItemWriter implements ItemWriter<User> {

    @Autowired
    private UserDao userDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void write(List<? extends User> list) throws Exception {
        for (User user : list) {
            System.out.println(user);
            // userDao.save(user);
        }
    }
}
