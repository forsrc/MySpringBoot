package com.forsrc.boot.solr.user.repository.impl;


import com.forsrc.boot.solr.base.repository.impl.BaseSolrImpl;
import com.forsrc.boot.solr.user.repository.UserSolr;
import com.forsrc.pojo.User;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserSolrImpl extends BaseSolrImpl<User, Long> implements UserSolr {

    @Override
    public List<User> findByUsername(final String username, final Pageable pageable) throws Exception {
        final List<User> list = new ArrayList<User>();

        SolrDocumentList solrDocumentList = findByQuery(String.format("username:%s", username), pageable);
        return exec(new SolrHandler<List<User>>() {
            @Override
            public List<User> handle(HttpSolrClient httpSolrClient) throws IOException, SolrServerException {
                SolrQuery solrQuery = new SolrQuery();
                solrQuery.setQuery(String.format("username:%s", username));
                solrQuery.setStart(pageable.getOffset());
                solrQuery.setRows(pageable.getPageSize());
                QueryResponse response = httpSolrClient.query(solrQuery);
                return response.getBeans(User.class);
//                SolrDocumentList solrDocumentList = response.getResults();
//                for (SolrDocument sd : solrDocumentList) {
//                    System.out.println("id：" + sd.getFieldValue("id"));
//                    System.out.println("username：" + sd.getFieldValue("username"));
//                    User user = new User();
//                    user.setId(Long.parseLong(sd.getFieldValue("id").toString()));
//                    List<String> lst = (List)sd.getFieldValue("username");
//                    user.setUsername(lst.get(0));
//                    list.add(user);
//                }
            }
        });
    }

    @Override
    public Page<User> findByEmail(String value, Pageable page) {
        return null;
    }


    @Override
    public String getSolrName() {
        return "/user";
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }

}
