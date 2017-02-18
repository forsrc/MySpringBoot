package com.forsrc.boot.web.user.service.impl;

import com.forsrc.boot.web.user.dao.UserRoleDao;
import com.forsrc.boot.web.user.service.UserRoleService;
import com.forsrc.pojo.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public List<UserRole> findByUserId(Long userId) {
        return userRoleDao.findByUserId(userId);
    }
}
