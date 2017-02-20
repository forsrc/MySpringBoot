package com.forsrc.core.web.user.dao.impl;

import com.forsrc.core.base.dao.impl.BaseDaoImpl;
import com.forsrc.core.web.user.dao.RoleDao;
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
    public List<Role> getRoles() {
        Query query = entityManager.createNamedQuery("sql_role_getRoles", getEntityClass());
        return query.getResultList();
    }
}
