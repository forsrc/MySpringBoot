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

public class MyUserDetails implements UserDetails {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleServiceu;
    private UserPrivacy userPrivacy;
    private static Map<Long, Role> ROLE_MAP;

    public MyUserDetails(UserPrivacy userPrivacy) {
        this.userPrivacy = userPrivacy;
        loadRoles();
    }

    private void loadRoles() {
        if (ROLE_MAP != null) {
            return;
        }
        synchronized (ROLE_MAP) {
            if (ROLE_MAP != null) {
                return;
            }
            ROLE_MAP = new HashMap<>();
            List<Role> roles = roleService.getRoles();
            for (Role role : roles) {
                ROLE_MAP.put(role.getId(), role);
            }
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<>();
        //auths.add(new SimpleGrantedAuthority("ROLE_TEST"));
        List<UserRole> userRoles = userRoleServiceu.findByUserId(userPrivacy.getUserId());
        for (UserRole userRole : userRoles) {
            Role role = ROLE_MAP.get(userRole.getRoleId());
            if (role != null) {
                auths.add(new SimpleGrantedAuthority(role.getName()));
            }
        }
        return auths;
    }

    @Override
    public String getPassword() {
        return userPrivacy.getPassword();
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
}
