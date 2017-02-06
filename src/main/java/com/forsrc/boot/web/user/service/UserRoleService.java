package com.forsrc.boot.web.user.service;

import com.forsrc.pojo.UserRole;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
@Service
public interface UserRoleService {

    @Transactional(transactionManager = "transactionManager", propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<UserRole> findByUserId(Long userId);
}
