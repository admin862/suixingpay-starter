/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年4月12日 上午9:28:16   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.rabbitmq.retry;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.suixingpay.takin.rabbitmq.util.FileUtil;
import com.suixingpay.takin.serializer.ISerializer;
import java.io.File;

import lombok.extern.slf4j.Slf4j;

/**
 * 持久化消息缓存
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年4月12日 上午9:28:16
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年4月12日 上午9:28:16
 */
@Slf4j
public class PersistentThread implements Runnable {

    private static final String CACHE_FILE_NAME = "retry.cache";

    private final RetryCache retryCache;

    private final ISerializer serializer;

    private final RetryCacheProperties properties;

    private volatile boolean running = false;

    public PersistentThread(RetryCache retryCache, ISerializer serializer, RetryCacheProperties properties) {
        this.retryCache = retryCache;
        this.serializer = serializer;
        this.properties = properties;
    }

    @PostConstruct
    public void start() {
        if (retryCache == null || serializer == null) {
            return;
        }
        loadMessage();
        Thread hook = new Thread(new Runnable() {

            @Override
            public void run() {
                persistent();
            }
        });
        Runtime.getRuntime().addShutdownHook(hook);
        Thread thread = new Thread(this);
        running = true;
        thread.start();
    }

    @PreDestroy
    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            persistent();
            try {
                Thread.sleep(properties.getPersistentInterval());
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private synchronized void persistent() {
        // 1.获取缓存消息
        Map<String, MessageCache> cacheMap = retryCache.getMessageLocalCache();
        // 2.写入本地磁盘
        String filePath = properties.getLocalCachePath() + CACHE_FILE_NAME;
        if (null != cacheMap && cacheMap.size() > 0) {
            try {
                FileUtil.writeFile(filePath, serializer.serialize(cacheMap), false);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } else {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    private void loadMessage() {
        // 1.开启时读取磁盘并将消息加载到缓存
        byte[] data = FileUtil.readFile(properties.getLocalCachePath() + CACHE_FILE_NAME);
        try {
            @SuppressWarnings("unchecked")
            Map<String, MessageCache> map = (Map<String, MessageCache>) serializer.deserialize(data);
            retryCache.putAll(map);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

}
