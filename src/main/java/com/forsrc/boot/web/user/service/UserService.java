package com.forsrc.boot.web.user.service;

import com.forsrc.pojo.User;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@Service
public interface UserService {

    @Transactional(transactionManager = "txManager01", value = "txManager01", propagation = Propagation.REQUIRED, readOnly = false)
    public void save(User user);

    @Transactional(transactionManager = "txManager01", value = "txManager01", propagation = Propagation.REQUIRED, readOnly = false)
    public void save(User user, char[] password);

    @Transactional(transactionManager = "txManager01", propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(User user);

    @Transactional(transactionManager = "txManager01", propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public User get(long id);

    @Transactional(transactionManager = "txManager01", propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<User> get(int start, int size);

    @Transactional(transactionManager = "txManager01", propagation = Propagation.REQUIRED, readOnly = false)
    public void update(User user);
}
