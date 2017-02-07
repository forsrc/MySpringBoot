package com.forsrc.boot.web.security;

import com.forsrc.boot.web.user.service.RoleService;
import com.forsrc.boot.web.user.service.UserRoleService;
import com.forsrc.boot.web.user.service.UserService;
import com.forsrc.pojo.Role;
import com.forsrc.pojo.UserPrivacy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserPrivacy user = userService.findByUsername(username);
        if (user == null) {
            System.out.println(String.format("--> MyUserDetailsService.loadUserByUsername() --> User is not exist: %s", username));
            throw new UsernameNotFoundException(String.format("User is not exist: %s", username));
        }
        System.out.println(String.format("--> MyUserDetailsService.loadUserByUsername() --> User is : %s", username));
        Map<Long, Role> roles = getRoles();
        MyUserDetails myUserDetails = new MyUserDetails(userRoleService, user, roles);
        return myUserDetails;
    }

    private Map<Long, Role> getRoles() {
        Map<Long, Role> map = new HashMap<>();
        List<Role> roles = roleService.getRoles();
        for (Role role : roles) {
            map.put(role.getId(), role);
        }
        return map;
    }
}
