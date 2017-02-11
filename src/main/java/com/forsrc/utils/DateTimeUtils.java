package com.forsrc.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The type Date time utils.
 */
public class DateTimeUtils {


    /**
     * The constant FORMAT.
     */
    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * The constant FORMAT_DATE_TIME.
     */
    public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    /**
     * The constant FORMAT_DATE.
     */
    public static final String FORMAT_DATE = "yyyy-MM-dd";
    /**
     * The constant FORMAT_TIME.
     */
    public static final String FORMAT_TIME = "HH:mm:ss";

    /**
     * The constant SDF_FORMAT.
     */
    public static final SimpleDateFormat SDF_FORMAT = new SimpleDateFormat(FORMAT);
    /**
     * The constant SDF_FORMAT_DATE_TIME.
     */
    public static final SimpleDateFormat SDF_FORMAT_DATE_TIME = new SimpleDateFormat(FORMAT_DATE_TIME);
    /**
     * The constant SDF_FORMAT_DATE.
     */
    public static final SimpleDateFormat SDF_FORMAT_DATE = new SimpleDateFormat(FORMAT_DATE);
    /**
     * The constant SDF_FORMAT_TIME.
     */
    public static final SimpleDateFormat SDF_FORMAT_TIME = new SimpleDateFormat(FORMAT_TIME);

    /**
     * The constant PATTERN_YYYY_MM_DD_HH_MM_SS.
     */
    public static final Pattern PATTERN_YYYY_MM_DD_HH_MM_SS = Pattern.compile("^\\d{4}\\-\\d{2}\\-\\d{2} \\d{2}\\:\\d{2}\\:\\d{2}$");
    /**
     * The constant PATTERN_YYYY_MM_DD.
     */
    public static final Pattern PATTERN_YYYY_MM_DD = Pattern.compile("^\\d{4}\\-\\d{2}\\-\\d{2}$");
    /**
     * The constant PATTERN_HH_MM_SS.
     */
    public static final Pattern PATTERN_HH_MM_SS = Pattern.compile("^\\d{2}\\:\\d{2}\\:\\d{2}$");
    /**
     * The constant PATTERN_LONG.
     */
    public static final Pattern PATTERN_LONG = Pattern.compile("^\\d+$");

    /**
     * Gets date time.
     *
     * @param @return
     * @return String date time
     * @throws
     * @Title: getDateTime
     * @Description:
     */
    public static String getDateTime() {
        return SDF_FORMAT.format(new Date());
    }

    /**
     * Gets date time.
     *
     * @param format the format
     * @return String date time
     * @throws
     * @Title: getDateTime
     * @Description:
     */
    public static String getDateTime(String format) {
        // Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // return sdf.format(calendar.getTime());
        return sdf.format(new Date());
    }

    /**
     * Gets date time.
     *
     * @param date   the date
     * @param format the format
     * @return String date time
     * @throws
     * @Title: getDateTime
     * @Description:
     */
    public static String getDateTime(Date date, String format) {
        // Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // return sdf.format(calendar.getTime());
        return sdf.format(date);
    }

    /**
     * Gets date time.
     *
     * @param date the date
     * @return String date time
     * @throws
     * @Title: getDateTime
     * @Description:
     */
    public static String getDateTime(Date date) {
        // Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        // return sdf.format(calendar.getTime());
        return SDF_FORMAT.format(date);
    }

