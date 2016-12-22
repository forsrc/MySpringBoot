package com.forsrc.boot.web.user.service;

import com.forsrc.pojo.User;
import java.util.List;

public interface UserService {

    public void save(User user);

    public void delete(User user);

    public User get(long id);
    
    public List<User> get(int start, int size);

    public void update(User user);
}
