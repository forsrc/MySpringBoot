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
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.text.MessageFormat;

/**
 * The type My aes utils.
 *
 * @ClassName: MyAesUtils
 * @Description: The MyAesUtils is a singleton class.
 */
public final class MyAesUtils {

    /**
     * The constant CHARSET_ASCII.
     */
    public static final String CHARSET_ASCII = "ASCII";
    /**
     * The constant CHARSET_UTF8.
     */
    public static final String CHARSET_UTF8 = "UTF-8";
    private static final String PREFIX = "Salted__";
    private static final byte[] SALT = {0, 0, 0, 0, 0, 0, 0, 0};

    /**
     * Decrypt string.
     *
     * @param aes  the aes
     * @param code the code
     * @return String string
     * @throws AesException the aes exception
     * @Title: decrypt
     * @Description:
     */
    public static String decrypt(AesKey aes, String code) throws AesException {
        byte[] encrypted = null;
        try {
            encrypted = new Base64().decode(code);
        } catch (Exception e) {
            throw new AesException(e);
        }

        byte[] original = null;
        try {
            original = aes.getDecryptCipher().doFinal(encrypted);
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
     * @param aes the aes
     * @param src the src
     * @return String string
     * @throws AesException the aes exception
     * @Title: encrypt
     * @Description:
     */
    public static String encrypt(AesKey aes, String src) throws AesException {

        byte[] encrypted = null;
        try {
            encrypted = aes.getEncryptCipher().doFinal(
                    src.getBytes(CHARSET_UTF8));
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
     * @param aes the aes
     * @param pwd the pwd
     * @return String decrypt password
     * @throws AesException the aes exception
     * @Title: getDecryptPassword
     * @Description:
     */
    public static String getDecryptPassword(AesKey aes, String pwd)
            throws AesException {

        try {
            String pass = decrypt(aes, pwd);
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
     * @param aes the aes
     * @param pwd the pwd
     * @return String encrypt password
     * @throws AesException the aes exception
     * @Title: getEncryptPassword
     * @Description:
     */
    public static String getEncryptPassword(AesKey aes, String pwd)
            throws AesException {

        try {
            return encrypt(aes,
                    MessageFormat.format("{0}{1}{2}", System.currentTimeMillis(), pwd,
                            System.currentTimeMillis()));
        } catch (AesException e) {
            throw e;
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

    /**
     * The type Aes key.
     */
    public static class AesKey implements Serializable {

        private static final String CIPHER_KEY = "AES/CBC/PKCS7Padding";

        private static final String IV_PARAMETER = "0123456789abcdef";

        private static final String KEY = "1234567890abcdef";

        private static final String SECRET_KEY = "AES";
        private static final String PROVIDER = "BC";

        private String key;
        private String iv;

        /**
         * Instantiates a new Aes key.
         */
        public AesKey() {
            this.key = generateKey();
            this.iv = generateIv();
        }

        /**
         * Instantiates a new Aes key.
         *
         * @param key the key
         * @param iv  the iv
         */
        public AesKey(String key, String iv) {
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
            return MyStringUtils.generate(16);
        }

        /**
         * Generate iv string.
         *
         * @return the string
         */
        public String generateIv() {
            return MyStringUtils.generate(16);
        }


        /**
         * Gets encrypt cipher.
         *
         * @return the encrypt cipher
         * @throws AesException the aes exception
         */
        public Cipher getEncryptCipher() throws AesException {
            Cipher cipher = getCipher();
            try {
                cipher.init(Cipher.ENCRYPT_MODE,
                        new SecretKeySpec(this.key.getBytes(), SECRET_KEY),
                        new IvParameterSpec(this.iv.getBytes()));
            } catch (InvalidKeyException e) {
                throw new AesException(e);
            } catch (InvalidAlgorithmParameterException e) {
                throw new AesException(e);
            }
            return cipher;
        }

        /**
         * Gets decrypt cipher.
         *
         * @return the decrypt cipher
         * @throws AesException the aes exception
         */
        public Cipher getDecryptCipher() throws AesException {
            Cipher cipher = getCipher();
            try {
                cipher.init(Cipher.DECRYPT_MODE,
                        new SecretKeySpec(this.key.getBytes(), SECRET_KEY),
                        new IvParameterSpec(this.iv.getBytes()));
            } catch (InvalidKeyException e) {
                throw new AesException(e);
            } catch (InvalidAlgorithmParameterException e) {
                throw new AesException(e);
            }
            return cipher;
        }

        private Cipher getCipher() throws AesException {
            Cipher cipher = null;
            try {
                Security.addProvider(new BouncyCastleProvider());

                cipher = Cipher.getInstance(CIPHER_KEY);
            } catch (NoSuchAlgorithmException e) {
                throw new AesException(e);
            } catch (NoSuchPaddingException e) {
                throw new AesException(e);
            } /*catch (NoSuchProviderException e) {
                throw new AesException(e);
            }*/
            return cipher;
        }
    }
}
