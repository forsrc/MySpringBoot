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

package com.forsrc.utils;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;

//import org.hibernate.service.ServiceRegistryBuilder;

/**
 * The type Export db.
 */
public final class ExportDb {

    private ExportDb() {
    }

    /**
     * Init.
     */
    public static void init() {
        Configuration cfg = new Configuration().configure();
        String isInit = cfg.getProperty("isInitDb");
        if (isInit == null || !"true".equals(isInit)) {
            return;
        }
        //4.x
        //SchemaExport export = new SchemaExport(cfg);
        //export.create(true, false);
        //ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
        //.applySettings(cfg.getProperties()).buildServiceRegistry();
        //SessionFactory sessionFactory = cfg.buildSessionFactory(serviceRegistry);
        ServiceRegistry serviceRegistry = null;
        try {
            //5.x
            serviceRegistry = new StandardServiceRegistryBuilder().configure().build();
            MetadataImplementor metadata = (MetadataImplementor) new MetadataSources(serviceRegistry).buildMetadata();
            SchemaExport export = new SchemaExport(serviceRegistry, metadata);
            export.create(true, false);
            StandardServiceRegistryBuilder.destroy(serviceRegistry);
        } catch (Exception e) {
            e.printStackTrace();
            //LogUtils.LOGGER.error(e.getMessage(), e);
        } finally {
            if (serviceRegistry != null) {
                StandardServiceRegistryBuilder.destroy(serviceRegistry);
                serviceRegistry = null;
            }
        }
        Session session = null;
        try {
            //ServiceRegistry serviceRegistry =  new StandardServiceRegistryBuilder().configure().build();
            //SessionFactory sessionFactory = new MetadataSources(serviceRegistry).buildMetadata()
            //.buildSessionFactory();
            //SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
            serviceRegistry = new StandardServiceRegistryBuilder().configure().build();
            SessionFactory sessionFactory = new MetadataSources(serviceRegistry).buildMetadata()
                    .buildSessionFactory();
            session = sessionFactory.openSession();

            executeNamedQuery(session, "sql_user_insert_admin");
            executeNamedQuery(session, "sql_book_category_insert_init");
            executeNamedQuery(session, "sql_book_insert_init");
        } catch (Exception e) {
            e.printStackTrace();
            //LogUtils.LOGGER.error(e.getMessage(), e);
        } finally {
            if (serviceRegistry != null) {
                StandardServiceRegistryBuilder.destroy(serviceRegistry);
                serviceRegistry = null;
            }
        }
    }

    private static void executeNamedQuery(Session session, String queryName) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            transaction.begin();
            Query query = session.getNamedQuery(queryName);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            //session.close();
            //LogUtils.LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

        init();
    }
}
