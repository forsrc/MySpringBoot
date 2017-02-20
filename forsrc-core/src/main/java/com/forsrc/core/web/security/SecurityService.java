package com.forsrc.core.web.security;

import com.forsrc.core.web.user.service.RoleService;
import com.forsrc.core.web.user.service.UserRoleService;
import com.forsrc.core.web.user.service.UserService;
import com.forsrc.pojo.Role;
import com.forsrc.pojo.UserPrivacy;
import com.forsrc.pojo.UserRole;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
