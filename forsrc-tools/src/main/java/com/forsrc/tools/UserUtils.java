package com.forsrc.tools;

import org.apache.commons.codec.digest.DigestUtils;

public final class UserUtils {

    public static final String encodePassword(String username, String password) {

        return DigestUtils.md5Hex(username + password);
    }
}
