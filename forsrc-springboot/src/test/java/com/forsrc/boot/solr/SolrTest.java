package com.forsrc.boot.solr;

import com.forsrc.boot.solr.user.repository.UserSolr;
import com.forsrc.boot.solr.user.service.UserSolrService;
import com.forsrc.boot.web.user.service.UserService;
import com.forsrc.pojo.User;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SolrTest {
    @Autowired
    private UserSolrService userSolrService;
    @Test
    public void test() throws IOException, SolrServerException {
        User user = new User();
        user.setId(10L);
        user.setUsername("u 10");
        user.setEmail("xxx");
        System.out.println("---> SolrTest");
        //userSolrService.save(user);
    }
}
