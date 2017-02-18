package com.forsrc.utils;

import org.apache.tomcat.jdbc.pool.PoolProperties;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcUtils {

    private static final ThreadLocal<DataSource> THREADLOCAL_DATASOURCE = new ThreadLocal<DataSource>();
    private static final ThreadLocal<Connection> THREADLOCAL_CONNECTION = new ThreadLocal<Connection>();

    public static List<Map<String, Object>> list(final Connection connection,
                                                 final String sql, final Object... parameters) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                preparedStatement.setObject(i, parameters[i]);
            }
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 1; i <= columnCount; i++) {
                    map.put(resultSetMetaData.getColumnName(i),
                            resultSet.getObject(i));
                }
                list.add(map);
            }
        } finally {
            close(resultSet);
            close(preparedStatement);
        }
        return list;
    }

    public static void call(final Connection connection, final String sql,
                            final Object[] parameters, final HandlerResultSet handlerResultSet)
            throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                preparedStatement.setObject(i + 1, parameters[i]);
            }
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();
        handlerResultSet.handle(resultSetMetaData);
        try {
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    if (!handlerResultSet.handle(resultSet.getRow(),
                            resultSetMetaData.getColumnName(i),
                            resultSet.getObject(i))) {
                        break;
                    }
                }
            }
        } finally {
            close(resultSet);
            close(preparedStatement);
        }
    }

    public static void close(AutoCloseable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (Exception e) {
        }
    }

    public static DataSource getDataSource(String driverName, String url, String user,
                                           String password) {
        PoolProperties p = new PoolProperties();
        p.setUrl(url);
        p.setDriverClassName(driverName);
        p.setUsername(user);
        p.setPassword(password);
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(100);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        org.apache.tomcat.jdbc.pool.DataSource datasource = new org.apache.tomcat.jdbc.pool.DataSource();
        datasource.setPoolProperties(p);
        return datasource;
    }

    public static DataSource getDataSource(PoolProperties poolProperties) {
        org.apache.tomcat.jdbc.pool.DataSource datasource = new org.apache.tomcat.jdbc.pool.DataSource();
        datasource.setPoolProperties(poolProperties);
        return datasource;
    }

    public final Connection getConnection() throws SQLException {
        Connection connection = THREADLOCAL_CONNECTION.get();
        if (connection == null || connection.isClosed()) {
            synchronized (JdbcUtils.class) {
                if (connection == null) {
                    connection = THREADLOCAL_DATASOURCE.get().getConnection();
                    THREADLOCAL_CONNECTION.set(connection);
                }
            }
        }
        return connection;
    }

    public final Connection getConnection(String url, String user,
                                          String password) throws SQLException {

        Connection connection = THREADLOCAL_CONNECTION.get();
        if (connection == null || connection.isClosed()) {
            synchronized (JdbcUtils.class) {
                if (connection == null) {
                    connection = DriverManager.getConnection(url, user,
                            password);
                    THREADLOCAL_CONNECTION.set(connection);
                }
            }
        }
        return connection;
    }

    public JdbcUtils setDataSource(DataSource dataSource) {
        THREADLOCAL_DATASOURCE.set(dataSource);
        return this;
    }

    public void close() throws SQLException {
        Connection connection = THREADLOCAL_CONNECTION.get();
        if (connection != null && !connection.isClosed()) {
            synchronized (JdbcUtils.class) {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    THREADLOCAL_CONNECTION.remove();
                }
            }
        }
    }

    public JdbcUtils call(HandlerConnection handler) throws SQLException {
        Connection connection = getConnection();
        handler.handle(connection);
        return this;
    }

    public List<Map<String, Object>> list(final String sql, Object... parameters)
            throws SQLException {
        Connection connection = getConnection();

        return list(connection, sql, parameters);
    }

    public JdbcUtils call(final String sql, final Object[] parameters,
                          final HandlerResultSet handlerResultSet) throws SQLException {
        call(getConnection(), sql, parameters, handlerResultSet);
        return this;
    }

    public interface HandlerResultSet {
        void handle(ResultSetMetaData resultSetMetaData)
                throws SQLException;

        boolean handle(int row, String columnName, Object value)
                throws SQLException;
    }

    public interface HandlerConnection {
        void handle(Connection connection) throws SQLException;
    }

}
