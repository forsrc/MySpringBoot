package com.forsrc.utils;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
//import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Configures and provides access to Hibernate sessions, tied to the current
 * thread of execution. Follows the Thread Local Session pattern, see
 * {@link http://hibernate.org/42.html }.
 */
public final class HibernateSessionFactory {

    private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
    /**
     * Location of hibernate.cfg.xml file. Location should be on the classpath
     * as Hibernate uses #resourceAsStream style lookup for its configuration
     * file. The default classpath location of the hibernate config file is in
     * the default package. Use #setConfigFile() to update the location of the
     * configuration file for the current session.
     */
    private static String CONFIG_FILE_LOCATION = "/hibernate.cfg.xml";
    private static String configFile = CONFIG_FILE_LOCATION;
    private static Configuration configuration = new Configuration();
    private static org.hibernate.SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;

    static {
        try {
            configuration.configure(configFile);
            serviceRegistry = new StandardServiceRegistryBuilder().configure(configFile).build();
            sessionFactory = new MetadataSources(serviceRegistry).buildMetadata()
                    .buildSessionFactory();
            //sessionFactory = configuration.buildSessionFactory();
            //ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
            //.applySettings(configuration.getProperties()).buildServiceRegistry();
            //sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception e) {
            //System.err.println("%%%% Error Creating SessionFactory %%%%");
            LogUtils.LOGGER.error(e.getMessage(), e);
        }
    }

    private HibernateSessionFactory() {
    }

    /**
     * Close the single hibernate session instance.
     *
     * @throws HibernateException the hibernate exception
     */
    public static void closeSession() throws HibernateException {
        Session session = threadLocal.get();
        threadLocal.set(null);

        if (session != null) {
            session.close();
        }
    }

    /**
     * return hibernate configuration
     *
     * @return the configuration
     */
    public static Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Returns the ThreadLocal Session instance. Lazy initialize the
     * <code>SessionFactory</code> if needed.
     *
     * @return Session session
     * @throws HibernateException the hibernate exception
     */
    public static Session getSession() throws HibernateException {
        Session session = threadLocal.get();

        if (session == null || !session.isOpen()) {
            if (sessionFactory == null) {
                rebuildSessionFactory();
            }
            session = (sessionFactory != null) ? sessionFactory.openSession()
                    : null;
            threadLocal.set(session);
        }

        return session;
    }

    /**
     * return session factory
     *
     * @return the session factory
     */
    public static org.hibernate.SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Rebuild hibernate session factory
     */
    public static void rebuildSessionFactory() {
        try {
            configuration.configure(configFile);
            StandardServiceRegistryBuilder.destroy(serviceRegistry);
            serviceRegistry = new StandardServiceRegistryBuilder().configure(configFile).build();
            sessionFactory = new MetadataSources(serviceRegistry).buildMetadata()
                    .buildSessionFactory();
            //sessionFactory = configuration.buildSessionFactory();
            //ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
            //.applySettings(configuration.getProperties()).buildServiceRegistry();
            //sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception e) {
            //System.err.println("%%%% Error Creating SessionFactory %%%%");
            LogUtils.LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * return session factory
     * <p/>
     * session factory will be rebuilded in the next call
     *
     * @param configFile the config file
     */
    public static void setConfigFile(String configFile) {
        HibernateSessionFactory.configFile = configFile;
        sessionFactory = null;
    }

}