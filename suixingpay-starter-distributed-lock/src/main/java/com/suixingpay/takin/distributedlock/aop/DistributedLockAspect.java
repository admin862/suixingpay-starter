/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年12月14日 上午10:05:44   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.distributedlock.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;

import com.suixingpay.takin.distributedlock.DistributedLockHandler;
import com.suixingpay.takin.distributedlock.annotation.DistributedLock;
import com.suixingpay.takin.distributedlock.autoconfigure.DistributedLockProperties;

/**
 * DistributedLock 注解拦截
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年12月14日 上午10:05:44
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年12月14日 上午10:05:44
 */
@Aspect
public class DistributedLockAspect implements Ordered {

    private DistributedLockProperties properties;

    private DistributedLockHandler distributedLockHandler;

    public DistributedLockAspect(DistributedLockProperties properties, DistributedLockHandler distributedLockHandler) {
        this.properties = properties;
        this.distributedLockHandler = distributedLockHandler;
    }

    @Override
    public int getOrder() {
        return properties.getAopOrder();
    }

    @Around("@annotation(lockAnnotation)")
    public Object doDistributedLockOperation(ProceedingJoinPoint invocation, DistributedLock lockAnnotation)
            throws Throwable {
        return distributedLockHandler.proceed(invocation, lockAnnotation);
    }

}
