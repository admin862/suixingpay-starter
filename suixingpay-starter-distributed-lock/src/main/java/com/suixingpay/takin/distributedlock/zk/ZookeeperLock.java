/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.distributedlock.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.suixingpay.takin.distributedlock.IDistributedLock;

/**
 * 基于Zookeeper实现分布式锁
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
public class ZookeeperLock implements IDistributedLock {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperLock.class);

    private static final String PATH_SEPARATOR = "/";

    private static final byte[] LOCK_VALUE = "".getBytes();

    private final ZooKeeper zookeeper;

    public ZookeeperLock(ZooKeeper zookeeper) {
        this.zookeeper = zookeeper;
    }

    private String getPath(String key) {
        if (!key.startsWith(PATH_SEPARATOR)) {
            return PATH_SEPARATOR + key;
        }
        return key;
    }

    @Override
    public boolean tryLock(String key, int lockExpire, int tryCnt, int interval) {
        String path = getPath(key);
        if (interval <= 0) {
            interval = 10;
        }
        boolean locked = false;
        for (int i = 0; !(locked = getLock(path)) && i < tryCnt; i++) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return locked;
    }

    private boolean getLock(String path) {
        try {
            zookeeper.create(path, LOCK_VALUE, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            logger.debug("success to acquire lock for [" + path + "]");
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public void release(String key) {
        String path = getPath(key);
        try {
            zookeeper.delete(path, -1);
            logger.debug("release the lock for [" + path + "] ... ...");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
