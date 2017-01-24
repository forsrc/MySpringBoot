/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.forsrc.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * The type Cookie utils.
 */
public final class CookieUtils {

    private CookieUtils() {
    }

    /**
     * Delete.
     *
     * @param request  the request
     * @param response the response
     * @param name     the name
     */
    public static void delete(HttpServletRequest request,
                              HttpServletResponse response, String name) {

        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        cookie.setPath(request.getContextPath());
        response.addCookie(cookie);
    }

    /**
     * Gets cookie.
     *
     * @param name    the name
     * @param request the request
     * @return the cookie
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public static String getCookie(String name, HttpServletRequest request)
            throws UnsupportedEncodingException {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return URLDecoder.decode(cookie.getValue(), "UTF-8");
                }
            }
        }
        return null;
    }

    /**
     * Save cookie.
     *
     * @param request  the request
     * @param response the response
     * @param name     the name
     * @param value    the value
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public static void saveCookie(HttpServletRequest request,
                                  HttpServletResponse response, String name, String value)
            throws UnsupportedEncodingException {

        CookieUtils
                .saveCookie(request, response, name, value, 7 * 24 * 60 * 60);
    }

    /**
     * Save cookie.
     *
     * @param request  the request
     * @param response the response
     * @param name     the name
     * @param value    the value
     * @param maxAge   the max age
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public static void saveCookie(HttpServletRequest request,
                                  HttpServletResponse response, String name, String value, int maxAge)
            throws UnsupportedEncodingException {

        Cookie cookie = new Cookie(name, URLEncoder.encode(value, "UTF-8"));
        cookie.setMaxAge(maxAge);
        cookie.setPath(request.getContextPath());
        response.addCookie(cookie);
    }
}
