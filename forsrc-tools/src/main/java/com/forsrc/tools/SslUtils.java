package com.forsrc.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class SslUtils {

    public SSLContext getSSLContext(String keystoreFile, String truststoreFile) throws Exception {
        String keystoreType = "JKS";
        InputStream keystoreLocation = new FileInputStream("src/main/resources/aaa.jks");
        char[] keystorePassword = "zzz".toCharArray();
        char[] keyPassword = "zzz".toCharArray();

        KeyStore keystore = KeyStore.getInstance(keystoreType);
        keystore.load(keystoreLocation, keystorePassword);
        KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmfactory.init(keystore, keyPassword);

        InputStream truststoreLocation = new FileInputStream("src/main/resources/aaa.jks");
        char[] truststorePassword = "zzz".toCharArray();
        String truststoreType = "JKS";

        KeyStore truststore = KeyStore.getInstance(truststoreType);
        truststore.load(truststoreLocation, truststorePassword);
        TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmfactory.init(truststore);

        KeyManager[] keymanagers = kmfactory.getKeyManagers();
        TrustManager[] trustmanagers = tmfactory.getTrustManagers();

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keymanagers, trustmanagers, new SecureRandom());
        SSLContext.setDefault(sslContext);
        return sslContext;
    }
}
