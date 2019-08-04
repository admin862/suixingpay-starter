package com.suixingpay.takin.cache.autoconfigure;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.PostConstruct;

import com.suixingpay.takin.manager.autoconfigure.ManagerAutoConfiguration;
import org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.jarvis.cache.CacheHandler;
import com.jarvis.cache.ICacheManager;
import com.jarvis.cache.annotation.Cache;
import com.jarvis.cache.annotation.CacheDelete;
import com.jarvis.cache.annotation.CacheDeleteTransactional;
import com.jarvis.cache.clone.ICloner;
import com.jarvis.cache.script.AbstractScriptParser;
import com.jarvis.cache.serializer.ISerializer;
import com.suixingpay.takin.cache.admin.AutoloadCacheController;
import com.suixingpay.takin.cache.interceptor.CacheDeleteInterceptor;
import com.suixingpay.takin.cache.interceptor.CacheDeleteTransactionalInterceptor;
import com.suixingpay.takin.cache.interceptor.CacheMethodInterceptor;
import com.suixingpay.takin.manager.ManagerFilterUrlPatternsRegistrationBean;
import com.suixingpay.takin.manager.ManagerUrlRegistrationBean;

import lombok.extern.slf4j.Slf4j;

/**
 * 对autoload-cache进行自动配置<br>
 * 需要先完成 {@link AutoloadCacheManageConfiguration
 * AutoloadCacheManageConfiguration}的配置<br>
 * 然后执行此类中的AOP相关的配置<br>
 *
 * @author jiayu.qiu
 */
@Slf4j
@Configuration
@ConditionalOnClass(name = "com.jarvis.cache.CacheHandler")
@AutoConfigureAfter(AutoloadCacheManageConfiguration.class)
@ConditionalOnProperty(value = AutoloadCacheProperties.PREFIX + ".enable", matchIfMissing = true)
public class AutoloadCacheAutoConfigure {

    private static final String VALIDATOR_BEAN_NAME = "autoloadCacheAutoConfigurationValidator";

    @Autowired
    private AutoloadCacheProperties config;

    @Bean(name = VALIDATOR_BEAN_NAME)
    public CacheManagerValidator autoloadCacheAutoConfigurationValidator() {
        return new CacheManagerValidator();
    }

    @Bean(destroyMethod = "destroy")
    @ConditionalOnMissingBean(CacheHandler.class)
    @ConditionalOnBean({ ICacheManager.class, AbstractScriptParser.class, ICloner.class })
    public CacheHandler autoloadCacheHandler(ICacheManager cacheManager, AbstractScriptParser scriptParser,
            ICloner cloner) {
        return new CacheHandler(cacheManager, scriptParser, config.getConfig(), cloner);
    }

    // 1. 创建通知
    @Bean
    @ConditionalOnBean(CacheHandler.class)
    @ConditionalOnProperty(value = AutoloadCacheProperties.PREFIX + ".enable-read-and-write", matchIfMissing = true)
    public CacheMethodInterceptor autoloadCacheMethodInterceptor(CacheHandler cacheHandler) {
        log.debug("auto configure CacheMethodInterceptor");
        return new CacheMethodInterceptor(cacheHandler, config);
    }

    @Bean
    @ConditionalOnBean(CacheHandler.class)
    @ConditionalOnProperty(value = AutoloadCacheProperties.PREFIX + ".enable-delete", matchIfMissing = true)
    public CacheDeleteInterceptor autoloadCacheDeleteInterceptor(CacheHandler cacheHandler) {
        log.debug("auto configure CacheDeleteInterceptor");
        return new CacheDeleteInterceptor(cacheHandler, config);
    }

    @Bean
    @ConditionalOnBean(CacheHandler.class)
    @ConditionalOnProperty(value = AutoloadCacheProperties.PREFIX + ".enable-delete", matchIfMissing = true)
    public CacheDeleteTransactionalInterceptor autoloadCacheDeleteTransactionalInterceptor(CacheHandler cacheHandler) {
        log.debug("auto configure CacheDeleteTransactionalInterceptor");
        return new CacheDeleteTransactionalInterceptor(cacheHandler, config);
    }

    // 2.配置Advisor
    @Bean("autoloadCacheAdvisor")
    @ConditionalOnBean({ CacheMethodInterceptor.class })
    public AbstractPointcutAdvisor autoloadCacheAdvisor(CacheMethodInterceptor cacheMethodInterceptor) {
        AbstractPointcutAdvisor cacheAdvisor = new MethodAnnotationPointcutAdvisor(Cache.class, cacheMethodInterceptor);
        cacheAdvisor.setOrder(config.getCacheOrder());
        return cacheAdvisor;
    }

