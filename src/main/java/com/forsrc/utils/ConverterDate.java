package com.forsrc.utils;


import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.util.Date;

/**
 * The type Converter date.
 */
public class ConverterDate implements Converter<String, Date> {

    @Override
    public Date convert(String timeStr) {

        try {
            return DateTimeUtils.parse(timeStr);
        } catch (ParseException e) {
            e.getMessage();
            return null;
        }
    }

}

