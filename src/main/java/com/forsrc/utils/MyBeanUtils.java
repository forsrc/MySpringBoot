package com.forsrc.utils;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The type My bean utils.
 */
public class MyBeanUtils {

    /**
     * The constant REF_GET_ID.
     */
    public static final String REF_GET_ID = "getId";

    /**
     * The constant FORMAT.
     */
    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * The constant FORMAT_DATE.
     */
    public static final String FORMAT_DATE = "yyyy-MM-dd";


    /**
     * Gets params.
     *
     * @param request the request
     * @return the params
     */
    public static Map<String, Object> getParams(final HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (request.getMethod().equalsIgnoreCase("POST")) {
            map.putAll(request.getParameterMap());
            //return map;
        }
        String queryString = request.getQueryString();
        if (MyStringUtils.isBlank(queryString)) {
            removeBlank(map);
            return map;
        }
        String[] params = queryString.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length != 2) {
                continue;
            }
            if (p[1].indexOf(",") >= 0) {
                String[] objs = p[1].split(",");
                map.put(p[0], decoder(objs));
                continue;
            }
            map.put(p[0], decoder(p[1]));
        }
        removeBlank(map);
        return map;
    }

    /**
     * Remove blank.
     *
     * @param map the map
     */
    public static void removeBlank(Map<String, Object> map) {
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value != null && value instanceof String[]
                    && (((String[]) value).length) == 1 && "".equals(((String[]) value)[0])) {
                it.remove();
            }
        }
    }

    /**
     * Gets params.
     *
     * @param request the request
     * @param prefix  the prefix
     * @return the params
     */
    public static Map<String, Object> getParams(final HttpServletRequest request, final String prefix) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (request.getMethod().equalsIgnoreCase("POST")) {
            Map<String, Object> parameterMap = getParamsMap(request.getParameterMap(), prefix);
            map.putAll(parameterMap);
            //return map;
        }
        String queryString = request.getQueryString();
        if (MyStringUtils.isBlank(queryString)) {
            return map;
        }
        String[] params = queryString.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length != 2) {
                continue;
            }
            p[0] = p[0].trim();
            if (!p[0].toLowerCase().startsWith(prefix.trim().toLowerCase())) {
                continue;
            }
            if (p[0].length() <= prefix.trim().length() + 1) {
                continue;
            }
            p[0] = p[0].trim().substring(prefix.trim().length() + 1);

            if (p[1].indexOf(",") >= 0) {
                String[] objs = p[1].split(",");
                map.put(p[0], decoder(objs));
                continue;
            }

            map.put(p[0], decoder(p[1]));
        }
        return map;
    }

    /**
     * Gets params map.
     *
     * @param map    the map
     * @param prefix the prefix
     * @return the params map
     */
    public static Map<String, Object> getParamsMap(final Map<String, String[]> map, final String prefix) {
        Map<String, Object> params = new HashMap<String, Object>();
        Iterator<Map.Entry<String, String[]>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String[]> entry = it.next();
            if (!entry.getKey().trim().toLowerCase().startsWith(prefix.trim().toLowerCase())) {
                continue;
            }
            if (entry.getKey().trim().length() <= prefix.trim().length() + 1) {
                continue;
            }
            params.put(entry.getKey().trim().substring(prefix.trim().length() + 1), entry.getValue());
        }
        return params;
    }

    /**
     * Gets params map obj.
     *
     * @param map    the map
     * @param prefix the prefix
     * @return the params map obj
     */
    public static Map<String, Object> getParamsMapObj(final Map<String, Object> map, final String prefix) {
        Map<String, Object> params = new HashMap<String, Object>();
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            if (!entry.getKey().trim().toLowerCase().startsWith(prefix.trim().toLowerCase())) {
                continue;
            }
            if (entry.getKey().trim().length() <= prefix.trim().length() + 1) {
                continue;
            }
            params.put(entry.getKey().trim().substring(prefix.trim().length() + 1), entry.getValue());
        }
        return params;
    }

    private static String[] decoder(String[] objs) {
        String[] strs = new String[objs.length];
        for (int i = 0; i < objs.length; i++) {
            strs[i] = decoder(objs[i]);
        }
        return strs;
    }

    private static String decoder(final String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }


    /**
     * Gets bean.
     *
     * @param <ENTITY> the type parameter
     * @param clazz    the clazz
     * @param request  the request
     * @return the bean
     * @throws IllegalAccessException    the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     * @throws InstantiationException    the instantiation exception
     */
    public static <ENTITY> ENTITY getBean(final Class<ENTITY> clazz, final HttpServletRequest request)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {

        Map<String, Object> map = getParams(request);
        return getBean(clazz, map);
    }

    /**
     * Gets bean.
     *
     * @param <ENTITY> the type parameter
     * @param clazz    the clazz
     * @param request  the request
     * @param prefix   the prefix
     * @return the bean
     * @throws IllegalAccessException    the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     * @throws InstantiationException    the instantiation exception
     */
    public static <ENTITY> ENTITY getBean(final Class<ENTITY> clazz, final HttpServletRequest request, String prefix)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {

        Map<String, Object> map = getParams(request, prefix);
        return getBean(clazz, map);
    }

    /**
     * Gets bean.
     *
     * @param <ENTITY> the type parameter
     * @param clazz    the clazz
     * @param map      the map
     * @param prefix   the prefix
     * @return the bean
     * @throws IllegalAccessException    the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     * @throws InstantiationException    the instantiation exception
     */
    public static <ENTITY> ENTITY getBean(final Class<ENTITY> clazz, final Map<String, Object> map, String prefix)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {

        return getBean(clazz, getParamsMapObj(map, prefix));
    }

    /**
     * Gets bean.
     *
     * @param <ENTITY> the type parameter
     * @param clazz    the clazz
     * @param map      the map
     * @return the bean
     * @throws IllegalAccessException    the illegal access exception
     * @throws InstantiationException    the instantiation exception
     * @throws InvocationTargetException the invocation target exception
     */
    public static <ENTITY> ENTITY getBean(final Class<ENTITY> clazz, final Map<String, Object> map)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {
        dateConvert();
        ENTITY obj = clazz.newInstance();
        BeanUtils.populate(obj, map);
        /*Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            try {
                Field field = obj.getClass().getDeclaredField(entry.getKey());
                if (field == null) {
                    continue;
                }
            } catch (NoSuchFieldException e) {
                continue;
            }
            //BeanUtils.setProperty(obj, entry.getKey(),entry.getValue());
            *//*PropertyDescriptor pd = new PropertyDescriptor(entry.getKey(), clazz);
            Method methodSet = pd.getWriteMethod();
            methodSet.invoke(obj, entry.getValue());*//*
        }*/
        return (ENTITY) obj;
    }

    /**
     * To bean entity.
     *
     * @param <ENTITY> the type parameter
     * @param clazz    the clazz
     * @param map      the map
     * @return the entity
     * @throws IllegalAccessException    the illegal access exception
     * @throws InstantiationException    the instantiation exception
     * @throws InvocationTargetException the invocation target exception
     */
    public static <ENTITY> ENTITY toBean(final Class<ENTITY> clazz, final Map<String, ?> map)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {
        //ENTITY obj = (ENTITY)Class.forName(clazz.getName()).newInstance();
        ENTITY obj = clazz.newInstance();
        BeanUtils.populate(obj, map);
        return (ENTITY) obj;
    }

    /**
     * Date convert.
     */
    public synchronized static void dateConvert() {
        ConvertUtils.deregister(Date.class);
        ConvertUtils.register(new Converter() {
            public Object convert(Class type, Object value) {
                if (value instanceof String && type == Date.class) {
                    try {
                        return DateTimeUtils.parse(value.toString());
                    } catch (ParseException e) {
                        return value;
                    }
                }
                if (type == String.class && value instanceof Date) {
                    try {
                        return DateTimeUtils.format(FORMAT, (Date) value);
                    } catch (ParseException e) {
                        return value;
                    }
                }
                return value;
            }
        }, Date.class);
        ConvertUtils.deregister(Timestamp.class);
        ConvertUtils.register(new Converter() {
            public Object convert(Class type, Object value) {
                if (value instanceof String && type == Timestamp.class) {
                    try {
                        return new Timestamp(DateTimeUtils.parse(value.toString()).getTime());
                    } catch (ParseException e) {
                        return value;
                    }
                }
                if (type == String.class && value instanceof Timestamp) {
                    try {
                        return DateTimeUtils.format(FORMAT, (Timestamp) value);
                    } catch (ParseException e) {
                        return value;
                    }
                }
                return value;
            }
        }, Timestamp.class);

    }

    /**
     * Date convert.
     *
     * @param format the format
     */
    public synchronized static void dateConvert(final String format) {
        ConvertUtils.deregister(Date.class);
        ConvertUtils.register(new Converter() {
            DateFormat df = new SimpleDateFormat(format);

            public Object convert(Class type, Object value) {
                if (value instanceof String && type == Date.class) {
                    try {
                        return DateTimeUtils.parse(format, value.toString());
                    } catch (ParseException e) {
                        return value;
                    }
                }
                if (type == String.class && value instanceof Date) {
                    try {
                        return DateTimeUtils.format(format, (Date) value);
                    } catch (ParseException e) {
                        return value;
                    }
                }
                return value;
            }
        }, Date.class);
        ConvertUtils.deregister(Timestamp.class);
        ConvertUtils.register(new Converter() {
            DateFormat df = new SimpleDateFormat(format);

            public Object convert(Class type, Object value) {
                if (value instanceof String && type == Timestamp.class) {
                    try {
                        return new Timestamp(DateTimeUtils.parse(format, value.toString()).getTime());
                    } catch (ParseException e) {
                        return value;
                    }
                }
                if (type == String.class && value instanceof Timestamp) {
                    try {
                        return DateTimeUtils.format(format, (Timestamp) value);
                    } catch (ParseException e) {
                        return value;
                    }
                }
                return value;
            }
        }, Timestamp.class);

    }

    /**
     * To map map.
     *
     * @param obj        the obj
     * @param ignoreNull the ignore null
     * @return the map
     */
    public static Map<String, Object> toMap(Object obj, boolean ignoreNull) {
        final BeanWrapper src = new BeanWrapperImpl(obj);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Map<String, Object> map = new HashMap<String, Object>();
        for (PropertyDescriptor pd : pds) {
            Object value = src.getPropertyValue(pd.getName());
            if (ignoreNull && value == null) {
                continue;
            }
            map.put(pd.getName(), value);
        }

        return map;
    }

    /**
     * Copy properties.
     *
     * @param <E>        the type parameter
     * @param src        the src
     * @param target     the target
     * @param ignoreNull the ignore null
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     */
    public static <E> void copyProperties(final E src, final E target, boolean ignoreNull)
            throws InvocationTargetException, IllegalAccessException {
        dateConvert(FORMAT);
        BeanUtils.copyProperties(src, toMap(target, ignoreNull));
    }

    /**
     * Copy properties.
     *
     * @param <E>        the type parameter
     * @param clazz      the clazz
     * @param src        the src
     * @param target     the target
     * @param ignoreNull the ignore null
     * @throws IllegalAccessException    the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     * @throws InstantiationException    the instantiation exception
     */
    public static <E> void copyProperties(final Class<E> clazz, final E src, final Object target, boolean ignoreNull)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        dateConvert(FORMAT);
        E e = toBean(clazz, toMap(target, ignoreNull));
        BeanUtils.copyProperties(src, toMap(e, ignoreNull));
    }

    /**
     * Gets id.
     *
     * @param <PK> the type parameter
     * @param <E>  the type parameter
     * @param e    the e
     * @return the id
     * @throws Exception the exception
     */
    public static <PK, E> PK getId(E e) throws Exception {
        return (PK) e.getClass().getMethod(REF_GET_ID).invoke(e);
    }

    /**
     * Md 5 string.
     *
     * @param <E> the type parameter
     * @param e   the e
     * @return the string
     * @throws IOException the io exception
     */
    public static <E> String md5(E e) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(e);
        } catch (IOException ioe) {
            throw ioe;
        } finally {
            if (oos != null) {
                oos.close();
            }
        }
        return DigestUtils.md5Hex(baos.toByteArray());
    }
}
