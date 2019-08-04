/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.distributedlock.autoconfigure;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.Assert;

import com.suixingpay.takin.distributedlock.DistributedLockHandler;
import com.suixingpay.takin.distributedlock.IDistributedLock;
import com.suixingpay.takin.distributedlock.aop.DistributedLockAspect;
import com.suixingpay.takin.distributedlock.cache.ICacheManager;
import com.suixingpay.takin.expression.AbstractExpressionParser;
import com.suixingpay.takin.expression.autoconfigure.ExpressionAutoConfiguration;

/**
 * 对分布式锁进行自动配置<br>
 * 需要先完成 {@link DistributedLockConfiguration}的配置<br>
 * 然后执行此类中的AOP相关的配置<br>
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
@Configuration
@EnableAspectJAutoProxy
@AutoConfigureAfter({DistributedLockConfiguration.class, ExpressionAutoConfiguration.class})
public class DistributedLockAutoConfigure {

    private static final String VALIDATOR_BEAN_NAME = "distributedLockAutoConfigurationValidator";

    @Autowired
    private DistributedLockProperties config;

    @Bean(name = VALIDATOR_BEAN_NAME)
    public DistributedLockValidator distributedLockAutoConfigurationValidator() {
        return new DistributedLockValidator();
    }

    @Bean
    @ConditionalOnMissingBean(DistributedLockHandler.class)
    @ConditionalOnBean({ DistributedLockValidator.class, IDistributedLock.class, AbstractExpressionParser.class })
    public DistributedLockHandler distributedLockHandler(IDistributedLock distributedLock,
            AbstractExpressionParser scriptParser, ICacheManager cacheManager) {
        return new DistributedLockHandler(distributedLock, scriptParser, cacheManager, config.getNamespace());
    }

    // 1. 创建通知
    @Bean
    @ConditionalOnBean(DistributedLockHandler.class)
    @ConditionalOnMissingBean(DistributedLockAspect.class)
    public DistributedLockAspect DistributedLockAspect(DistributedLockHandler handler) {
        return new DistributedLockAspect(config, handler);
    }

    static class DistributedLockValidator {

        @Autowired(required = false)
        private AbstractExpressionParser scriptParser;

        @Autowired(required = false)
        private IDistributedLock distributedLock;

        @PostConstruct
        public void checkHasCacheManager() {
            Assert.notNull(this.scriptParser, "No script parser could be auto-configured");
            Assert.notNull(this.distributedLock, "No distributed lock could be auto-configured");
        }

    }
}
