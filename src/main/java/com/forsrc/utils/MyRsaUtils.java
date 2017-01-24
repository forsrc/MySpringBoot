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

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;

/**
 * The type My rsa utils.
 */
public final class MyRsaUtils {

    /**
     * The constant BLOCK_SIZE.
     */
    public static final int BLOCK_SIZE = 102;

    private static final String CHAR_SET = "UTF-8";

    /**
     * Decrypt big integer.
     *
     * @param rsaKey    the rsa key
     * @param encrypted the encrypted
     * @return the big integer
     */
    public static BigInteger decrypt(RsaKey rsaKey, BigInteger encrypted) {
        // C = (encrypted^privateKey) * mod m
        return encrypted.modPow(rsaKey.getPrivateKey()/* d */, rsaKey.getN());
    }

    /**
     * Decrypt string.
     *
     * @param rsaKey    the rsa key
     * @param encrypted the encrypted
     * @return the string
     * @throws IOException the io exception
     */
    public static String decrypt(RsaKey rsaKey, String encrypted)
            throws IOException {
        BigInteger encrypt = new BigInteger(
                new String(new Base64().decode(encrypted)));
        return bigInteger2String(decrypt(rsaKey, encrypt));
    }

    /**
     * Encrypt big integer.
     *
     * @param rsaKey    the rsa key
     * @param plaintext the plaintext
     * @return the big integer
     */
    public static BigInteger encrypt(RsaKey rsaKey, BigInteger plaintext) {
        // C = (plaintext^publicKey) * mod n
        return plaintext.modPow(rsaKey.getPublicKey()/* e */, rsaKey.getN());
    }

    /**
     * Encrypt string.
     *
     * @param rsaKey    the rsa key
     * @param plaintext the plaintext
     * @return the string
     */
    public static String encrypt(RsaKey rsaKey, String plaintext) {
        BigInteger plaintextNumber = string2BigInteger(plaintext);
        BigInteger encrypt = encrypt(rsaKey, plaintextNumber);
        return new String(new Base64().encode(encrypt.toString().getBytes()));
    }


    /**
     * Gets rsa key.
     *
     * @return the rsa key
     */
    public static RsaKey getRsaKey() {
        return new RsaKey();
    }

    /**
     * Big integer 2 string string.
     *
     * @param bigInteger the big integer
     * @return the string
     * @throws IOException the io exception
     */
    public static String bigInteger2String(BigInteger bigInteger) throws IOException {

        return new String(new Base64().decode(toStr(bigInteger)));
    }

    private static String base64Encode(String plaintext) {
        try {
            return new String(new Base64().encode(plaintext.getBytes(CHAR_SET)));
        } catch (UnsupportedEncodingException e) {
            return new String(new Base64().encode(plaintext.getBytes()));
        }
    }

    /**
     * String 2 big integer big integer.
     *
     * @param plaintext the plaintext
     * @return the big integer
     */
    public static BigInteger string2BigInteger(String plaintext) {

        String msg = base64Encode(plaintext);
        return toBigInteger(msg);
    }


    /**
     * Big integers 2 string string.
     *
     * @param bigIntegers the big integers
     * @return the string
     * @throws IOException the io exception
     */
    public static String bigIntegers2String(BigInteger[] bigIntegers) throws IOException {

        StringBuilder plaintext = new StringBuilder(BLOCK_SIZE + 1);

        for (int i = 0; i < bigIntegers.length; i++) {
            plaintext.append(toStr(bigIntegers[i]));
        }

        return new String(new Base64().decode(plaintext.toString()));
    }

    private static String toStr(BigInteger number) throws IOException {

        StringBuilder plaintext = new StringBuilder(BLOCK_SIZE + 1);

        String plaintextNumber = number.toString();

        plaintextNumber = plaintextNumber.substring(1, plaintextNumber.length());
        for (int i = 0; i < plaintextNumber.length(); i += 3) {
            String blockString = plaintextNumber.substring(i, i + 3);
            int block = Integer.parseInt(blockString);
            plaintext.append((char) block);
        }

        return plaintext.toString();
    }

    /**
     * String 2 big integers big integer [ ].
     *
     * @param plaintext the plaintext
     * @return the big integer [ ]
     */
    public static BigInteger[] string2BigIntegers(String plaintext) {

        String base64 = base64Encode(plaintext);
        int length = (int) Math.ceil(base64.length() * 1.0d / BLOCK_SIZE);

        BigInteger[] bigIntegers = new BigInteger[length];
        String text = null;
        for (int i = 0; i < length - 1; i++) {
            int start = i * BLOCK_SIZE;
            text = base64.substring(start, start + BLOCK_SIZE);
            bigIntegers[i] = toBigInteger(text);
        }
        if (length == 1) {
            bigIntegers[0] = toBigInteger(base64);
        }
        if (length > 1) {
            int start = (length - 1) * BLOCK_SIZE;
            text = base64.substring(start, base64.length());
            bigIntegers[length - 1] = toBigInteger(text);
        }

        return bigIntegers;

    }

