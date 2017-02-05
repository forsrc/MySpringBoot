package com.forsrc.boot.web.user.dao.impl;

import com.forsrc.boot.base.dao.impl.BaseDaoImpl;
import com.forsrc.boot.web.user.dao.RoleDao;
import com.forsrc.pojo.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class RoleDaoImpl extends BaseDaoImpl<Role, Long> implements RoleDao {

    @Override
    public Class<Role> getEntityClass() {
        return Role.class;
    }


    @Override
    public List<Role> roles() {
        Query query = entityManager.createQuery(String.format("from %s", getEntityClassName()));
        return query.getResultList();
    }
}
