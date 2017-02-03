package com.forsrc.boot.web.security;

import java.io.Serializable;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

public class MyPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication a, Object o, Object o1) {
        return true;
    }

    @Override
    public boolean hasPermission(Authentication a, Serializable srlzbl, String string, Object o) {
        return true;
    }
}
