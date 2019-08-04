/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.distributedlock.autoconfigure;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import com.suixingpay.takin.distributedlock.IDistributedLock;
import com.suixingpay.takin.distributedlock.cache.ICacheManager;
import com.suixingpay.takin.distributedlock.cache.redis.JedisClusterCacheManager;
import com.suixingpay.takin.distributedlock.cache.redis.SpringJedisCacheManager;
import com.suixingpay.takin.distributedlock.redis.JedisClusterLock;
import com.suixingpay.takin.distributedlock.redis.SpringJedisLock;
import com.suixingpay.takin.distributedlock.zk.ZookeeperLock;
import com.suixingpay.takin.serializer.ISerializer;
import com.suixingpay.takin.serializer.autoconfigure.SerializerAutoConfiguration;

import redis.clients.jedis.JedisCluster;

/**
 * 对分布式锁进行一些默认配置<br>
 * 如果需要自定义，需要自行覆盖即可
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
@Configuration
@EnableConfigurationProperties(DistributedLockProperties.class)
@AutoConfigureAfter({RedisAutoConfiguration.class, SerializerAutoConfiguration.class})
public class DistributedLockConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DistributedLockConfiguration.class);

    /**
     * 默认只支持{@link JedisClusterCacheManager JedisClusterCacheManager}<br>
     * 
     * @param connectionFactory
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ICacheManager.class)
    public ICacheManager distributedLockCacheManager(RedisConnectionFactory connectionFactory, ISerializer serializer) {
        if (null == connectionFactory) {
            return null;
        }
        if (!(connectionFactory instanceof JedisConnectionFactory)) {
            logger.debug("connectionFactory is not JedisConnectionFactory");
            return null;
        }

        RedisConnection redisConnection = null;
        try {
            redisConnection = connectionFactory.getConnection();
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
        if (null != redisConnection) {
            if (redisConnection instanceof RedisClusterConnection) {
                RedisClusterConnection redisClusterConnection = (RedisClusterConnection) redisConnection;
                // 优先使用JedisCluster
                JedisCluster jedisCluster = null;
                jedisCluster = (JedisCluster) redisClusterConnection.getNativeConnection();
                if (null != jedisCluster) {
                    JedisClusterCacheManager manager = new JedisClusterCacheManager(jedisCluster, serializer);
                    logger.debug("ICacheManager with JedisClusterCacheManager auto-configured");
                    return manager;
                }
            } else if (redisConnection instanceof JedisConnection) {
                SpringJedisCacheManager manager = new SpringJedisCacheManager(
                        (JedisConnectionFactory) connectionFactory, serializer);
                logger.debug("ICacheManager with SpringJedisCacheManager auto-configured");
                return manager;
            }
        }
        return null;
    }

    /**
     * 分布式锁实现方法的选定：<br>
     * 优先选用Zookeeper来实现，因为redis可能会出现单点故障（当master节点宕机时），会造成分布式锁不可用。
     * 
     * @param config
     * @param connectionFactory
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(IDistributedLock.class)
    public IDistributedLock distributedLock(DistributedLockProperties config,
            RedisConnectionFactory connectionFactory) {
        String zkServers = config.getZkServers();
        if (null != zkServers && zkServers.trim().length() > 0) {
            ZooKeeperCreator zkCreator = new ZooKeeperCreator(zkServers);
            ZooKeeper zookeeper = zkCreator.getZookeeper();
            if (null != zookeeper) {
                logger.debug("IDistributedLock with ZookeeperLock auto-configured");
                return new ZookeeperLock(zookeeper);
            }
        }
        if (null == connectionFactory) {
            return null;
        }
        if (!(connectionFactory instanceof JedisConnectionFactory)) {
            logger.debug("connectionFactory is not JedisConnectionFactory");
            return null;
        }

        RedisConnection redisConnection = null;
        try {
            redisConnection = connectionFactory.getConnection();
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
        if (null != redisConnection) {
            if (redisConnection instanceof RedisClusterConnection) {
                RedisClusterConnection redisClusterConnection = (RedisClusterConnection) redisConnection;
                // 优先使用JedisCluster
                JedisCluster jedisCluster = null;
                jedisCluster = (JedisCluster) redisClusterConnection.getNativeConnection();
                if (null != jedisCluster) {
                    JedisClusterLock lock = new JedisClusterLock(jedisCluster);
                    logger.debug("IDistributedLock with JedisClusterLock auto-configured");
                    return lock;
                }
            } else if (redisConnection instanceof JedisConnection) {
                SpringJedisLock lock = new SpringJedisLock((JedisConnectionFactory) connectionFactory);
                logger.debug("IDistributedLock with SpringJedisLock auto-configured");
                return lock;
            }
        }
        return null;
    }

    static class ZooKeeperCreator {

        private static final Logger logger = LoggerFactory.getLogger(ZooKeeperCreator.class);

        private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

        private ZooKeeper zookeeper;

        public ZooKeeperCreator(String zkServers) {
            try {
                this.zookeeper = new ZooKeeper(zkServers, 15000, new ZooKeeperWatcher());
                logger.debug("zookeeper state :" + zookeeper.getState());
                try {
                    connectedSemaphore.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.debug("ZooKeeper established......");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        /**
         * 建立zk 的watcher
         */
        private class ZooKeeperWatcher implements Watcher {

            @Override
            public void process(WatchedEvent event) {
                logger.debug("Receive watched event: " + event.getState());
                if (KeeperState.SyncConnected == event.getState()) {
                    connectedSemaphore.countDown();
                }
            }
        }

        public ZooKeeper getZookeeper() {
            return zookeeper;
        }
    }

}
