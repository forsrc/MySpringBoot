package com.forsrc.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Random;

/**
 * The type My string utils.
 */
public class MyStringUtils {
    /**
     * Gets multi language.
     *
     * @param string the string
     * @param src    the src
     * @param des    the des
     * @return the multi language
     */
    public static String getMultiLanguage(String string, Charset src,
                                          Charset des) {
        if (src.name().equals(des.name())) {
            return string;
        }
        String str = null;
        try {
            str = new String(string.getBytes(src.name()), des.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str == null ? string : str;
    }

    /**
     * Gets last line break offset.
     *
     * @param txt       the txt
     * @param lineBreak the line break
     * @return the last line break offset
     */
    public static int getLastLineBreakOffset(String txt, String lineBreak) {
        int offset = -1;
        byte[] txtBytes = txt.getBytes();
        byte[] lineBreakBytes = lineBreak.getBytes();
        for (int i = txtBytes.length - 1; i >= 0; i = i - lineBreak.length()) {
            int matchs = 0;
            for (int j = lineBreakBytes.length - 1; j >= 0; j--) {
                if (lineBreakBytes[j] == txtBytes[i + j + 1
                        - lineBreakBytes.length]) {
                    matchs++;
                }
            }
            if (matchs == lineBreak.length()) {
                offset = i - lineBreak.length() + 1;
                break;
            }
        }
        return offset;
    }

    /**
     * Generate letter string.
     *
     * @param length the length
     * @return the string
     */
    public static String generateLetter(int length) {
        Random random = new Random(length);
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int ch = random.nextInt(27) + (random.nextBoolean() ? 'A' : 's');
            char c = (char) ch;
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Generate string.
     *
     * @param length the length
     * @return the string
     */
    public static String generate(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length + 1);
        for (int i = 0; i < length; i++) {
            if (random.nextBoolean()) {
                sb.append(random.nextInt(10));
                continue;
            }
            int ch = random.nextInt(26) + (random.nextBoolean() ? 'A' : 'a');
            char c = (char) ch;
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Generate number string.
     *
     * @param length the length
     * @return the string
     */
    public static String generateNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();

    }

    /**
     * formatStr("{0} + {1} = {2}", 1, 2, 3) --> "1 + 2 = 3"
     *
     * @param format format: "{0} + {1} = {2}"
     * @param args   args
     * @return formatted string
     */
    public static String formatStr(final String format, final Object... args) {
        if (format == null) {
            return format;
        }
        String formatted = format;
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                continue;
            }
            formatted = formatted.replace("{" + i + "}", args[i].toString());
        }
        return formatted;
    }

    /**
     * Returns the formatted string: format("{0} + {1} = {2}", 1, 2, 3) --> "1 + 2 = 3"
     *
     * @param format format: "{0} + {1} = {2}"
     * @param args   args
     * @return the formatted string
     */
    public static String format(final String format, final Object... args) {
        if (format == null) {
            return format;
        }
        StringBuilder formatted = new StringBuilder(format.length() * 2);
        char ch = ' ';
        int indexOf = -1;
        String number = null;
        int n = 0;
        for (int i = 0; i < format.length(); i++) {
            ch = format.charAt(i);
            if (ch == '{') {
                indexOf = format.indexOf("}", i + 1);
                if (indexOf < 0) {
                    formatted.append(format.substring(i));
                    break;
                }
                number = format.substring(i + 1, indexOf);
                if (!isNumber(number)) {
                    formatted.append(format.charAt(i));
                    continue;
                }
                i = i + number.length() + 1;
                n = Integer.parseInt(number);
                formatted.append(n >= args.length || n < 0 ? "{" + n + "}" : args[n]);
                continue;
            }
            formatted.append(format.charAt(i));
        }

        return formatted.toString();
    }

    /**
     * Returns <tt>true</tt> if this string is number.
     *
     * @param number {String}
     * @return true | false; <tt>true</tt> if this string is number.
     */
    public static boolean isNumber(final String number) {
        if (number == null || number.length() == 0) {
            return false;
        }
        int ch = 0;
        for (int i = 0; i < number.length(); i++) {
            ch = number.charAt(i);
            if (ch < 48 || ch > 57) {
                return false;
            }
        }
        return true;
    }

    /**
     * Is blank boolean.
     *
     * @param str the str
     * @return the boolean
     */
    public static boolean isBlank(String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; ++i) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Is not blank boolean.
     *
     * @param str the str
     * @return the boolean
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
}
