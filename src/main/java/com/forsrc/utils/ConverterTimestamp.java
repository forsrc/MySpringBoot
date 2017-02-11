package com.forsrc.utils;


import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;
import java.text.ParseException;

/**
 * The type Converter timestamp.
 */
public class ConverterTimestamp implements Converter<String, Timestamp> {

    @Override
    public Timestamp convert(String timeStr) {

        try {
            return new Timestamp(DateTimeUtils.parse(timeStr).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}