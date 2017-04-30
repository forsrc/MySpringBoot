package com.forsrc.utils;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class RsautilsTest {

    @Test
    public void test() throws IOException {
        MyRsaUtils.RsaKey rsaKey = MyRsaUtils.getRsaKey();
        System.out.println(new String(new Base64().encode((rsaKey.getPrivateKey()).toByteArray())));
        System.out.println(new String(new Base64().encode((rsaKey.getPublicKey().toByteArray()))));

    }
}
