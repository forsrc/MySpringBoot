package com.forsrc.boot.web.user.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.SSLConfig;
import com.jayway.restassured.response.Response;

public class UserControllerTest {

    public static void main(String[] args) {
        final String accessToken = obtainAccessToken("forsrc", "forsrc", "forsrc");
        System.out.println(accessToken);
        // @formatter:off
        final Response response = RestAssured.given()
                .given()
                .config(RestAssured.config()
                        .sslConfig(new SSLConfig().allowAllHostnames()))
                .relaxedHTTPSValidation()
                .auth()
                .preemptive()
                .basic("forsrc", "forsrc")
                .and()
                .header("Authorization", "Bearer " + accessToken)
                .get("https://localhost:8075/user/1");
        // @formatter:on
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.jsonPath().get("reason"));
        System.out.println(response.jsonPath().getString(""));
    }

    private static String obtainAccessToken(String clientId, String username, String password) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "password");
        params.put("client_id", clientId);
        params.put("username", username);
        params.put("password", password);
        // @formatter:off

        final Response response = RestAssured
                .given()
                .config(RestAssured.config()
                  .sslConfig(new SSLConfig()
                          .allowAllHostnames()))
                .relaxedHTTPSValidation()
                .auth()
                .preemptive()
                .basic("forsrc", "forsrc")
                .and()
                .with()
                .params(params)
                .when()
                .post("https://localhost:8075/oauth/token");

        // @formatter:on
        return response.jsonPath().getString("access_token");

    }
}
