/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.forsrc.utils;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.text.MessageFormat;

/**
 * The type My des utils.
 *
 * @ClassName: MyDesUtils
 * @Description: The MyDesUtils is a singleton class.
 */
public final class MyDesUtils {

    /**
     * The constant CHARSET_ASCII.
     */
    public static final String CHARSET_ASCII = "ASCII";
    /**
     * The constant CHARSET_UTF8.
     */
    public static final String CHARSET_UTF8 = "UTF-8";
    private static final String PREFIX = "Salted__";
    private static final byte[] SALT = {0, 1, 0, 1, 0, 1, 0, 1};

    /**
     * Decrypt string.
     *
     * @param des  the des
     * @param code the code
     * @return String string
     * @throws DesException the des exception
     * @Title: decrypt
     * @Description:
     */
    public static String decrypt(DesKey des, String code) throws DesException {
        if (des == null) {
            throw new DesException("DesKey is null.");
        }
        if (code == null) {
            throw new DesException("Decrypted code is null.");
        }
        byte[] encrypted = null;
        try {
            encrypted = new Base64().decode(code);
        } catch (Exception e) {
            throw new DesException(e);
        }

        byte[] original = null;
        try {
            original = des.getCipher(false).doFinal(encrypted);
        } catch (IllegalBlockSizeException e) {
            throw new DesException(e);
        } catch (BadPaddingException e) {
            throw new DesException(e);
        }

        try {
            return new String(original, CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new DesException(e);
        }

    }

    /**
     * Encrypt string.
     *
     * @param des the des
     * @param src the src
     * @return String string
     * @throws DesException the des exception
     * @Title: encrypt
     * @Description:
     */
    public static String encrypt(DesKey des, String src) throws DesException {
        if (des == null) {
            throw new DesException("DesKey is null.");
        }
        if (src == null) {
            throw new DesException("Encrypted code is null.");
        }
        byte[] encrypted = null;
        try {
            encrypted = des.getCipher(true).doFinal(src.getBytes(CHARSET_UTF8));
        } catch (IllegalBlockSizeException e) {
            throw new DesException(e);
        } catch (BadPaddingException e) {
            throw new DesException(e);
        } catch (UnsupportedEncodingException e) {
            throw new DesException(e);
        }
        return new String(new Base64().encode(encrypted));
    }

    /**
     * Gets decrypt password.
     *
     * @param des the des
     * @param pwd the pwd
     * @return String decrypt password
     * @throws DesException the des exception
     * @Title: getDecryptPassword
     * @Description:
     */
    public static String getDecryptPassword(DesKey des, String pwd)
            throws DesException {
        if (des == null) {
            throw new DesException("DesKey is null.");
        }
        if (pwd == null) {
            throw new DesException("Decrypted pwd is null.");
        }
        try {
            String pass = decrypt(des, pwd);
            return pass.substring(
                    (String.valueOf(System.currentTimeMillis())).length(),
                    pass.length()
                            - (String.valueOf(System.currentTimeMillis()))
                            .length());
        } catch (DesException e) {
            throw e;
        }
    }

    /**
     * Gets encrypt password.
     *
     * @param des the des
     * @param pwd the pwd
     * @return String encrypt password
     * @throws DesException the des exception
     * @Title: getEncryptPassword
     * @Description:
     */
    public static String getEncryptPassword(DesKey des, String pwd)
            throws DesException {
        if (des == null) {
            throw new DesException("DesKey is null.");
        }
        if (pwd == null) {
            throw new DesException("Encrypted pwd is null.");
        }
        try {
            return encrypt(des,
                    MessageFormat.format("{0}{1}{2}", System.currentTimeMillis(), pwd,
                            System.currentTimeMillis()));
        } catch (DesException e) {
            throw e;
        }
    }

    /**
     * The type Des exception.
     */
    public static class DesException extends Exception {
        /**
         * Instantiates a new Des exception.
         *
         * @param cause the cause
         */
        public DesException(Throwable cause) {
            super(cause);
        }

        /**
         * Instantiates a new Des exception.
         *
         * @param cause the cause
         */
        public DesException(String cause) {
            super(cause);
        }
    }

    /**
     * The type Des key.
     */
    public static class DesKey implements Serializable {

        private static final String CIPHER_KEY = "DES";

        private static final String IV_PARAMETER = "0123456789abcdef";

        private static final String KEY = "1234567890abcdef";

        private static final String SECRET_KEY = "DES";
        private static final String PROVIDER = "BC";

        private String key;
        private String iv;

        /**
         * Instantiates a new Des key.
         */
        public DesKey() {
            this.key = generateKey();
            this.iv = generateIv();
        }

        /**
         * Instantiates a new Des key.
         *
         * @param key the key
         * @param iv  the iv
         */
        public DesKey(String key, String iv) {
            this.key = key;
            this.iv = iv;
        }


        /**
         * Gets key.
         *
         * @return the key
         */
        public String getKey() {
            return this.key;
        }

        /**
         * Sets key.
         *
         * @param key the key
         */
        public void setKey(String key) {
            this.key = key;
        }

        /**
         * Gets iv.
         *
         * @return the iv
         */
        public String getIv() {
            return this.iv;
        }

        /**
         * Sets iv.
         *
         * @param iv the iv
         */
        public void setIv(String iv) {
            this.iv = iv;
        }

        /**
         * Generate key string.
         *
         * @return the string
         */
        public String generateKey() {
            return MyStringUtils.generate(8);
        }

        /**
         * Generate iv string.
         *
         * @return the string
         */
        public String generateIv() {
            return MyStringUtils.generate(8);
        }


        /**
         * Gets cipher.
         *
         * @param isEncrypt the is encrypt
         * @return the cipher
         * @throws DesException the des exception
         */
        public Cipher getCipher(boolean isEncrypt) throws DesException {
            Cipher cipher = null;
            try {
                cipher = Cipher.getInstance(CIPHER_KEY);
            } catch (NoSuchAlgorithmException e) {
                throw new DesException(e);
            } catch (NoSuchPaddingException e) {
                throw new DesException(e);
            }
            try {
                SecretKeyFactory keyFactory = SecretKeyFactory
                        .getInstance(SECRET_KEY);
                cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE
                        : Cipher.DECRYPT_MODE, keyFactory
                        .generateSecret(new DESKeySpec(this.key
                                .getBytes(CHARSET_UTF8))), SecureRandom
                        .getInstance("SHA1PRNG"));
            } catch (InvalidKeyException e) {
                throw new DesException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new DesException(e);
            } catch (InvalidKeySpecException e) {
                throw new DesException(e);
            } catch (UnsupportedEncodingException e) {
                throw new DesException(e);
            }
            return cipher;
        }
    }
}