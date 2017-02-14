package com.forsrc.boot.solr.user.controller;

import com.forsrc.boot.solr.user.service.UserSolrService;
import com.forsrc.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

//@Controller
@RestController
@RequestMapping("/solr/user")
public class UserSolrController {


    private UserSolrService service;

    @RequestMapping(value = "", method = {RequestMethod.GET})
    @ResponseBody
    public List<User> get(String username) throws Exception {
        Pageable pageable = new PageRequest(0, 10);
        try {
            List<User> list = service.findByUsername(username, pageable);
            //System.out.println("---> " + ((Object[])(list.get(0)))[0]);
            return list;
        } catch (Exception e) {
            throw new Exception(MessageFormat.format("Exception --> {0} : {1}", new Date().toString(), e.getMessage()));
        }
    }

    /**
     * Gets service.
     *
     * @return Value of service.
     */
    public UserSolrService getService() {
        return service;
    }

    /**
     * Sets new service.
     *
     * @param service New value of service.
     */
    @Autowired
    public void setService(UserSolrService service) {
        this.service = service;
    }
}
