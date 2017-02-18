package com.forsrc.utils;

import org.apache.commons.codec.digest.DigestUtils;

public final class UserUtils {

    public static final String encodePassword(String username, String password) {

        return DigestUtils.md5Hex(username + password);
    }
}
