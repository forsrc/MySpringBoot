package com.forsrc.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The type Message digest utils.
 */
public class MessageDigestUtils {

    private static MessageDigest md = null;

    static {
        try {
            md = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            LogUtils.LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Md 5 string.
     *
     * @param string the string
     * @return the string
     */
    public static String md5(String string) {
        if (string == null) {
            return null;
        }
        return new String(Hex.encodeHex(md.digest(StringUtils
                .getBytesUtf8(string))));
        // return new String(DigestUtils.md5Hex(string));
    }

}
