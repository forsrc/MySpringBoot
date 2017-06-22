package com.forsrc.boot.web.user.controller;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forsrc.pojo.User;

@RunWith(SpringRunner.class)
// @PropertySource("classpath*:application.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = { "security.basic.enabled = false",
        "management.security.enabled = false", "management.health.db.enabled = false", "security.require-ssl = false",
        "server.ssl.enabled = false", "local.server.port = 0" })
@AutoConfigureMockMvc(secure = false)
// @WithMockUser(username = "forsrc", password = "forsrc", roles = { "ADMIN",
// "USER" })
public class UserControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    // @Value("${local.server.port}")
    private int port;

    @Before
    public void setup() {
        // given(this.userService.get(1L)).willReturn(new User(1L));

    }

    @Test
    // @WithMockUser(username = "forsrc", password = "forsrc", roles = {
    // "ADMIN", "USER" })
    public void userTest() {
        System.out.println(port);
        ResponseEntity<User> user = this.testRestTemplate.withBasicAuth("forsrc", "forsrc").getForEntity("/users/1",
                User.class);

        System.out.println(user);
    }

    @Test
    // @WithMockUser(username = "forsrc", password = "forsrc", roles = {
    // "ADMIN", "USER" })
    public void accessTokenTest() throws Exception {
        String accessToken = obtainAccessToken("forsrc", "forsrc", "forsrc");
        assertNotNull(accessToken);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> token = mapper.readValue(accessToken, Map.class);

        final Map<String, Long> params = new HashMap<>();
        params.put("id", 1L);

        System.out.println(token.get("access_token"));

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        HttpHeaders headers = testRestTemplate.withBasicAuth("forsrc", "forsrc").headForHeaders("/user/1");
        headers.add("Authorization", "Bearer " + token.get("access_token"));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        Map user = testRestTemplate.exchange("/user/1", HttpMethod.GET, request, Map.class).getBody();
        // assertNotNull(user);
        System.out.println("-->" + user);
    }

    private String obtainAccessToken(String clientId, String username, String password) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "password");
        params.put("client_id", clientId);
        params.put("username", username);
        params.put("password", password);
        // @formatter:off

        String accessToken = testRestTemplate.withBasicAuth("forsrc", "forsrc")
                .postForObject("/oauth/token"
                        + "?grant_type={grant_type}"
                        + "&client_id={client_id}"
                        + "&username={username}"
                        + "&password={password}", null, String.class, params);
        System.out.println(accessToken);
        return accessToken;

//        final Response response = RestAssured
//                .given()
//                .config(RestAssured.config()
//                  .sslConfig(new SSLConfig()
//                          .allowAllHostnames()))
//                .relaxedHTTPSValidation()
//                .auth()
//                .preemptive()
//                .basic("forsrc", "forsrc")
//                .and()
//                .with()
//                .params(params)
//                .when()
//                .post(String.format("https://localhost:%s/oauth/token", port));
//
//        // @formatter:on
        // System.out.println(response.jsonPath().getString(""));
        // return response.jsonPath().getString("access_token");

    }
}
