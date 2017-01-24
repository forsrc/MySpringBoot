package com.forsrc.utils;


import org.hibernate.SessionFactory;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.hibernate.persister.walking.spi.AttributeDefinition;
import org.hibernate.type.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.orm.hibernate5.SessionFactoryUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type My hibernate utils.
 */
public class MyHibernateUtils {

    /**
     * The constant PREPARED_STATEMENT_SET_METHOD_MAP.
     */
    public static final Map<Class<?>, Method> PREPARED_STATEMENT_SET_METHOD_MAP = getPreparedStatementSetMethodMap();

    /**
     * The constant PATTERN.
     */
    public static final Pattern PATTERN = Pattern.compile("[:#$](\\w+)");
    /**
     * The constant PATTERN_COLON.
     */
    public static final Pattern PATTERN_COLON = Pattern.compile(":(\\w+)");
    /**
     * The constant PATTERN_POUND.
     */
    public static final Pattern PATTERN_POUND = Pattern.compile("#(\\w+)");
    /**
     * The constant PATTERN_$.
     */
    public static final Pattern PATTERN_$ = Pattern.compile("$(\\w+)");

    /**
     * Format sql string.
     *
     * @param sql the sql
     * @return the string
     */
    public static String formatSql(final String sql) {
        return sql.replaceAll(PATTERN.pattern(), "?");
    }

    /**
     * Gets prepared statement.
     *
     * @param sessionFactory the session factory
     * @param sql            the sql
     * @return the prepared statement
     * @throws SQLException the sql exception
     */
    public static PreparedStatement getPreparedStatement(SessionFactory sessionFactory, final String sql) throws SQLException {
        DataSource ds = SessionFactoryUtils.getDataSource(sessionFactory);
        Connection connection = ds.getConnection();
        return connection.prepareStatement(sql);
    }

    /**
     * Add batch.
     *
     * @param <E>                the type parameter
     * @param ps                 the ps
     * @param sqlPropertyColumns the sql property columns
     * @param e                  the e
     * @throws SQLException              the sql exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     */
    public static <E> void addBatch(PreparedStatement ps, List<PropertyColumn> sqlPropertyColumns, E e) throws SQLException, InvocationTargetException, IllegalAccessException {

        Method method = null;
        int i = 1;
        for (PropertyColumn propertyColumn : sqlPropertyColumns) {
            method = BeanUtils.getPropertyDescriptor(e.getClass(), propertyColumn.getPropertyName()).getReadMethod();
            Class returnedClass = propertyColumn.getType().getReturnedClass();
            Object value = method.invoke(e, (Object[]) null);
            //ps.setObject(i++, value);
            method = PREPARED_STATEMENT_SET_METHOD_MAP.get(returnedClass);
            method.invoke(ps, i++, value);
            //System.out.println(obj);
        }
        ps.addBatch();
    }

    /**
     * Gets sql property column list.
     *
     * @param sessionFactory the session factory
     * @param cls            the cls
     * @param sql            the sql
     * @return the sql property column list
     * @throws IOException the io exception
     */
    public static List<PropertyColumn> getSqlPropertyColumnList(SessionFactory sessionFactory, final Class<?> cls, final String sql) throws IOException {

        final List<String> propertyColumnNameList = getPropertyNames(sql, PATTERN);
        final List<PropertyColumn> list = new ArrayList<PropertyColumn>(propertyColumnNameList.size());
        handle(sessionFactory, cls,
                new Handler() {
                    @Override
                    public boolean handle(String propertyName, String propertyColumnName, Type type) throws IOException {
                        if (propertyColumnNameList.contains(propertyColumnName)) {
                            list.add(propertyColumnNameList.indexOf(propertyColumnName), new PropertyColumn(cls, propertyName, propertyColumnName, type));
                        } else if (propertyColumnNameList.contains(propertyName)) {
                            list.add(propertyColumnNameList.indexOf(propertyName), new PropertyColumn(cls, propertyName, propertyColumnName, type));
                        }
                        return true;
                    }
                });
        return list;
    }

    /**
     * Gets property names.
     *
     * @param sql     the sql
     * @param pattern the pattern
     * @return the property names
     */
    public static List<String> getPropertyNames(final String sql, Pattern pattern) {
        Matcher matcher = pattern.matcher(sql);
        List<String> list = new ArrayList<String>();
        while (matcher.find()) {
            list.add(matcher.group(1));
        }
        return list;
    }

