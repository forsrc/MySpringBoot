package com.forsrc.utils;


import org.springframework.context.MessageSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * The type Message utils.
 */
public class MessageUtils {
    /**
     * Sets message.
     *
     * @param request the request
     * @param key     the key
     * @param msg     the msg
     */
    public static void setMessage(HttpServletRequest request, String key, String msg) {
        request.setAttribute(key, msg);
    }

    /**
     * Gets text.
     *
     * @param messageSource the message source
     * @param key           the key
     * @param params        the params
     * @return the text
     */
    public static String getText(MessageSource messageSource, String key, Object[] params) {
        Locale locale = getLocale();
        return messageSource.getMessage(key, params, locale);
    }

    /**
     * Gets text.
     *
     * @param messageSource the message source
     * @param key           the key
     * @return the text
     */
    public static String getText(MessageSource messageSource, String key) {

        return messageSource.getMessage(key, null, getLocale());
    }

    /**
     * Gets locale.
     *
     * @return the locale
     */
    public static Locale getLocale() {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(httpServletRequest);

        if (localeResolver != null) {
            return localeResolver.resolveLocale(httpServletRequest);
        }

        RequestContext requestContext = new RequestContext(httpServletRequest);
        return requestContext.getLocale();
    }
}
