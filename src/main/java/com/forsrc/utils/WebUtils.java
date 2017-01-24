package com.forsrc.utils;


import com.forsrc.constant.KeyConstants;


//import com.opensymphony.xwork2.ActionContext;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.MessageFormat;
import java.util.*;

/**
 * The type Web utils.
 */
public class WebUtils {

    public static final String KEY_BASE_URL = "baseUrl";

    /**
     * The constant contentType.
     */
    public static final Map<String, String> contentType = new HashMap<String, String>(4) {
        {
            put(".js", "text/javascript");
            put(".css", "text/css");
            put(".less", "text/css");
            put(".scss", "text/css");
            put(".json", "text/json");
            put(".map", "text/json");
            put(".html", "text/html");
        }
    };

    /**
     * Gets ip.
     *
     * @param request the request
     * @return the ip
     */
    public static String getIp(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() > 0 && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (ip != null && ip.length() > 0 && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip != null && ip.length() > 0 && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getRemoteAddr();
        return ip;
    }

    /**
     * Sets content type.
     *
     * @param request  the request
     * @param response the response
     */
    public static void setContentType(HttpServletRequest request, HttpServletResponse response) {
        int index = request.getRequestURI().lastIndexOf(".");
        if (index < 0) {
            return;
        }
        String suffix = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("."));
        if (contentType.keySet().contains(suffix)) {
            response.setContentType(contentType.get(suffix));
        }

    }

    /**
     * Gets available language.
     *
     * @return the available language
     */
    public static List<String> getAvailableLanguage() {

        List<String> list = new ArrayList<String>();
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            list.add(locale.getLanguage() + "_" + locale.getCountry());
        }
        return list;
    }

    /**
     * Gets locale.
     *
     * @param language the language
     * @return the locale
     */
    public static Locale getLocale(String language) {

        if (language == null || language.indexOf("_") < 0) {
            return Locale.getDefault();
        }
        Locale locale = getAvailableLanguage().contains(language) ? new Locale(
                language.substring(0, language.indexOf("_")),
                language.substring(language.indexOf("_") + 1)) : Locale
                .getDefault();
        return locale;
    }

    /**
     * Sets language.
     *
     * @param request the request
     */
    public static void setLanguage(HttpServletRequest request) {

        String languageFromRequest = request.getParameter(KeyConstants.LANGUAGE.getKey());
        String languageFromSession = (String) request.getSession()
                .getAttribute(KeyConstants.LANGUAGE.getKey());
        String language = languageFromRequest == null ? languageFromSession
                : languageFromRequest;
        Locale locale = getLocale(language);
        language = locale.getLanguage() + "_" + locale.getCountry();
        //struts2
        /*ActionContext actionContext = ActionContext.getContext();
        if (actionContext != null) { //struts2
            actionContext.setLocale(locale);
        }*/
        request.setAttribute(KeyConstants.LANGUAGE.getKey(), language);
        request.getSession().setAttribute(KeyConstants.LANGUAGE.getKey(), language);
        request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
    }
   
    public static final String getBaseUrl(HttpServletRequest request) {
        String baseUrl = (String) request.getSession().getAttribute(KEY_BASE_URL);
        if (baseUrl != null) {
            return baseUrl;
        }
        baseUrl = MessageFormat.format("//{0}:{1,number,#}{2}",
                request.getServerName(), request.getServerPort(), request.getContextPath());
        request.getSession().setAttribute(KEY_BASE_URL, baseUrl);
        return baseUrl;
    }

}
