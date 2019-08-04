/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年11月20日 下午5:34:10   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.logback.filter;

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

import com.suixingpay.takin.logback.config.TraceProperties;
import com.suixingpay.takin.logback.util.MiscUtil;
import com.suixingpay.takin.logback.util.TraceUtil;

/**
 * 请求追踪
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年11月20日 下午5:34:10
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年11月20日 下午5:34:10
 */
public class TraceFilter implements Filter {
    private static final String CONCAT_CHAR = "-";
    private static final PathMatcher PATH_MATCHER = new AntPathMatcher();
    private final TraceProperties properties;

    public TraceFilter(TraceProperties properties) {
        this.properties = properties;
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
        try {
            insertTraceId(request);
            chain.doFilter(req, resp);
        } finally {
            removeTraceId();
        }
    }

    private void insertTraceId(HttpServletRequest request) {
        String requestId = request.getParameter(properties.getTraceId());
        if (null == requestId || requestId.trim().length() == 0) {
            requestId = request.getHeader(properties.getTraceId());
        }
        String traceId = MiscUtil.genUniqueStr();
        if (null != requestId && requestId.length() > 0) {
            String invalidChars = properties.getInvalidChars();
            if (null == invalidChars || invalidChars.length() == 0) {
                invalidChars = TraceProperties.DEFAULT_INVALID_CHARS;
            }
            // 过滤非法字符，避免打乱日志格式
            requestId = requestId.replaceAll(invalidChars, "");
            int maxLength = properties.getTraceIdMaxLength();
            if (maxLength > 0 && requestId.length() > maxLength) {
                // 限制长度，避免非法输入
                requestId = requestId.substring(0, maxLength);
            }
            traceId = traceId.concat(CONCAT_CHAR).concat(requestId);
        }
        TraceUtil.beginTrace(traceId);
    }

    private void removeTraceId() {
        TraceUtil.endTrace();
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
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}
