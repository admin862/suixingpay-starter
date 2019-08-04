/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月19日 下午5:01:12   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.xss;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * XSS Filter
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月19日 下午5:01:12
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月19日 下午5:01:12
 */
@Slf4j
public class XssFilter implements Filter {
    private static final PathMatcher PATH_MATCHER = new AntPathMatcher();
    private final XssProperties properties;
    private final ObjectMapper mapper;

    public XssFilter(XssProperties properties, ObjectMapper mapper) {
        this.properties = properties;
        this.mapper = mapper;
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.debug("(XssFilter) initialize");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String servletPath = request.getServletPath();
        if (isIgnore(servletPath)) {
            chain.doFilter(req, resp);
            return;
        }
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(request, mapper);
        chain.doFilter(xssRequest, resp);
    }

    /**
     * 判断是否被忽略
     * 
     * @param requestUrl
     * @return
     */
    private boolean isIgnore(String requestUrl) {
        boolean ignore = false;
        String[] ignoreUrls = properties.getIgnoreUrls();
        if (ignoreUrls != null && ignoreUrls.length > 0) {
            requestUrl = requestUrl.toLowerCase();
            for (String ignoreUrl : ignoreUrls) {
                ignore = PATH_MATCHER.match(ignoreUrl.toLowerCase(), requestUrl);
                if (ignore) {
                    return ignore;
                }
            }
        }
        return ignore;
    }

    @Override
    public void destroy() {
        log.debug("(XssFilter) destroy");
    }

}
