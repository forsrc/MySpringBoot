/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.forsrc.tools;

import com.forsrc.utils.AesUtils;
import com.forsrc.utils.AesUtils.AesException;
import org.apache.log4j.Logger;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

//import org.apache.commons.dbcp.BasicDataSource;

/**
 * The type Encryption local session factory bean.
 */
public class EncryptionLocalContainerEntityManagerFactoryBean extends LocalContainerEntityManagerFactoryBean {

    private static final Logger LOGGER = Logger.getLogger(EncryptionLocalContainerEntityManagerFactoryBean.class);

    @Override
    public void setDataSource(DataSource dataSource) {

        String password = null;

//        if (dataSource instanceof BasicDataSource) {
//            password = ((BasicDataSource) dataSource).getPassword();
//        }

        if (dataSource instanceof com.atomikos.jdbc.AtomikosDataSourceBean) {
            password = ((com.atomikos.jdbc.AtomikosDataSourceBean) dataSource).getXaProperties().getProperty("password");
        }

        if (dataSource instanceof com.atomikos.jdbc.nonxa.AtomikosNonXADataSourceBean) {
            password = ((com.atomikos.jdbc.nonxa.AtomikosNonXADataSourceBean) dataSource).getPassword();
        }


        String decryPassword = null;
        try {
            decryPassword = AesUtils.getInstance().decrypt(password);
        } catch (AesException e) {
            decryPassword = password;
        }

//        if (dataSource instanceof BasicDataSource) {
//            ((BasicDataSource) dataSource).setPassword(decryPassword);
//        }

        if (dataSource instanceof com.atomikos.jdbc.AtomikosDataSourceBean) {
            ((com.atomikos.jdbc.AtomikosDataSourceBean) dataSource).getXaProperties().setProperty("password", decryPassword);
        }

        if (dataSource instanceof com.atomikos.jdbc.nonxa.AtomikosNonXADataSourceBean) {
            ((com.atomikos.jdbc.nonxa.AtomikosNonXADataSourceBean) dataSource).setPassword(decryPassword);
        }
        super.setDataSource(dataSource);
    }

}
