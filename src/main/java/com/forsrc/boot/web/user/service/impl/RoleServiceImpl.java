package com.forsrc.boot.web.user.service.impl;

import com.forsrc.boot.web.user.dao.RoleDao;
import com.forsrc.boot.web.user.service.RoleService;
import com.forsrc.pojo.Role;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public List<Role> getRoles(){
        return roleDao.getRoles();
    }
}