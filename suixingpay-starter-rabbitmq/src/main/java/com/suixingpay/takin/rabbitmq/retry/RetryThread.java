/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年04月10日 16时54分
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.rabbitmq.retry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.suixingpay.takin.rabbitmq.manager.MessageContent;
import com.suixingpay.takin.rabbitmq.producer.SimpleMessageProducer;
import com.suixingpay.takin.rabbitmq.util.FileUtil;
import com.suixingpay.takin.util.json.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年04月10日 16时54分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年04月10日 16时54分
 */
@Slf4j
public class RetryThread implements Runnable {

    private static final String TIMEOUT_FILE_NAME_PREFIX = "timeout";

    private final SimpleMessageProducer messageProducer;

    private final RetryCache retryCache;

    private final RetryCacheProperties properties;

    private volatile boolean running = false;

    private volatile boolean paused = false;

    public RetryThread(RetryCache retryCache, SimpleMessageProducer messageProducer, RetryCacheProperties properties) {
        this.retryCache = retryCache;
        this.messageProducer = messageProducer;
        this.properties = properties;
    }

    @PostConstruct
    public void start() {
        Thread thread = new Thread(this);
        running = true;
        thread.start();
    }

    /**
     * 暂停重试：当MQ出现问题时，手动暂停重试，以减轻MQ压力
     */
    public void pause() {
        this.paused = true;
    }

    /**
     * 是否暂停
     *
     * @return
     */
    public boolean isPaused() {
        return this.paused;
    }

    /**
     * 继续;重新开始
     */
    public void resume() {
        this.paused = false;
    }

    @PreDestroy
    public void stop() {
        running = false;
    }

    /**
     * 消息检查和重发线程
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        while (running) {
            if (this.isPaused()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                continue;
            }
            doCheck();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    private void doCheck() {
        MessageCache[] cacheQueue = getMessageCacheQueue();
        if (null == cacheQueue || cacheQueue.length == 0) {
            return;
        }
        List<MessageContent> timeOutList = new ArrayList<>(cacheQueue.length > 16 ? 0 : cacheQueue.length / 2);
        long now = System.currentTimeMillis();
        for (MessageCache messageCache : cacheQueue) {
            if (null == messageCache) {
                continue;
            }
            if (now - messageCache.getFirstSendTime() > properties.getOverTime()) {
                // 加入超时队列并清除缓存
                timeOutList.add(new MessageContent(messageCache));
                retryCache.del(messageCache);
            } else if (now - messageCache.getLastSendTime() > properties.getRetryInterval()) {
                if (messageCache.isSended()) {
                    try {
                        messageCache.setLastSendTime(now);
                        log.debug("尝试重发 id={}", messageCache.getId());
                        messageProducer.doSendMessage(messageCache.getExchangeName(), messageCache.getRoutingKey(),
                                messageCache.getMessage(), messageCache.getId());
                    } catch (Exception e) {
                    }
                } else {
                    // 加入超时队列并清除缓存
                    timeOutList.add(new MessageContent(messageCache));
                    retryCache.del(messageCache);
                }
            }
        }
        // 持久化超时队列,每日一个文件
        persistentTimeOutMessage(timeOutList);
    }

    private MessageCache[] getMessageCacheQueue() {
        Map<String, MessageCache> messageLocalCache = retryCache.getMessageLocalCache();
        if (null == messageLocalCache || messageLocalCache.isEmpty()) {
            return null;
        }
        MessageCache[] tmpArr = new MessageCache[messageLocalCache.size()];
        // 复制引用
        tmpArr = messageLocalCache.values().toArray(tmpArr);
        return tmpArr;
    }

    private void persistentTimeOutMessage(List<MessageContent> timeOutList) {
        if (timeOutList.size() > 0) {
            try {
                String value = JsonUtil.objectToJson(timeOutList);
                String fileName = TIMEOUT_FILE_NAME_PREFIX + LocalDate.now() + ".cache";
                FileUtil.writeFile(properties.getLocalCachePath() + fileName, value.getBytes(), true);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
