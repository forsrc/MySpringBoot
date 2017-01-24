package com.forsrc.utils;


/**
 * The type Args utils.
 */
public class ArgsUtils {

    /**
     * Gets string.
     *
     * @param key   the key
     * @param index the index
     * @param args  the args
     * @return String string
     * @throws
     * @Title: getString
     * @Description:
     */
    public static String getString(String key, int index, String[] args) {

        return getString(key, index, args, null);
    }

    /**
     * Gets string.
     *
     * @param key         the key
     * @param index       the index
     * @param args        the args
     * @param def         the def
     * @param toLowerCase the to lower case
     * @return the string
     */
    public static String getString(String key, int index, String[] args,
                                   String def, boolean toLowerCase) {
        for (int i = 0; i < args.length; i++) {
            if ((toLowerCase ? args[i].toLowerCase().equals(key.toLowerCase())
                    : args[i].equals(key)) && i + index < args.length) {
                if (args[i + index].startsWith("%")
                        && args[i + index].endsWith("%")) {
                    String env = System.getenv(args[i + index].substring(1,
                            args[i + index].length() - 1));
                    return env == null ? args[i + index] : env;
                }
                return args[i + index];
            }
        }
        return def;
    }

    /**
     * Gets string.
     *
     * @param key   the key
     * @param index the index
     * @param args  the args
     * @param def   the def
     * @return the string
     */
    public static String getString(String key, int index, String[] args,
                                   String def) {

        return getString(key, index, args, def, true);
    }

    /**
     * Gets string.
     *
     * @param key  the key
     * @param args the args
     * @return the string
     */
    public static String getString(String key, String[] args) {
        return getString(key, 1, args);
    }

    /**
     * Gets string.
     *
     * @param key  the key
     * @param args the args
     * @param def  the def
     * @return the string
     */
    public static String getString(String key, String[] args, String def) {
        return getString(key, 1, args, def);
    }

    /**
     * Gets integer.
     *
     * @param key   the key
     * @param index the index
     * @param args  the args
     * @return the integer
     */
    public static int getInteger(String key, int index, String[] args) {

        return getInteger(key, index, args, -1);
    }

    /**
     * Gets integer.
     *
     * @param key  the key
     * @param args the args
     * @param def  the def
     * @return int integer
     * @throws
     * @Title: getInteger
     * @Description:
     */
    public static int getInteger(String key, String[] args, int def) {

        return getInteger(key, 1, args, def);
    }

    /**
     * Gets integer.
     *
     * @param key         the key
     * @param index       the index
     * @param args        the args
     * @param def         the def
     * @param toLowerCase the to lower case
     * @return the integer
     */
    public static int getInteger(String key, int index, String[] args, int def,
                                 boolean toLowerCase) {
        for (int i = 0; i < args.length; i++) {
            if ((toLowerCase ? args[i].toLowerCase().equals(key.toLowerCase())
                    : args[i].equals(key)) && i + index < args.length) {
                try {
                    return Integer.parseInt(args[i + index]);
                } catch (Exception e) {
                    return def;
                }

            }
        }
        return def;
    }

    /**
     * Gets integer.
     *
     * @param key   the key
     * @param index the index
     * @param args  the args
     * @param def   the def
     * @return the integer
     */
    public static int getInteger(String key, int index, String[] args, int def) {

        return getInteger(key, index, args, def, true);
    }

    /**
     * Gets integer.
     *
     * @param key  the key
     * @param args the args
     * @return the integer
     */
    public static int getInteger(String key, String[] args) {
        return getInteger(key, 1, args);
    }

    /**
     * Gets boolean.
     *
     * @param key  the key
     * @param args the args
     * @return the boolean
     */
    public static boolean getBoolean(String key, String[] args) {
        return getString(key, 0, args) != null;
    }

    /**
     * Gets boolean.
     *
     * @param key   the key
     * @param index the index
     * @param args  the args
     * @return the boolean
     */
    public static boolean getBoolean(String key, int index, String[] args) {
        return getString(key, index, args) != null;
    }

    /**
     * Gets boolean.
     *
     * @param key   the key
     * @param value the value
     * @param index the index
     * @param args  the args
     * @return the boolean
     */
    public static boolean getBoolean(String key, String value, int index,
                                     String[] args) {

        return getBoolean(key, value, index, args, true);
    }

    /**
     * Gets boolean.
     *
     * @param key         the key
     * @param value       the value
     * @param index       the index
     * @param args        the args
     * @param toLowerCase the to lower case
     * @return the boolean
     */
    public static boolean getBoolean(String key, String value, int index,
                                     String[] args, boolean toLowerCase) {
        for (int i = 0; i < args.length; i++) {
            if ((toLowerCase ? args[i].toLowerCase().equals(key.toLowerCase())
                    : args[i].equals(key)) && i + index < args.length) {
                return args[i + index].equals(value);
            }
        }
        return false;
    }

    /**
     * Gets boolean.
     *
     * @param key   the key
     * @param value the value
     * @param args  the args
     * @return boolean boolean
     * @throws
     * @Title: getBoolean
     * @Description:
     */
    public static boolean getBoolean(String key, String value, String[] args) {
        return getBoolean(key, value, 1, args);
    }

}
