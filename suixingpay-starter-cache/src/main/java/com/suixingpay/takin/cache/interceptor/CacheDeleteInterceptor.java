package com.suixingpay.takin.cache.interceptor;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

import com.jarvis.cache.CacheHandler;
import com.jarvis.cache.annotation.CacheDelete;
import com.suixingpay.takin.cache.autoconfigure.AutoloadCacheProperties;
import com.suixingpay.takin.cache.interceptor.aopproxy.DeleteCacheAopProxy;
import com.suixingpay.takin.cache.util.AopUtil;

/**
 * 对@CacheDelete 拦截注解
 * @author jiayu.qiu
 */
public class CacheDeleteInterceptor implements MethodInterceptor {

    private static final Logger logger=LoggerFactory.getLogger(CacheDeleteInterceptor.class);

    private final CacheHandler cacheHandler;

    private final AutoloadCacheProperties config;
    
    public CacheDeleteInterceptor(CacheHandler cacheHandler, AutoloadCacheProperties config) {
        this.cacheHandler=cacheHandler;
        this.config=config;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if(!this.config.isEnable()) {
            return invocation.proceed();
        }
        Class<?> cls=AopUtil.getTargetClass(invocation.getThis());
        Method method=invocation.getMethod();
        if(!cls.equals(invocation.getThis().getClass())) {
            logger.debug(invocation.getThis().getClass() + "-->" + cls);
            return invocation.proceed();
        }
        Object result=invocation.proceed();
        if(method.isAnnotationPresent(CacheDelete.class)) {
            CacheDelete cacheDelete=method.getAnnotation(CacheDelete.class);
            logger.debug(invocation.getThis().getClass().getName() + "." + method.getName() + "-->@CacheDelete");
            cacheHandler.deleteCache(new DeleteCacheAopProxy(invocation), cacheDelete, result);
        } else {
            Method specificMethod=AopUtils.getMostSpecificMethod(method, invocation.getThis().getClass());
            if(specificMethod.isAnnotationPresent(CacheDelete.class)) {
                CacheDelete cacheDelete=specificMethod.getAnnotation(CacheDelete.class);
                logger.debug(invocation.getThis().getClass().getName() + "." + specificMethod.getName() + "-->@CacheDelete");
                cacheHandler.deleteCache(new DeleteCacheAopProxy(invocation), cacheDelete, result);
            }
        }
        return result;
    }

}
