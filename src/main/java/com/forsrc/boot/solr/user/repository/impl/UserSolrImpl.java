package com.forsrc.boot.solr.user.repository.impl;


import com.forsrc.boot.solr.base.repository.impl.BaseSolrImpl;
import com.forsrc.boot.solr.user.repository.UserSolr;
import com.forsrc.pojo.User;
import com.forsrc.utils.MyBeanUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserSolrImpl extends BaseSolrImpl<User, Long> implements UserSolr {

    @Override
    public List<User> findByUsername(String username, Pageable pageable) throws Exception {
        final List<User> list = new ArrayList<>();

        SolrDocumentList solrDocumentList = findByQuery("username:" + username, pageable);
        exec(new SolrHandler<Void>() {
            @Override
            public Void handle(HttpSolrClient httpSolrClient) throws IOException, SolrServerException, IllegalAccessException, InvocationTargetException, InstantiationException {
                SolrQuery solrQuery = new SolrQuery();
                solrQuery.setQuery("username:" + username);
                solrQuery.setStart(pageable.getOffset());
                solrQuery.setRows(pageable.getPageSize());
                QueryResponse response = httpSolrClient.query(solrQuery);
                SolrDocumentList solrDocumentList = response.getResults();
                for (SolrDocument sd : solrDocumentList) {
                    System.out.println("id：" + sd.getFieldValue("id"));
                    System.out.println("username：" + sd.getFieldValue("username"));
                    User user = new User();
                    user.setId(Long.parseLong(sd.getFieldValue("id").toString()));
                    List<String> lst = (List)sd.getFieldValue("username");
                    user.setUsername(lst.get(0));
                    list.add(user);
                }
                return null;
            }
        });
        return list;
    }

    @Override
    public Page<User> findByEmail(String value, Pageable page) {
        return null;
    }


    @Override
    public User save(User user) {

        return user;
    }

    @Override
    public String getSolrName() {
        return "/user";
    }

}