    /**
     * Handle.
     *
     * @param sessionFactory the session factory
     * @param cls            the cls
     * @param handler        the handler
     * @throws IOException the io exception
     */
    public static void handle(SessionFactory sessionFactory, Class<?> cls, Handler handler) throws IOException {
        SingleTableEntityPersister singleTableEntityPersister = (SingleTableEntityPersister)
                (sessionFactory.getAllClassMetadata().get(cls.getName()));

        if (singleTableEntityPersister == null) {
            return;
        }
        Iterator<AttributeDefinition> iterator = singleTableEntityPersister.getAttributes().iterator();
        int index = 0;
        while (iterator.hasNext()) {
            AttributeDefinition attributeDefinition = iterator.next();
            String propertyName = attributeDefinition.getName();
            String propertyColumnName = singleTableEntityPersister.getPropertyColumnNames(propertyName)[0];
            if (!handler.handle(propertyName, propertyColumnName, attributeDefinition.getType())) {
                break;
            }
            index++;
        }
    }

    /**
     * The interface Handler.
     */
    public static interface Handler {
        /**
         * Handle boolean.
         *
         * @param propertyName       the property name
         * @param propertyColumnName the property column name
         * @param type               the type
         * @return the boolean
         * @throws IOException the io exception
         */
        public boolean handle(String propertyName, String propertyColumnName, Type type) throws IOException;
    }

    /**
     * Gets property names.
     *
     * @param sessionFactory the session factory
     * @param cls            the cls
     * @return the property names
     * @throws IOException the io exception
     */
    public static Map<String, PropertyColumn> getPropertyNames(SessionFactory sessionFactory, final Class<?> cls) throws IOException {
        final Map<String, PropertyColumn> map = new HashMap<String, PropertyColumn>();
        handle(sessionFactory, cls, new Handler() {
            @Override
            public boolean handle(String propertyName, String propertyColumnName, Type type) throws IOException {
                map.put(propertyName, new PropertyColumn(cls, propertyName, propertyColumnName, type));
                return true;
            }
        });
        return map;
    }

    /**
     * Gets property column names.
     *
     * @param sessionFactory the session factory
     * @param cls            the cls
     * @return the property column names
     * @throws IOException the io exception
     */
    public static Map<String, PropertyColumn> getPropertyColumnNames(SessionFactory sessionFactory, final Class<?> cls) throws IOException {

        final Map<String, PropertyColumn> map = new HashMap<String, PropertyColumn>();
        handle(sessionFactory, cls, new Handler() {
            @Override
            public boolean handle(String propertyName, String propertyColumnName, Type type) throws IOException {
                map.put(propertyColumnName, new PropertyColumn(cls, propertyName, propertyColumnName, type));
                return true;
            }
        });
        return map;
    }

    /**
     * The type Property column.
     */
    public static class PropertyColumn {
        private Class<?> cls;
        private String propertyName;
        private String propertyColumnName;
        private Type type;

        /**
         * Instantiates a new Property column.
         *
         * @param cls the cls
         */
        public PropertyColumn(Class<?> cls) {
            this.cls = cls;
        }

        /**
         * Instantiates a new Property column.
         *
         * @param cls                the cls
         * @param propertyName       the property name
         * @param propertyColumnName the property column name
         * @param type               the type
         */
        public PropertyColumn(Class<?> cls, String propertyName, String propertyColumnName, Type type) {
            this.cls = cls;
            this.propertyName = propertyName;
            this.propertyColumnName = propertyColumnName;
            this.type = type;
        }

        /**
         * Gets cls.
         *
         * @return the cls
         */
        public Class<?> getCls() {
            return cls;
        }

        /**
         * Sets cls.
         *
         * @param cls the cls
         */
        public void setCls(Class<?> cls) {
            this.cls = cls;
        }

        /**
         * Gets property name.
         *
         * @return the property name
         */
        public String getPropertyName() {
            return propertyName;
        }

        /**
         * Sets property name.
         *
         * @param propertyName the property name
         */
        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        /**
         * Gets property column name.
         *
         * @return the property column name
         */
        public String getPropertyColumnName() {
            return propertyColumnName;
        }

        /**
         * Sets property column name.
         *
         * @param propertyColumnName the property column name
         */
        public void setPropertyColumnName(String propertyColumnName) {
            this.propertyColumnName = propertyColumnName;
        }

        /**
         * Gets type.
         *
         * @return the type
         */
        public Type getType() {
            return type;
        }

        /**
         * Sets type.
         *
         * @param type the type
         */
        public void setType(Type type) {
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass()) return false;

            PropertyColumn that = (PropertyColumn) o;

            if (!this.cls.equals(that.cls)) return false;
            if (!this.propertyName.equals(that.propertyName)) return false;
            return propertyColumnName.equals(that.propertyColumnName);

        }