    /**
     * Gets date time.
     *
     * @param today  the today
     * @param index  the index
     * @param format the format
     * @return the date time
     */
    public static String getDateTime(Date today, int index, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_MONTH, index);
        Date d = calendar.getTime();
        return getDateTime(d, format);
    }

    /**
     * Gets week.
     *
     * @param today  the today
     * @param index  　0-6
     * @param format the format
     * @return String week
     * @throws
     * @Title: getWeek
     * @Description:
     */
    public static String getWeek(Date today, int index, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        ArrayList<Date> list = getWeekDateList(today, 0);
        Date d = calendar.getTime();
        for (Date date : list) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            if (c.get(Calendar.DAY_OF_WEEK) == index + 1) {
                d = c.getTime();
            }
        }

        return getDateTime(d, format);
    }

    /**
     * Gets date list.
     *
     * @param today the today
     * @param index the index
     * @return ArrayList<Date> date list
     * @throws
     * @Title: getDateList
     * @Description:
     */
    public static ArrayList<Date> getDateList(Date today, int index) {
        ArrayList<Date> list = new ArrayList<Date>();
        for (int i = 0; i <= (index < 0 ? 0 - index : index); i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.add(Calendar.DAY_OF_MONTH, index < 0 ? 0 - i : i);
            Date d = calendar.getTime();
            list.add(d);
        }
        if (list.size() == 0) {
            list.add(today);
        }

        return list;
    }

    /**
     * Gets date list reg.
     *
     * @param today  the today
     * @param index  the index
     * @param format the format
     * @return the date list reg
     */
    public static String getDateListReg(Date today, int index, String format) {
        return getDatesReg(getDateList(today, index), format);
    }

    /**
     * Gets week date list.
     *
     * @param today the today
     * @param index the index
     * @return ArrayList<Date> week date list
     * @throws
     * @Title: getWeekDateList
     * @Description:
     */
    public static ArrayList<Date> getWeekDateList(Date today, int index) {
        ArrayList<Date> list = new ArrayList<Date>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.WEEK_OF_YEAR, -1);
        }
        calendar.add(Calendar.WEEK_OF_YEAR, index);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        for (int i = 0; i <= 6; i++) {
            Calendar c = Calendar.getInstance();
            c.setTime(calendar.getTime());
            c.add(Calendar.DAY_OF_MONTH, i);
            Date d = c.getTime();
            list.add(d);
        }
        if (list.size() == 0) {
            list.add(today);
        }
        return list;
    }

    /**
     * Gets week date list reg.
     *
     * @param today  the today
     * @param index  the index
     * @param format the format
     * @return the week date list reg
     */
    public static String getWeekDateListReg(Date today, int index, String format) {
        ArrayList<Date> list = DateTimeUtils.getWeekDateList(today, index);
        return getDatesReg(list, format);
    }

    /**
     * Gets dates reg.
     *
     * @param list   the list
     * @param format the format
     * @return the dates reg
     */
    public static String getDatesReg(ArrayList<Date> list, String format) {
        Iterator<Date> it = list.iterator();
        StringBuffer sb = new StringBuffer();
        int i = 0;
        while (it.hasNext()) {
            Date d = it.next();
            if (i++ == 0) {
                sb.append("(");
            } else {
                sb.append("|(");
            }
            sb.append(DateTimeUtils.getDateTime(d, format)).append(")");
        }
        return sb.toString();
    }

    /**
     * Gets date time.
     *
     * @param currentTimeMillis the current time millis
     * @param format            the format
     * @return String date time
     * @throws
     * @Title: getDateTime
     * @Description:
     */
    public static String getDateTime(long currentTimeMillis, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(currentTimeMillis));
    }

    /**
     * String to date date.
     *
     * @param date   the date
     * @param format the format
     * @return the date
     */
    public static Date stringToDate(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets date time.
     *
     * @param currentTimeMillis the current time millis
     * @return String date time
     * @throws
     * @Title: getDateTime
     * @Description:
     */
    public static String getDateTime(long currentTimeMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        return sdf.format(new Date(currentTimeMillis));
    }

    /**
     * Gets date time.
     *
     * @param currentTimeMillis the current time millis
     * @param ms                the ms
     * @return String date time
     * @throws
     * @Title: getDateTime
     * @Description:
     */
    public static String getDateTime(long currentTimeMillis, boolean ms) {
        String format = ms ? "yyyy-MM-dd HH:mm:ss.SSS" : FORMAT;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(currentTimeMillis));
    }


    /**
     * Format string.
     *
     * @param format the format
     * @param date   the date
     * @return the string
     * @throws ParseException the parse exception
     */
    public static String format(String format, Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * Format string.
     *
     * @param date the date
     * @return the string
     * @throws ParseException the parse exception
     */
    public static String format(Date date) throws ParseException {
        return SDF_FORMAT.format(date);
    }

    /**
     * Parse date.
     *
     * @param format the format
     * @param date   the date
     * @return the date
     * @throws ParseException the parse exception
     */
    public static Date parse(String format, String date) throws ParseException {
        if (date == null) {
            return null;
        }
        //Pattern pattern = Pattern.compile("^\\d{4}\\-\\d{2}\\-\\d{2} \\d{2}\\:\\d{2}\\:\\d{2}$");
        //Matcher matcher = pattern.matcher(date);
        Matcher matcher = PATTERN_YYYY_MM_DD_HH_MM_SS.matcher(date);

        if (matcher.matches()) {
            return SDF_FORMAT.parse(date);
        }

        //pattern = Pattern.compile("^\\d{4}\\-\\d{2}\\-\\d{2}$");
        //matcher = pattern.matcher(date);
        matcher = PATTERN_YYYY_MM_DD.matcher(date);

        if (matcher.matches()) {
            return SDF_FORMAT_DATE.parse(date);
        }

        //pattern = Pattern.compile("^\\d{2}\\:\\d{2}\\:\\d{2}$");
        //matcher = pattern.matcher(date);
        matcher = PATTERN_HH_MM_SS.matcher(date);

        if (matcher.matches()) {
            return SDF_FORMAT_TIME.parse(date);
        }

        //pattern = Pattern.compile("^\\d+$");
        //matcher = pattern.matcher(date);
        matcher = PATTERN_LONG.matcher(date);
        Date d = null;
        if (matcher.matches()) {
            long time = Long.parseLong(date);
            d = new Date(time);
            return d;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(date);
    }

    /**
     * Parse date.
     *
     * @param date the date
     * @return the date
     * @throws ParseException the parse exception
     */
    public static Date parse(String date) throws ParseException {
        return parse(FORMAT, date);
    }
}