    private static BigInteger toBigInteger(String text) {
        StringBuilder numberString = new StringBuilder(BLOCK_SIZE + 1);
        numberString.append("1");

        for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            int asc = (int) c;

            if (String.valueOf(asc).length() <= 2) {
                numberString.append("0");
            }

            numberString.append(asc);
        }

        return new BigInteger(numberString.toString());
    }


    /**
     * Encrypt 2 string.
     *
     * @param rsaKey    the rsa key
     * @param plaintext the plaintext
     * @return the string
     */
    public static String encrypt2(RsaKey rsaKey, String plaintext) {

        BigInteger[] plaintextBigIntegers = string2BigIntegers(plaintext);
        StringBuilder sb = new StringBuilder(BLOCK_SIZE + 1);
        for (BigInteger bigInteger : plaintextBigIntegers) {
            BigInteger encrypt = encrypt(rsaKey, bigInteger);
            sb.append(encrypt.toString()).append("$");
        }
        sb.delete(sb.length() - 1, sb.length());

        return new String(new Base64().encode(sb.toString().getBytes()));
    }

    /**
     * Decrypt 2 string.
     *
     * @param rsaKey    the rsa key
     * @param encrypted the encrypted
     * @return the string
     * @throws IOException the io exception
     */
    public static String decrypt2(RsaKey rsaKey, String encrypted)
            throws IOException {

        String text = new String(new Base64().decode(encrypted));
        String[] texts = text.split("\\$");
        BigInteger[] bigIntegers = new BigInteger[texts.length];
        BigInteger bigInteger = null;
        for (int i = 0; i < bigIntegers.length; i++) {
            bigInteger = new BigInteger(texts[i]);
            bigIntegers[i] = decrypt(rsaKey, bigInteger);
        }

        return bigIntegers2String(bigIntegers);
    }

    /**
     * The type Rsa key.
     */
    public static class RsaKey implements Serializable {

        /**
         * The constant DEF_E.
         */
        public static final BigInteger DEF_E =  new BigInteger("65537");

        private int bits = 1024;

        /**
         * @Fields coeff : coefficient ; (inverse of q) mod p
         */
        transient private BigInteger coeff;

        /**
         * @Fields d : private exponent; d = (publicKey^-1) * mod(phi)
         */
        transient private BigInteger d;

        /**
         * @Fields dmp1 : dmp1 = d mod (p-1)
         */
        transient private BigInteger dmp1;

        /**
         * @Fields dmq1 : dmq1 = d mod (q-1)
         */
        transient private BigInteger dmq1;

        /**
         * @Fields e : public exponent; e = common prime = 2^16 + 1 = 65537
         */
        transient private BigInteger e; //65537

        /**
         * @Fields iqmp : q^-1 mod p
         */
        transient private BigInteger iqmp;

        /**
         * @Fields n : public modulus; n = p*q
         */
        private BigInteger n;

        /**
         * @Fields p : secret prime factor; generating a big prime number (bits)
         */
        transient private BigInteger p;

        /**
         * @Fields phi : phi = (p -1)*(q - 1)
         */
        transient private BigInteger phi;

        private BigInteger privateKey;

        private BigInteger publicKey;

        /**
         * @Fields q : secret prime factor; generating a big prime number (bits)
         */
        private BigInteger q;

        private SecureRandom random;

        /**
         * Instantiates a new Rsa key.
         */
        public RsaKey() {
            this.random = getSecureRandom();
            // generating a big prime number (bits)
            this.p = BigInteger.probablePrime(this.bits, this.random);
            // generating a big prime number (bits)
            this.q = BigInteger.probablePrime(this.bits, this.random);
            init(this.p, this.q);
        }

        /**
         * Instantiates a new Rsa key.
         *
         * @param p the p
         * @param q the q
         */
        public RsaKey(BigInteger p, BigInteger q) {

            init(p, q);
        }

        /**
         * Instantiates a new Rsa key.
         *
         * @param n the n
         * @param e the e
         * @param d the d
         */
        public RsaKey(BigInteger n, BigInteger e, BigInteger d) {
            this.n = n;
            this.e = e;
            this.publicKey = e;
            this.d = d;
            this.privateKey = d;
        }

        /**
         * Init.
         *
         * @param p the p
         * @param q the q
         */
        public void init(BigInteger p, BigInteger q) {

            this.p = p;
            this.q = q;
            // phi = (p -1)*(q - 1)
            this.phi = (p.subtract(BigInteger.ONE)).multiply(q
                    .subtract(BigInteger.ONE));
            // n = p*q
            this.n = p.multiply(q);
            // e = common prime = 2^16 + 1
            this.publicKey = DEF_E;
            // d = (publicKey^-1) * mod(phi)
            this.privateKey = this.publicKey.modInverse(this.phi);
            // e = common prime = 2^16 + 1
            this.e = this.publicKey;
            // d = (publicKey^-1) * mod(phi)
            this.d = this.privateKey;
            // dmp1 = d mod (p-1)
            this.dmp1 = this.d.mod(this.p.subtract(BigInteger.ONE));
            // dmq1 = d mod (q-1)
            this.dmq1 = this.d.mod(this.q.subtract(BigInteger.ONE));
            // iqmp = q^-1 mod p
            this.iqmp = this.q.modInverse(this.p);
            // coeff = q^-1 mod p
            this.coeff = this.iqmp;
        }

        /**
         * Gets bits.
         *
         * @return the bits
         */
        public int getBits() {
            return this.bits;
        }

        /**
         * Sets bits.
         *
         * @param bits the bits
         */
        public void setBits(int bits) {
            this.bits = bits;
        }

        /**
         * Gets coeff.
         *
         * @return the coeff
         */
        public BigInteger getCoeff() {
            return this.coeff;
        }

        /**
         * Sets coeff.
         *
         * @param coeff the coeff
         */
        public void setCoeff(BigInteger coeff) {
            this.coeff = coeff;
        }

        /**
         * Gets d.
         *
         * @return the d
         */
        public BigInteger getD() {
            return this.d;
        }

        /**
         * Sets d.
         *
         * @param d the d
         */
        public void setD(BigInteger d) {
            this.d = d;
        }

        /**
         * Gets dmp 1.
         *
         * @return the dmp 1
         */
        public BigInteger getDmp1() {
            return this.dmp1;
        }

        /**
         * Sets dmp 1.
         *
         * @param dmp1 the dmp 1
         */
        public void setDmp1(BigInteger dmp1) {
            this.dmp1 = dmp1;
        }

        /**
         * Gets dmq 1.
         *
         * @return the dmq 1
         */
        public BigInteger getDmq1() {
            return this.dmq1;
        }

        /**
         * Sets dmq 1.
         *
         * @param dmq1 the dmq 1
         */
        public void setDmq1(BigInteger dmq1) {
            this.dmq1 = dmq1;
        }

        /**
         * Gets e.
         *
         * @return the e
         */
        public BigInteger getE() {
            return this.e;
        }

        /**
         * Sets e.
         *
         * @param e the e
         */
        public void setE(BigInteger e) {
            this.e = e;
            this.privateKey = e;
        }

        /**
         * Gets iqmp.
         *
         * @return the iqmp
         */
        public BigInteger getIqmp() {
            return this.iqmp;
        }

        /**
         * Sets iqmp.
         *
         * @param iqmp the iqmp
         */
        public void setIqmp(BigInteger iqmp) {
            this.iqmp = iqmp;
        }

        /**
         * Gets n.
         *
         * @return the n
         */
        public BigInteger getN() {
            return this.n;
        }

        /**
         * Sets n.
         *
         * @param n the n
         */
        public void setN(BigInteger n) {
            this.n = n;
        }

        /**
         * Gets p.
         *
         * @return the p
         */
        public BigInteger getP() {
            return this.p;
        }

        /**
         * Sets p.
         *
         * @param p the p
         */
        public void setP(BigInteger p) {
            this.p = p;
        }

        /**
         * Gets phi.
         *
         * @return the phi
         */
        public BigInteger getPhi() {
            return this.phi;
        }

        /**
         * Sets phi.
         *
         * @param phi the phi
         */
        public void setPhi(BigInteger phi) {
            this.phi = phi;
        }

        /**
         * Gets private key.
         *
         * @return the private key
         */
        public BigInteger getPrivateKey() {
            return this.privateKey;
        }

        /**
         * Sets private key.
         *
         * @param privateKey the private key
         */
        public void setPrivateKey(BigInteger privateKey) {
            this.privateKey = privateKey;
            this.d = privateKey;
        }

        /**
         * Gets public key.
         *
         * @return the public key
         */
        public BigInteger getPublicKey() {
            return this.publicKey;
        }

        /**
         * Sets public key.
         *
         * @param publicKey the public key
         */
        public void setPublicKey(BigInteger publicKey) {
            this.publicKey = publicKey;
            this.e = publicKey;
        }

        /**
         * Gets q.
         *
         * @return the q
         */
        public BigInteger getQ() {
            return this.q;
        }

        /**
         * Sets q.
         *
         * @param q the q
         */
        public void setQ(BigInteger q) {
            this.q = q;
        }

        /**
         * Gets random.
         *
         * @return the random
         */
        public SecureRandom getRandom() {
            return this.random;
        }

        /**
         * Sets random.
         *
         * @param random the random
         */
        public void setRandom(SecureRandom random) {
            this.random = random;
        }

        /**
         * Gets secure random.
         *
         * @return the secure random
         */
        public SecureRandom getSecureRandom() {
            return new SecureRandom("forsrc@163.com".getBytes());
        }


    }
}