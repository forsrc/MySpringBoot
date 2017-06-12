package com.forsrc.boot.web.user.controller;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration;
import org.springframework.security.oauth2.client.test.OAuth2ContextSetup;
import org.springframework.security.oauth2.client.test.RestTemplateHolder;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestOperations;

import com.forsrc.pojo.User;

@RunWith(SpringRunner.class)
// @PropertySource("classpath*:application.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = { "security.basic.enabled = false",
        "management.security.enabled = false", "management.health.db.enabled = false", "security.require-ssl = false",
        "server.ssl.enabled = false" })
@AutoConfigureMockMvc(secure = false)
//@WithMockUser(username = "forsrc", password = "forsrc", roles = { "ADMIN", "USER" })
public class UserControllerTest {



    @Autowired
    private TestRestTemplate testRestTemplate;

    @Value("${local.server.port}")
    private static int port;

    @Before
    public void setup() {
        // given(this.userService.get(1L)).willReturn(new User(1L));

    }

    @Test
    //@WithMockUser(username = "forsrc", password = "forsrc", roles = { "ADMIN", "USER" })
    public void userTest() {
        System.out.println(port);
        ResponseEntity<User> user = this.testRestTemplate.withBasicAuth("forsrc", "forsrc").getForEntity("/users/1",
                User.class);

        System.out.println(user);
    }

}
