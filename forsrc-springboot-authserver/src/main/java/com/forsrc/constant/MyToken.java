package com.forsrc.constant;


import com.forsrc.utils.MyAesUtils;
import com.forsrc.utils.MyDesUtils;
import com.forsrc.utils.MyRsaUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * The type My token.
 */
public class MyToken implements Serializable {
    private String loginToken;
    private long loginTokenTime = -1;
    private String token;
    private long tokenTime = -1;
    private MyAesUtils.AesKey aesKey;
    private MyDesUtils.DesKey desKey;
    private MyRsaUtils.RsaKey rsaKey4Client;
    private MyRsaUtils.RsaKey rsaKey4Server;

    /**
     * Instantiates a new My token.
     */
    public MyToken(){
        generate();
    }

    /**
     * Generate.
     */
    public void generate(){
        this.token = UUID.randomUUID().toString();
        this.tokenTime = new Date().getTime();
        this.aesKey = new MyAesUtils.AesKey();
        this.desKey = new MyDesUtils.DesKey();
        //this.rsaKey4Client = new MyRsaUtils.RsaKey();
        this.rsaKey4Server = new MyRsaUtils.RsaKey();
        this.loginToken = UUID.randomUUID().toString();
        this.loginTokenTime = new Date().getTime();
    }

    /**
     * Gets login token.
     *
     * @return the login token
     */
    public String getLoginToken() {
        return loginToken;
    }

    /**
     * Gets login token time.
     *
     * @return the login token time
     */
    public long getLoginTokenTime() {
        return loginTokenTime;
    }

    /**
     * Gets token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Gets token time.
     *
     * @return the token time
     */
    public long getTokenTime() {
        return tokenTime;
    }

    /**
     * Gets aes key.
     *
     * @return the aes key
     */
    public MyAesUtils.AesKey getAesKey() {
        return aesKey;
    }

    /**
     * Gets des key.
     *
     * @return the des key
     */
    public MyDesUtils.DesKey getDesKey() {
        return desKey;
    }

    /**
     * Gets rsa key 4 server.
     *
     * @return the rsa key 4 server
     */
    public MyRsaUtils.RsaKey getRsaKey4Server() {
        return rsaKey4Server;
    }

    /**
     * Sets rsa key 4 server.
     *
     * @param rsaKey4Server the rsa key 4 server
     */
    public void setRsaKey4Server(MyRsaUtils.RsaKey rsaKey4Server) {
        this.rsaKey4Server = rsaKey4Server;
    }

    /**
     * Gets rsa key 4 client.
     *
     * @return the rsa key 4 client
     */
    public MyRsaUtils.RsaKey getRsaKey4Client() {
        return rsaKey4Client;
    }

    /**
     * Sets rsa key 4 client.
     *
     * @param rsaKey4Client the rsa key 4 client
     */
    public void setRsaKey4Client(MyRsaUtils.RsaKey rsaKey4Client) {
        this.rsaKey4Client = rsaKey4Client;
    }
}
