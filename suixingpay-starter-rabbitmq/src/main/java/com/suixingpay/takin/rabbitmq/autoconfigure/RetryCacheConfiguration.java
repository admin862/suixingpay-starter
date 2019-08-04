/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年04月10日 16时31分
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.rabbitmq.autoconfigure;

import com.suixingpay.takin.manager.ManagerFilterUrlPatternsRegistrationBean;
import com.suixingpay.takin.manager.ManagerUrlRegistrationBean;
import com.suixingpay.takin.manager.autoconfigure.ManagerAutoConfiguration;
import com.suixingpay.takin.rabbitmq.config.RabbitMQRetryHealthIndicator;
import com.suixingpay.takin.rabbitmq.manager.RabbitMQRetryCacheController;
import com.suixingpay.takin.rabbitmq.producer.SimpleMessageProducer;
import com.suixingpay.takin.rabbitmq.retry.PersistentThread;
import com.suixingpay.takin.rabbitmq.retry.RetryCache;
import com.suixingpay.takin.rabbitmq.retry.RetryCacheProperties;
import com.suixingpay.takin.rabbitmq.retry.RetryThread;
import com.suixingpay.takin.serializer.ISerializer;
import com.suixingpay.takin.serializer.autoconfigure.SerializerAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年04月10日 16时31分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年04月10日 16时31分
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({ RetryCacheProperties.class })
@ConditionalOnProperty(value = { RetryCacheProperties.PREFIX + ".enabled", "spring.rabbitmq.publisher-confirms" })
@AutoConfigureAfter({ RabbitMQConfiguration.class, SerializerAutoConfiguration.class })
public class RetryCacheConfiguration {

    @Autowired
    private RetryCacheProperties retryCacheProperties;

    @Bean
    public RetryCache rabbitMQRetryCache(SimpleMessageProducer simpleMessageProducer) {
        RetryCache rabbitMQRetryCache = new RetryCache(retryCacheProperties);
        simpleMessageProducer.setRetryCache(rabbitMQRetryCache);
        log.debug("初始化Message缓存");
        return rabbitMQRetryCache;
    }

    @Bean
    @ConditionalOnClass(HealthIndicator.class)
    public RabbitMQRetryHealthIndicator rabbitMQRetryHealthIndicator(RetryCache rabbitMQRetryCache) {
        log.debug("配置SxfRetryServerHealthIndicator");
        return new RabbitMQRetryHealthIndicator(rabbitMQRetryCache, retryCacheProperties);
    }

    @Bean
    public RetryThread rabbitMQRetryThread(SimpleMessageProducer simpleMessageProducer, RetryCache rabbitMQRetryCache) {
        RetryThread retryThread = new RetryThread(rabbitMQRetryCache, simpleMessageProducer, retryCacheProperties);
        log.debug("配置RetryThread");
        return retryThread;
    }

    @Bean
    public PersistentThread rabbitMQRetryPersistentThread(RetryCache rabbitMQRetryCache, ISerializer serializer) {
        PersistentThread persistentThread = new PersistentThread(rabbitMQRetryCache, serializer, retryCacheProperties);
        log.debug("配置PersistentThread");
        return persistentThread;
    }

    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnMissingBean(RabbitMQRetryCacheController.class)
    public RabbitMQRetryCacheController rabbitMQMessageCacheViewController(RetryCache rabbitMQRetryCache,
            RetryThread rabbitMQRetryThread) {
        return new RabbitMQRetryCacheController(rabbitMQRetryCache, rabbitMQRetryThread);
    }

    @Configuration
    @ConditionalOnWebApplication
    @AutoConfigureBefore(ManagerAutoConfiguration.class)
    @ConditionalOnClass(ManagerUrlRegistrationBean.class)
    @ConditionalOnProperty(value = SxfRabbitMQRetryManagerConfiguration.PREFIX + ".enabled", matchIfMissing = true)
    public static class SxfRabbitMQRetryManagerConfiguration {
        public static final String PREFIX = "suixingpay.manager.rabbitmq.retry";

        @Bean
        public ManagerUrlRegistrationBean rabbitMQRetryManagerUrlRegistrationBean() {
            ManagerUrlRegistrationBean registrationBean = new ManagerUrlRegistrationBean();
            registrationBean.setTitle("RabbitMQ消息重发管理器");
            registrationBean.setUrl("rabbitmq-retry-ui.html");
            log.debug("注册管理web[{}]", registrationBean.getTitle());
            return registrationBean;
        }

        @Bean
        public ManagerFilterUrlPatternsRegistrationBean rabbitMQRetryManagerFilterUrlPatternsRegistrationBean() {
            Collection<String> urlPatterns = Arrays.asList("/rabbitmq-retry-ui.html",
                    RabbitMQRetryCacheController.REQUEST_ROOT_PATH + "/*");
            log.debug("注册应用url[{}]到权限过滤器", StringUtils.collectionToCommaDelimitedString(urlPatterns));
            return new ManagerFilterUrlPatternsRegistrationBean(urlPatterns);
        }
    }
}
