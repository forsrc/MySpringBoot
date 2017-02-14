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
package com.forsrc.constant;

/**
 * The enum Key constants.
 */
public enum KeyConstants {
    /**
     * User key constants.
     */
    USER("User"),
    /**
     * Code key constants.
     */
    CODE("code"),
    /**
     * Key pair key constants.
     */
    KEY_PAIR("KeyPair"),
    /**
     * Login time key constants.
     */
    LOGIN_TIME("loginTime"),
    /**
     * Rsa key key constants.
     */
    RSA_KEY("RsaKey"),
    /**
     * Aes key key constants.
     */
    AES_KEY("AesKey"),
    /**
     * Des key key constants.
     */
    DES_KEY("DesKey"),
    /**
     * Username key constants.
     */
    USERNAME("username"),
    /**
     * Language key constants.
     */
    LANGUAGE("language"),
    /**
     * Ready key constants.
     */
    READY("ready"),
    /**
     * Token key constants.
     */
    TOKEN("Token");

    private String key;

    KeyConstants(String key) {

        this.key = key;

    }

    /**
     * Gets key.
     *
     * @return the key
     */
    public String getKey() {

        return key;
    }


    @Override
    public String toString() {

        return this.key;
    }
}