        @Override
        public int hashCode() {
            int result = this.cls.hashCode();
            result = 31 * result + this.propertyName.hashCode();
            result = 31 * result + this.propertyColumnName.hashCode();
            return result;
        }
    }

    /**
     * Gets prepared statement set method map.
     *
     * @return the prepared statement set method map
     */
    public static Map<Class<?>, Method> getPreparedStatementSetMethodMap() {
        Map<Class<?>, Method> map = new HashMap<Class<?>, Method>();

        try {
//setLong(int parameterIndex, long x)
            map.put(Long.class, PreparedStatement.class.getMethod("setLong", int.class, long.class));
            map.put(int.class, PreparedStatement.class.getMethod("setLong", int.class, long.class));
            //setString(int parameterIndex, String x)
            map.put(String.class, PreparedStatement.class.getMethod("setString", int.class, String.class));
            //setObject(int parameterIndex, Object x)
            map.put(Object.class, PreparedStatement.class.getMethod("setObject", int.class, Object.class));
            //setArray (int parameterIndex, Array x)
            map.put(Array.class, PreparedStatement.class.getMethod("setArray", int.class, Array.class));
            //setAsciiStream(int parameterIndex, java.io.InputStream x)
            //map.put(InputStream.class, PreparedStatement.class.getMethod("setAsciiStream", Integer.class, InputStream.class));
            //setBigDecimal(int parameterIndex, BigDecimal x)
            map.put(BigDecimal.class, PreparedStatement.class.getMethod("setBigDecimal", int.class, BigDecimal.class));
            //setBinaryStream(int parameterIndex, java.io.InputStream x)
            map.put(InputStream.class, PreparedStatement.class.getMethod("setBinaryStream", int.class, InputStream.class));
            //setBlob (int parameterIndex, Blob x)
            map.put(Blob.class, PreparedStatement.class.getMethod("setBlob", int.class, Blob.class));
            //setBoolean(int parameterIndex, boolean x)
            map.put(Boolean.class, PreparedStatement.class.getMethod("setBoolean", int.class, boolean.class));
            map.put(boolean.class, PreparedStatement.class.getMethod("setBoolean", int.class, boolean.class));
            //setByte(int parameterIndex, byte x)
            map.put(Byte.class, PreparedStatement.class.getMethod("setByte", int.class, byte.class));
            map.put(byte.class, PreparedStatement.class.getMethod("setByte", int.class, byte.class));
            //setBytes(int parameterIndex, byte x[])
            map.put(Byte[].class, PreparedStatement.class.getMethod("setBytes", int.class, byte[].class));
            map.put(byte[].class, PreparedStatement.class.getMethod("setBytes", int.class, byte[].class));
            //setCharacterStream(int parameterIndex,java.io.Reader reader)
            map.put(Reader.class, PreparedStatement.class.getMethod("setCharacterStream", int.class, Reader.class));
            //setClob (int parameterIndex, Clob x)
            map.put(Clob.class, PreparedStatement.class.getMethod("setClob", int.class, Clob.class));
            //setDate(int parameterIndex, java.sql.Date x)
            map.put(java.sql.Date.class, PreparedStatement.class.getMethod("setDate", int.class, java.sql.Date.class));
            //setDouble(int parameterIndex, double x)
            map.put(Double.class, PreparedStatement.class.getMethod("setDouble", int.class, double.class));
            map.put(double.class, PreparedStatement.class.getMethod("setDouble", int.class, double.class));
            //setFloat(int parameterIndex, float x)
            map.put(Float.class, PreparedStatement.class.getMethod("setFloat", int.class, float.class));
            map.put(float.class, PreparedStatement.class.getMethod("setFloat", int.class, float.class));
            //setInt(int parameterIndex, int x)
            map.put(Integer.class, PreparedStatement.class.getMethod("setInt", int.class, int.class));
            map.put(int.class, PreparedStatement.class.getMethod("setInt", int.class, int.class));
            //void setNull(int parameterIndex, int sqlType) throws SQLException;  Types
            map.put(Types.class, PreparedStatement.class.getMethod("setNull", int.class, int.class));
            //setNClob(int parameterIndex, NClob value)
            map.put(NClob.class, PreparedStatement.class.getMethod("setNClob", int.class, NClob.class));
            //setRef (int parameterIndex, Ref x)
            map.put(Ref.class, PreparedStatement.class.getMethod("setRef", int.class, Ref.class));
            //void setRowId(int parameterIndex, RowId x)
            map.put(RowId.class, PreparedStatement.class.getMethod("setRowId", int.class, RowId.class));
            //setShort(int parameterIndex, short x)
            map.put(Short.class, PreparedStatement.class.getMethod("setShort", int.class, short.class));
            map.put(short.class, PreparedStatement.class.getMethod("setShort", int.class, short.class));
            //setSQLXML(int parameterIndex, SQLXML xmlObject)
            map.put(SQLXML.class, PreparedStatement.class.getMethod("setSQLXML", int.class, SQLXML.class));
            //setTime(int parameterIndex, java.sql.Time x)
            map.put(Time.class, PreparedStatement.class.getMethod("setTime", int.class, Time.class));
            //setTimestamp(int parameterIndex, java.sql.Timestamp x)
            map.put(Timestamp.class, PreparedStatement.class.getMethod("setTimestamp", int.class, Timestamp.class));
            //setURL(int parameterIndex, java.net.URL x)
            map.put(URL.class, PreparedStatement.class.getMethod("setURL", int.class, URL.class));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

}
