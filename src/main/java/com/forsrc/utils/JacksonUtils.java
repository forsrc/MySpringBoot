package com.forsrc.utils;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The type Jackson utils.
 */
public class JacksonUtils {

    /**
     * The constant DATE_FORMAT_DEF.
     */
    public static String DATE_FORMAT_DEF = "yyyy-MM-dd HH:mm:ss";

    /**
     * To json string.
     *
     * @param <E> the type parameter
     * @param e   the e
     * @return the string
     * @throws IOException the io exception
     */
    public static <E> String toJson(E e) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT_DEF));
        StringWriter stringWriter = new StringWriter();
        objectMapper.writeValue(stringWriter, e);
        return stringWriter.toString();
    }

    /**
     * To bean e.
     *
     * @param <E>   the type parameter
     * @param clazz the clazz
     * @param json  the json
     * @return the e
     * @throws IOException the io exception
     */
    public static <E> E toBean(Class<E> clazz, String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT_DEF));
        objectMapper.getDeserializationConfig().with(new SimpleDateFormat(DATE_FORMAT_DEF));
        StringWriter stringWriter = new StringWriter();
        return objectMapper.readValue(json, clazz);
    }

    /**
     * As list list.
     *
     * @param <E>   the type parameter
     * @param clazz the clazz
     * @param json  the json
     * @return the list
     * @throws IOException the io exception
     */
    public static <E> List<E> asList(Class<E> clazz, String json) throws IOException {

        List<Map<String, Object>> list = toList(clazz, json);
        Iterator<Map<String, Object>> it = list.iterator();
        List<E> lst = new ArrayList<E>();

        MyBeanUtils.dateConvert(DATE_FORMAT_DEF);
        try {
            while (it.hasNext()) {
                Map<String, Object> map = it.next();
                E e = MyBeanUtils.toBean(clazz, map);
                lst.add(e);
            }
        } catch (Exception ee) {
            throw new IOException(ee);
        }
        return lst;
    }

    /**
     * To list list.
     *
     * @param <E>   the type parameter
     * @param clazz the clazz
     * @param json  the json
     * @return the list
     * @throws IOException the io exception
     */
    public static <E> List<Map<String, Object>> toList(Class<E> clazz, String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT_DEF));
        objectMapper.getDeserializationConfig().with(new SimpleDateFormat(DATE_FORMAT_DEF));
        JavaType javaType = objectMapper.getTypeFactory().constructType(List.class, clazz);
        List<Map<String, Object>> list = objectMapper.readValue(json, javaType);

        return list;
    }

    /**
     * As map map.
     *
     * @param <K>  the type parameter
     * @param <V>  the type parameter
     * @param k    the k
     * @param v    the v
     * @param json the json
     * @return the map
     * @throws IOException the io exception
     */
    public static <K, V> Map<K, V> asMap(Class<K> k, Class<V> v, String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT_DEF));
        objectMapper.getDeserializationConfig().with(new SimpleDateFormat(DATE_FORMAT_DEF));
        JavaType javaType = objectMapper.getTypeFactory().constructMapType(Map.class, k, v);
        return objectMapper.readValue(json, javaType);
    }

}
