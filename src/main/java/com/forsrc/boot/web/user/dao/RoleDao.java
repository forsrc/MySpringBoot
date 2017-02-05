package com.forsrc.boot.web.user.dao;

import com.forsrc.boot.base.dao.BaseDao;
import com.forsrc.pojo.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDao extends BaseDao<Role, Long> {
    public List<Role> roles();
}
