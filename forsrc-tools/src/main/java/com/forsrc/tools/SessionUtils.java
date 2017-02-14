package com.forsrc.tools;


import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * The type Session utils.
 */
public class SessionUtils {

    /**
     * Gets session.
     *
     * @return the session
     */
    public static HttpSession getSession() {
        return getHttpServletRequest().getSession();
    }

    /**
     * Gets httpServletRequest.
     *
     * @return the httpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    /**
     * Get t.
     *
     * @param <T> the type parameter
     * @param key the key
     * @return the t
     */
    public static <T> T get(String key) {
        HttpSession session = getSession();
        return get(session, key);
    }

    /**
     * Get t.
     *
     * @param <T>     the type parameter
     * @param session the session
     * @param key     the key
     * @return the t
     */
    public static <T> T get(HttpSession session, String key) {
        Object obj = session.getAttribute(key);
        if (obj == null) {
            return null;
        }
        return (T) obj;
    }

}
