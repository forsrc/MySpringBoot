package com.forsrc.core.web.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.forsrc.MySpringBootApplication;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = MySpringBootApplication.class)
@WebAppConfiguration()
@PropertySource("classpath*:application.properties")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testGet() {
        assertNotNull(userService);
        assertThat(userService.get(1L).getUsername(), is("admin"));
    }
}
