package com.suixingpay.takin.exception.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.suixingpay.takin.exception.ExceptionInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常捕获
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月16日 下午6:07:30
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月16日 下午6:07:30
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler{

    private final SuixingPayExceptionHandler handler;

    public GlobalExceptionHandler(SuixingPayExceptionHandler handler) {
        this.handler = handler;
    }

    @ExceptionHandler(Throwable.class)
    public void handleException(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
        try {
            ExceptionInfo res = handler.resolveException(ex);
            handler.sendErrorMsg(request, response, res);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }
}
