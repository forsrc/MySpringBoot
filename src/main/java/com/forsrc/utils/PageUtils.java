package com.forsrc.utils;

/**
 * The type Page utils.
 */
public final class PageUtils {

    /**
     * The constant SIZE.
     */
    public static final int SIZE = 10;

    private PageUtils() {
    }

    /**
     * Gets total page.
     *
     * @param size  the size
     * @param total the total
     * @return the total page
     */
    public static int getTotalPage(final int size, final long total) {

        double s = size <= 0 ? SIZE : size;
        double t = total <= 0 ? 1 : total;
        double page = Math.ceil(t / s);
        return (int) page;
    }
}
