package com.forsrc.boot.web.security;

import com.forsrc.boot.web.user.service.RoleService;
import com.forsrc.boot.web.user.service.UserRoleService;
import com.forsrc.boot.web.user.service.UserService;
import com.forsrc.pojo.Role;
import com.forsrc.pojo.UserPrivacy;
import com.forsrc.pojo.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityService {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;

    public UserPrivacy findByUsername(String username) {
        return userService.findByUsername(username);
    }

    List<UserRole> findByUserId(Long userId) {
        return userRoleService.findByUserId(userId);
    }

    List<Role> getRoles() {
        return roleService.getRoles();
    }
}
