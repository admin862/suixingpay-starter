package com.suixingpay.takin.cache.interceptor;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

import com.jarvis.cache.CacheHandler;
import com.jarvis.cache.annotation.Cache;
import com.suixingpay.takin.cache.autoconfigure.AutoloadCacheProperties;
import com.suixingpay.takin.cache.interceptor.aopproxy.CacheAopProxy;
import com.suixingpay.takin.cache.util.AopUtil;

/**
 * 对@Cache 拦截注解
 * @author jiayu.qiu
 */
public class CacheMethodInterceptor implements MethodInterceptor {

    private static final Logger logger=LoggerFactory.getLogger(CacheMethodInterceptor.class);

    private final CacheHandler cacheHandler;
    
    private final AutoloadCacheProperties config;

    public CacheMethodInterceptor(CacheHandler cacheHandler, AutoloadCacheProperties config) {
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
        logger.debug(invocation.toString());
        if(method.isAnnotationPresent(Cache.class)) {
            Cache cache=method.getAnnotation(Cache.class);
            logger.debug(invocation.getThis().getClass().getName() + "." + method.getName() + "-->@Cache");
            return cacheHandler.proceed(new CacheAopProxy(invocation), cache);
        } else {
            Method specificMethod=AopUtils.getMostSpecificMethod(method, invocation.getThis().getClass());
            if(specificMethod.isAnnotationPresent(Cache.class)) {
                Cache cache=specificMethod.getAnnotation(Cache.class);
                logger.debug(invocation.getThis().getClass().getName() + "." + specificMethod.getName() + "-->@Cache");
                return cacheHandler.proceed(new CacheAopProxy(invocation), cache);
            }
        }
        return invocation.proceed();
    }

}
