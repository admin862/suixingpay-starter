/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: xingxin[xing_xin@suixingpay.com]
 * @date: 2017年6月16日 下午4:50:45
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.token.interceptor;

import java.lang.reflect.Method;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.suixingpay.takin.exception.ExceptionInfo;
import com.suixingpay.takin.exception.handler.SuixingPayExceptionHandler;
import com.suixingpay.takin.exception.type.ExceptionCode;
import com.suixingpay.takin.token.TokenHelper;
import com.suixingpay.takin.token.TokenInfo;
import com.suixingpay.takin.token.TokenWapper;
import com.suixingpay.takin.token.annotation.IgnoreToken;
import com.suixingpay.takin.token.annotation.PreAuthorize;
import com.suixingpay.takin.token.auth.AuthenticationChecker;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * token 拦截器，检查是否已登录，是否有权限等
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年12月27日 上午10:04:18
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年12月27日 上午10:04:18
 */
@Getter
@Slf4j
public class TokenInterceptor extends HandlerInterceptorAdapter {

    private static final String OPTIONS = "OPTIONS";

    private final AuthenticationChecker authenticationChecker;

    private final TokenHelper tokenHelper;

    private final SuixingPayExceptionHandler sxfExceptionHandler;

    public TokenInterceptor(TokenHelper tokenHelper, AuthenticationChecker authenticationChecker,
            SuixingPayExceptionHandler sxfExceptionHandler) {
        this.tokenHelper = tokenHelper;
        this.authenticationChecker = authenticationChecker;
        this.sxfExceptionHandler = sxfExceptionHandler;
    }

    private boolean isIgnore(HttpServletRequest request, Method method, Object handler) {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String requestMethod = request.getMethod();
        if (OPTIONS.equals(requestMethod)) {
            return true;
        }
        String servletPath = request.getServletPath();
        /**
         * 若目标方法忽略了安全性检查，则直接调用目标方法
         */
        if (method.isAnnotationPresent(IgnoreToken.class)) {
            return true;
        }
        log.trace("servletPath:{}", servletPath);
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (isIgnore(request, method, handler)) {
            return true;
        }
        Optional<TokenWapper> tokenOptional = tokenHelper.getTokenWapper(request);
        if (!tokenOptional.isPresent()) {
            setUnAuth(request, response);
            return false;
        }
        TokenWapper tokenWapper = tokenOptional.get();
        if (!hasRole(method, tokenWapper.getTokenInfo())) {
            setForbidden(request, response);
            return false;
        }
        if (!authenticationChecker.hasRole(request, tokenWapper.getTokenInfo())) {
            setForbidden(request, response);
            return false;
        }
        // 刷新token
        tokenHelper.refresh(tokenWapper.getToken());
        return true;
    }

    private boolean hasRole(Method method, TokenInfo tokenInfo) {
        if (method.isAnnotationPresent(PreAuthorize.class)) {
            PreAuthorize authorize = method.getAnnotation(PreAuthorize.class);
            return authenticationChecker.hasRole(authorize, tokenInfo);
        }
        return true;
    }

    private void setUnAuth(HttpServletRequest request, HttpServletResponse response) {
        ExceptionCode code = ExceptionCode.UNAUTHORIZED;
        ExceptionInfo info = new ExceptionInfo(code.value(), code.defaultMessage());
        this.sxfExceptionHandler.sendErrorMsg(request, response, info);
    }

    private void setForbidden(HttpServletRequest request, HttpServletResponse response) {
        ExceptionCode code = ExceptionCode.FORBIDDEN;
        ExceptionInfo info = new ExceptionInfo(code.value(), code.defaultMessage());
        this.sxfExceptionHandler.sendErrorMsg(request, response, info);
    }
}
