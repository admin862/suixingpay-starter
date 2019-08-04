/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年12月27日 上午11:26:44   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.token.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CookieUtil
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年12月27日 上午11:26:44
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年12月27日 上午11:26:44
 */
public class CookieUtil {

    /**
     * 添加一个新Cookie
     * 
     * @param response HttpServletResponse
     * @param cookieName cookie名称
     * @param cookieValue cookie值
     * @param domain cookie所属的子域
     * @param httpOnly 是否将cookie设置成HttpOnly
     * @param maxAge 设置cookie的最大生存期
     * @param path 设置cookie路径
     * @param secure 是否只允许HTTPS访问
     * @return null
     */
    public static void addCookie(HttpServletResponse response, String cookieName, String cookieValue, String domain,
            boolean httpOnly, int maxAge, String path, boolean secure) {
        if (cookieName == null || cookieName.length() == 0) {
            return;
        }
        if (cookieValue == null) {
            cookieValue = "";
        }

        Cookie cookie = new Cookie(cookieName, cookieValue);
        if (domain != null && domain.length() > 0) {
            cookie.setDomain(domain);
        }
        cookie.setHttpOnly(httpOnly);
        if (maxAge > 0) {
            cookie.setMaxAge(maxAge);
        }
        if (path == null) {
            cookie.setPath("/");
        } else {
            cookie.setPath(path);
        }
        cookie.setSecure(secure);
        response.addCookie(cookie);
    }

    /**
     * 根据Cookie名获取对应的Cookie
     * 
     * @param request HttpServletRequest
     * @param cookieName cookie名称
     * @return 对应cookie，如果不存在则返回null
     */
    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0 || cookieName == null || cookieName.length() == 0) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return (Cookie) cookie;
            }
        }
        return null;
    }

    /**
     * 根据Cookie名获取对应的Cookie值
     * 
     * @param request HttpServletRequest
     * @param cookieName cookie名称
     * @return 对应cookie值，如果不存在则返回null
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie cookie = getCookie(request, cookieName);
        return cookie == null ? null : cookie.getValue();
    }

    /**
     * 删除指定Cookie
     * 
     * @param response HttpServletResponse
     * @param cookie 待删除cookie
     */
    public static void delCookie(HttpServletResponse response, Cookie cookie) {
        if (cookie != null) {
            cookie.setPath("/");
            cookie.setMaxAge(0);
            cookie.setValue(null);
            response.addCookie(cookie);
        }
    }

    /**
     * 根据cookie名删除指定的cookie
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param cookieName 待删除cookie名
     */
    public static void delCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        Cookie c = getCookie(request, cookieName);
        if (c != null && c.getName().equals(cookieName)) {
            delCookie(response, c);
        }
    }
}