    @Bean("autoloadCacheDeleteAdvisor")
    @ConditionalOnBean({ CacheDeleteInterceptor.class })
    public AbstractPointcutAdvisor autoloadCacheDeleteAdvisor(CacheDeleteInterceptor cacheDeleteInterceptor) {
        AbstractPointcutAdvisor cacheDeleteAdvisor = new MethodAnnotationPointcutAdvisor(CacheDelete.class,
                cacheDeleteInterceptor);
        cacheDeleteAdvisor.setOrder(config.getDeleteCacheOrder());
        return cacheDeleteAdvisor;
    }

    @Bean("autoloadCacheDeleteTransactionalAdvisor")
    @ConditionalOnBean({ CacheDeleteTransactionalInterceptor.class })
    public AbstractPointcutAdvisor autoloadCacheDeleteTransactionalAdvisor(
            CacheDeleteTransactionalInterceptor cacheDeleteTransactionalInterceptor) {
        AbstractPointcutAdvisor cacheDeleteTransactionalAdvisor = new MethodAnnotationPointcutAdvisor(
                CacheDeleteTransactional.class, cacheDeleteTransactionalInterceptor);
        cacheDeleteTransactionalAdvisor.setOrder(config.getDeleteCacheTransactionalOrder());
        return cacheDeleteTransactionalAdvisor;
    }

    // 3.配置ProxyCreator
    @Bean
    @ConditionalOnBean(CacheHandler.class)
    public AbstractAdvisorAutoProxyCreator autoloadCacheAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxy = new DefaultAdvisorAutoProxyCreator();
        proxy.setAdvisorBeanNamePrefix("autoloadCache");
        proxy.setProxyTargetClass(config.isProxyTargetClass());
        // proxy.setInterceptorNames("cacheAdvisor","cacheDeleteAdvisor","cacheDeleteTransactionalAdvisor");//
        // 注意此处不需要设置，否则会执行两次
        return proxy;
    }

    // 4.配置管理web并注册到[统一管理web]
    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnMissingBean(AutoloadCacheController.class)
    public AutoloadCacheController autoloadCacheController(CacheHandler autoloadCacheHandler) {
        log.debug("配置管理web[{}]", "AutoloadCacheController");
        return new AutoloadCacheController(autoloadCacheHandler);
    }

    @Configuration
    @ConditionalOnWebApplication
    @AutoConfigureBefore(ManagerAutoConfiguration.class)
    @ConditionalOnClass(ManagerUrlRegistrationBean.class)
    @ConditionalOnProperty(value = CacheManagerConfiguration.PREFIX + ".enabled", matchIfMissing = true)
    public static class CacheManagerConfiguration {
        public static final String PREFIX = "suixingpay.manager.autoloadcache";

        @Bean
        public ManagerUrlRegistrationBean autoloadCacheManagerUrlRegistrationBean() {
            ManagerUrlRegistrationBean registrationBean = new ManagerUrlRegistrationBean();
            registrationBean.setTitle("AutoloadCache操作管理");
            registrationBean.setUrl("autoload-cache-ui.html");
            log.debug("注册管理web[{}]", registrationBean.getTitle());
            return registrationBean;
        }

        @Bean
        public ManagerFilterUrlPatternsRegistrationBean autoloadCacheManagerFilterUrlPatternsRegistrationBean() {
            Collection<String> urlPatterns = Arrays.asList("/autoload-cache-ui.html", "//autoload-cache/*");
            log.debug("注册应用url[{}]到权限过滤器", StringUtils.collectionToCommaDelimitedString(urlPatterns));
            return new ManagerFilterUrlPatternsRegistrationBean(urlPatterns);
        }
    }

    static class CacheManagerValidator {

        @Autowired(required = false)
        private AbstractScriptParser scriptParser;

        @Autowired(required = false)
        private ISerializer<Object> serializer;

        @Autowired(required = false)
        private ICacheManager cacheManager;

        @PostConstruct
        public void checkHasCacheManager() {
            Assert.notNull(this.scriptParser, "No script parser could be auto-configured");
            Assert.notNull(this.serializer, "No serializer could be auto-configured");
            Assert.notNull(this.cacheManager, "No cache manager could be auto-configured");
        }

    }

}
