/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年8月22日 上午10:34:27   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.redis.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import com.suixingpay.takin.redis.IRedisOperater;
import com.suixingpay.takin.redis.RedisOperaterFactory;
import com.suixingpay.takin.serializer.ISerializer;
import com.suixingpay.takin.serializer.autoconfigure.SerializerAutoConfiguration;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月22日 上午10:34:27
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月22日 上午10:34:27
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@AutoConfigureAfter({ RedisAutoConfiguration.class, SerializerAutoConfiguration.class })
public class RedisOperaterAutoConfigure {

    /**
     * 默认只支持{@link JedisClusterCacheManager JedisClusterCacheManager}<br>
     * 
     * @param config
     * @param serializer
     * @param connectionFactory
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(IRedisOperater.class)
    public IRedisOperater redisOperater(RedisProperties config, ISerializer sxfSerializer,
            RedisConnectionFactory connectionFactory) {
        return RedisOperaterFactory.create(config, sxfSerializer, connectionFactory);
    }
}
