package com.forsrc.boot.web.security;

import com.forsrc.boot.web.user.service.RoleService;
import com.forsrc.boot.web.user.service.UserRoleService;
import com.forsrc.boot.web.user.service.UserService;
import com.forsrc.pojo.Role;
import com.forsrc.pojo.UserPrivacy;
import com.forsrc.pojo.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

public class MyUserDetails implements UserDetails {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;
    private UserPrivacy userPrivacy;
    private Map<Long, Role> roles;

    public MyUserDetails(UserRoleService userRoleService, UserPrivacy userPrivacy, Map<Long, Role> roles) {
        this.userRoleService = userRoleService;
        this.roles = roles;
        this.userPrivacy = userPrivacy;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> auths = new ArrayList<>();
        //auths.add(new SimpleGrantedAuthority("ROLE_TEST"));
        List<UserRole> userRoles = userRoleService.findByUserId(userPrivacy.getUserId());
        for (UserRole userRole : userRoles) {
            Role role = roles.get(userRole.getRoleId());
            if (role != null) {
                auths.add(new SimpleGrantedAuthority(role.getName()));
            }
        }
        return auths;
    }

    @Override
    public String getPassword() {
        return new BCryptPasswordEncoder().encode(userPrivacy.getPassword());
    }

    @Override
    public String getUsername() {
        return userPrivacy.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserPrivacy getUserPrivacy() {
        return userPrivacy;
    }

    public void setUserPrivacy(UserPrivacy userPrivacy) {
        this.userPrivacy = userPrivacy;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public RoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public UserRoleService getUserRoleService() {
        return userRoleService;
    }

    public void setUserRoleService(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }
}
