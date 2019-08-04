/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年03月20日 15时45分
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.rabbitmq.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * Rabbitmq 消费者管理器
 * 
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年03月20日 15时45分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年03月20日 15时45分
 */
@Slf4j
@RestController
@RequestMapping("/rabbitmq/consumer/")
public class RabbitmqConsumerManagerController {

    private final AbstractMessageListenerContainer[] containers;

    public RabbitmqConsumerManagerController(ObjectProvider<AbstractMessageListenerContainer[]> listenerContainer) {
        if (null != listenerContainer) {
            containers = listenerContainer.getIfAvailable();
        } else {
            containers = null;
        }
    }

    @GetMapping
    public List<ConsumerInfo> listConsumers() {
        int size = null == containers ? 0 : containers.length;
        List<ConsumerInfo> list = new ArrayList<>(size);
        if (null == containers) {
            return list;
        }
        for (int i = 0; i < size; i++) {
            AbstractMessageListenerContainer container = containers[i];
            ConsumerInfo consumerInfo = new ConsumerInfo();
            consumerInfo.setName(container.getListenerId());
            consumerInfo.setQueueNames(StringUtils.arrayToCommaDelimitedString(container.getQueueNames()));
            consumerInfo.setRunning(container.isRunning());
            list.add(consumerInfo);
        }
        return list;
    }

    private AbstractMessageListenerContainer getContainerByName(String consumerName) {
        if (null == containers || containers.length == 0 || null == consumerName || consumerName.isEmpty()) {
            return null;
        }
        for (AbstractMessageListenerContainer container : containers) {
            if (container.getListenerId().equals(consumerName)) {
                return container;
            }
        }
        return null;
    }

    @PostMapping("/{name}/start")
    public boolean start(@PathVariable("name") String name) {
        AbstractMessageListenerContainer container = getContainerByName(name);
        if (null == container) {
            return false;
        }
        if (!container.isRunning()) {
            log.debug("消费者:{} start", name);
            container.start();
            return container.isRunning();
        }
        return false;
    }

    @PostMapping("/{name}/stop")
    public boolean stop(@PathVariable("name") String name) {
        AbstractMessageListenerContainer container = getContainerByName(name);
        if (null == container) {
            return false;
        }
        if (container.isActive() && container.isRunning()) {
            log.debug("消费者:{} stop", name);
            container.stop();
            return !container.isRunning();
        }
        return false;
    }
}
