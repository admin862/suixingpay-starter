/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年04月13日 09时51分
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.rabbitmq.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suixingpay.takin.rabbitmq.retry.MessageCache;
import com.suixingpay.takin.rabbitmq.retry.RetryCache;
import com.suixingpay.takin.rabbitmq.retry.RetryThread;

/**
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年04月13日 09时51分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年04月13日 09时51分
 */
@RestController
@RequestMapping(RabbitMQRetryCacheController.REQUEST_ROOT_PATH)
public class RabbitMQRetryCacheController {

    public static final String REQUEST_ROOT_PATH = "/rabbitmq/retry-cache";

    private final RetryCache retryCache;

    private final RetryThread retryThread;

    public RabbitMQRetryCacheController(RetryCache retryCache, RetryThread retryThread) {
        this.retryCache = retryCache;
        this.retryThread = retryThread;
    }

    @GetMapping
    public List<MessageContent> findMessageLocalCache() {
        if (null == retryCache) {
            return null;
        }
        Map<String, MessageCache> localCache = retryCache.getMessageLocalCache();
        List<MessageContent> list = new ArrayList<>();
        for (Map.Entry<String, MessageCache> messageCacheEntry : localCache.entrySet()) {
            list.add(new MessageContent(messageCacheEntry.getValue()));
        }
        return list;
    }

    @PostMapping("/pause")
    public boolean pause() {
        if (null == retryThread) {
            return false;
        }
        try {
            retryThread.pause();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @PostMapping("/resume")
    public boolean resume() {
        if (null == retryThread) {
            return false;
        }
        try {
            retryThread.resume();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("/status")
    public boolean isPaused() {
        if (null == retryThread) {
            return false;
        }
        return retryThread.isPaused();
    }
}
