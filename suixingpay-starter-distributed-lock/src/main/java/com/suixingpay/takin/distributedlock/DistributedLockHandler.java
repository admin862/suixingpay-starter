/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.distributedlock;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.suixingpay.takin.distributedlock.annotation.DistributedLock;
import com.suixingpay.takin.distributedlock.cache.ICacheManager;
import com.suixingpay.takin.distributedlock.exception.BusinessFinishedException;
import com.suixingpay.takin.distributedlock.exception.DistributedLockConflictException;
import com.suixingpay.takin.expression.AbstractExpressionParser;

/**
 * AOP拦截后，对请求进行处理
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
public class DistributedLockHandler {

    private static final Logger logger = LoggerFactory.getLogger(DistributedLockHandler.class);

    private static final String NULL_VALUE = "<<NULL>>";

    private static final String LAST_RESUTL_KEY = "_last_result";

    private final IDistributedLock distributedLock;

    private final AbstractExpressionParser scriptParser;

    private final ICacheManager cacheManager;

    private final String namespace;

    public DistributedLockHandler(IDistributedLock distributedLock, AbstractExpressionParser scriptParser,
            ICacheManager cacheManager, String namespace) {
        this.distributedLock = distributedLock;
        this.scriptParser = scriptParser;
        this.cacheManager = cacheManager;
        if (null != namespace && namespace.length() > 0) {
            this.namespace = namespace + ":";
        } else {
            this.namespace = "";
        }
    }

    /**
     * 用于处理 拦截@DistributedLock注解：<br>
     * 1: 先检查业务是否已经完成，避免重复提交<br>
     * 2：先尝试获取分布式锁，如果获取失败抛DistributedLockConflictException 异常<br>
     * 3: 获取分布式锁成功后，检查业务是否已经完成，避免重复处理<br>
     * 4：处理业务；<br>
     * 4：设置业务已经完成状态<br>
     * 注意：业务状态是会过期的，所以在业务处理中，还需要使用乐观锁等机制保证业务不会重复处理。
     * 
     * @param invocation MethodInvocation
     * @param lock DistributedLock
     * @return 业务数据
     * @throws Throwable DistributedLockConflictException 或
     *             BusinessFinishedException
     */
    public Object proceed(ProceedingJoinPoint invocation, DistributedLock lock) throws Throwable {
        Object[] args = invocation.getArgs();
        Method method = getMethod(invocation);
        String key = buildKey(method, args, lock.key());
        String businessStatusKey = key + LAST_RESUTL_KEY;
        Object retVal = getLastResult(businessStatusKey, invocation, lock);// 在锁外面进行判断，以减少持锁的次数，提升吞吐量
        if (null != retVal) {
            if (NULL_VALUE.equals(retVal)) {
                return null;
            }
            return retVal;
        }
        if (!distributedLock.tryLock(key, lock.leaseTime(), lock.tryCnt(), lock.interval())) {
            return fallback(invocation, lock, key);
        }
        logger.debug("acquire DistributedLock for [{}]", key);
        try {
            retVal = getLastResult(businessStatusKey, invocation, lock);// 持锁后一定要再次进行检查。
            if (null != retVal) {
                if (NULL_VALUE.equals(retVal)) {
                    return null;
                }
                return retVal;
            }
            retVal = invocation.proceed();
            setBusinessStatus(businessStatusKey, lock, retVal);
            return retVal;
        } catch (Throwable e) {
            throw e;
        } finally {
            distributedLock.release(key);
        }
    }

    public Method getMethod(ProceedingJoinPoint invocation) {
        Signature signature = invocation.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return methodSignature.getMethod();
    }

    private static final String EXP_ARGS = "args";

    private <T> T getValue(String exp, Object[] args, Class<T> valueType) throws Exception {
        Map<String, Object> variables = new HashMap<>(1);
        variables.put(EXP_ARGS, args);
        return scriptParser.getValue(exp, variables, valueType);
    }

    private String buildKey(Method method, Object[] args, String keyScript) throws Exception {
        String key = getValue(keyScript, args, String.class);
        if (null == key || key.trim().length() == 0) {
            throw new IllegalArgumentException("the DistributedLock key value of method :" + method.getName());
        }
        return namespace + key;
    }

    private Object fallback(ProceedingJoinPoint invocation, DistributedLock lock, String key) throws Throwable {
        String fallbackMethodName = lock.fallbackMethod();
        if (null != fallbackMethodName && fallbackMethodName.trim().length() > 0) {
            Method invMethod = getMethod(invocation);
            Object taget = invocation.getTarget();
            Method fallbackMethod = taget.getClass().getMethod(fallbackMethodName, invMethod.getParameterTypes());
            if (null != fallbackMethod && invMethod.getReturnType().equals(fallbackMethod.getReturnType())) {
                return fallbackMethod.invoke(taget, invocation.getArgs());
            }
        }
        throw new DistributedLockConflictException("does not acquire distributed lock for [" + key + "]");
    }

    /**
     * 检查业务是否已经执行完成，如果已经执行完成，则返回上次执行结果或抛异常
     * 
     * @param businessStatusKey
     * @param invocation
     * @param lock
     * @throws Throwable
     */
    private Object getLastResult(String businessStatusKey, ProceedingJoinPoint invocation, DistributedLock lock)
            throws Throwable {
        if (null == cacheManager) {
            return null;
        }
        if (!lock.checkBusinessFinished()) {
            return null;
        }
        Object lastResult = cacheManager.get(businessStatusKey);// 从缓存中取出这次的执行结果
        // 通过checkBusinessFinishedMethod进行二次检查
        if (null == lastResult && lock.checkBusinessFinishedMethod().trim().length() > 0) {
            BusinessResult<Object> res = getBusinessResult(invocation, lock);
            if (null != res && res.isFinished()) {
                if (null == res.getLastResult()) {
                    lastResult = NULL_VALUE;
                } else {
                    lastResult = res.getLastResult();
                }
                cacheManager.setCache(businessStatusKey, lastResult, lock.businessStatusExpire());
            }
        }
        if (null == lastResult) {// 上次执行结果为空，说明业务还没有执行
            return null;
        }

        if (lock.returnLastResult()) {// 如果是返回上次值
            return lastResult;
        }
        String msg = getValue(lock.businessFinishedMessage(), invocation.getArgs(), String.class);
        if (null == msg || msg.length() == 0) {
            msg = "the business of [" + businessStatusKey + "] had finished";
        }
        throw new BusinessFinishedException(businessStatusKey, msg);
    }

    /**
     * checkBusinessFinishedMethod 用于指定确认业务是否已经完成的函数
     * 
     * @param invocation
     * @param lock
     * @return
     * @throws Throwable
     */
    @SuppressWarnings("unchecked")
    private BusinessResult<Object> getBusinessResult(ProceedingJoinPoint invocation, DistributedLock lock)
            throws Throwable {
        Method invMethod = getMethod(invocation);
        Object taget = invocation.getTarget();
        Method checkBusinessFinishedMethod = taget.getClass().getMethod(lock.checkBusinessFinishedMethod(),
                invMethod.getParameterTypes());
        if (null != checkBusinessFinishedMethod) {
            return (BusinessResult<Object>) checkBusinessFinishedMethod.invoke(taget, invocation.getArgs());
        }
        return null;
    }

    /**
     * 往缓存中设置业务状态
     * 
     * @param businessStatusKey
     * @param lock
     * @param result
     * @throws Exception
     */
    private void setBusinessStatus(String businessStatusKey, DistributedLock lock, Object result) throws Exception {
        if (null == cacheManager) {
            return;
        }
        if (null == businessStatusKey || businessStatusKey.length() == 0) {
            return;
        }
        if (null == result) {
            cacheManager.setCache(businessStatusKey, NULL_VALUE, lock.businessStatusExpire());
        } else {
            cacheManager.setCache(businessStatusKey, result, lock.businessStatusExpire());
        }
    }

}
