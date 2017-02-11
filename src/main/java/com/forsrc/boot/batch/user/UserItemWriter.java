package com.forsrc.boot.batch.user;

import com.forsrc.boot.web.user.dao.UserDao;
import com.forsrc.pojo.User;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            //userDao.save(user);
        }
    }
}
