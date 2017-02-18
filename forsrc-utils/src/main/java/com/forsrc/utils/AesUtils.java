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

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;

/**
 * The type Aes utils.
 *
 * @ClassName: AesUtils
 * @Description: The AesUtils is a singleton class.
 */
public final class AesUtils {

    /**
     * The constant CHARSET_ASCII.
     */
    public static final String CHARSET_ASCII = "ASCII";
    /**
     * The constant CHARSET_UTF8.
     */
    public static final String CHARSET_UTF8 = "UTF-8";
    private static final String CIPHER_KEY = "AES/CBC/PKCS5Padding";
    private static final String IV_PARAMETER = "x0123456789abcde";
    private static final String KEY = "x0123456789abcde";
    private static final String SECRET_KEY = "AES";

    private AesUtils() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static AesUtils getInstance() {

        return AesUtilsStaticClass.INSTANCE;
    }

    /**
     * Decrypt string.
     *
     * @param code the code
     * @return String string
     * @throws AesException the aes exception
     * @Title: decrypt
     * @Description:
     */
    public String decrypt(String code) throws AesException {

        byte[] raw = null;
        try {
            raw = KEY.getBytes(CHARSET_ASCII);
        } catch (UnsupportedEncodingException e) {
            throw new AesException(e);
        }

        SecretKeySpec skeySpec = new SecretKeySpec(raw, SECRET_KEY);

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(CIPHER_KEY);
        } catch (NoSuchAlgorithmException e) {
            throw new AesException(e);
        } catch (NoSuchPaddingException e) {
            throw new AesException(e);
        }

        IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes());

        try {
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        } catch (InvalidKeyException e) {
            throw new AesException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new AesException(e);
        }

        byte[] encrypted = null;
        try {
            encrypted = new Base64().decode(code);
        } catch (Exception e) {
            throw new AesException(e);
        }

        byte[] original = null;
        try {
            original = cipher.doFinal(encrypted);
        } catch (IllegalBlockSizeException e) {
            throw new AesException(e);
        } catch (BadPaddingException e) {
            throw new AesException(e);
        }

        try {
            return new String(original, CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new AesException(e);
        }

    }

    /**
     * Encrypt string.
     *
     * @param src the src
     * @return String string
     * @throws AesException the aes exception
     * @Title: encrypt
     * @Description:
     */
    public String encrypt(String src) throws AesException {

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(CIPHER_KEY);
        } catch (NoSuchAlgorithmException e) {
            throw new AesException(e);
        } catch (NoSuchPaddingException e) {
            throw new AesException(e);
        }

        byte[] raw = KEY.getBytes();

        SecretKeySpec secretKeySpec = new SecretKeySpec(raw, SECRET_KEY);

        IvParameterSpec ivParameterSpec = new IvParameterSpec(
                IV_PARAMETER.getBytes());

        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        } catch (InvalidKeyException e) {
            throw new AesException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new AesException(e);
        }

        byte[] encrypted;
        try {
            encrypted = cipher.doFinal(src.getBytes(CHARSET_UTF8));
        } catch (IllegalBlockSizeException e) {
            throw new AesException(e);
        } catch (BadPaddingException e) {
            throw new AesException(e);
        } catch (UnsupportedEncodingException e) {
            throw new AesException(e);
        }

        return new String(new Base64().encode(encrypted));
    }

    /**
     * Gets decrypt password.
     *
     * @param pwd the pwd
     * @return String decrypt password
     * @throws AesException the aes exception
     * @Title: getDecryptPassword
     * @Description:
     */
    public String getDecryptPassword(String pwd) throws AesException {

        try {
            String pass = decrypt(pwd);
            return pass.substring(
                    (String.valueOf(System.currentTimeMillis())).length(),
                    pass.length()
                            - (String.valueOf(System.currentTimeMillis()))
                            .length());
        } catch (AesException e) {
            throw e;
        }
    }

    /**
     * Gets encrypt password.
     *
     * @param pwd the pwd
     * @return String encrypt password
     * @throws AesException the aes exception
     * @Title: getEncryptPassword
     * @Description:
     */
    public String getEncryptPassword(String pwd) throws AesException {

        try {
            return encrypt(MessageFormat.format("{0}{1}{2}", System.currentTimeMillis(),
                    pwd, System.currentTimeMillis()));
        } catch (AesException e) {
            throw e;
        }
    }

    /**
     * Matches boolean.
     *
     * @param password the password
     * @param pwd      the pwd
     * @return the boolean
     */
    public boolean matches(String password, String pwd) {
        if (MyStringUtils.isBlank(password) || MyStringUtils.isBlank(pwd)) {
            return false;
        }
        try {
            return getDecryptPassword(password).equals(getDecryptPassword(pwd));
        } catch (AesException e) {
            return false;
        }
    }

    /**
     * The type Aes exception.
     */
    public static class AesException extends Exception {
        /**
         * Instantiates a new Aes exception.
         *
         * @param cause the cause
         */
        public AesException(Throwable cause) {
            super(cause);
        }
    }

    private static class AesUtilsStaticClass {
        private static final AesUtils INSTANCE = new AesUtils();

        private AesUtilsStaticClass() {
        }
    }

}
