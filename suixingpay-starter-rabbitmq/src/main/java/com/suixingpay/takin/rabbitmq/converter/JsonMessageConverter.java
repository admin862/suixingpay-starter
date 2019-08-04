/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年3月7日 上午8:48:28   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.rabbitmq.converter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * MQ是解耦工具，消息经常会跨系统投递，在这样的场景下，使用JSON是最好的。
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年3月7日 上午8:48:28
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年3月7日 上午8:48:28
 */
public class JsonMessageConverter extends AbstractMessageConverter {

    private static final Logger logger = LoggerFactory.getLogger(JsonMessageConverter.class);

    public static final String DEFAULT_CHARSET = "UTF-8";

    private static final SimpleMessageConverter SIMPLE_MESSAGE_CONVERTER = new SimpleMessageConverter();

    private static final ObjectMapper jsonObjectMapper = new ObjectMapper();

    static {
        // 忽略大小写
        jsonObjectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        // 该特性决定了当遇到未知属性（没有映射到属性，没有任何setter或者任何可以处理它的handler），是否应该抛出一个JsonMappingException异常。这个特性一般式所有其他处理方法对未知属性处理都无效后才被尝试，属性保留未处理状态。默认情况下，该设置是被打开的。
        // 但在这里需要将它关闭
        jsonObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private final Type type;

    public JsonMessageConverter(Type type) {
        super();
        this.type = type;
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        Object content = null;
        MessageProperties properties = message.getMessageProperties();
        String contentType = null;
        if (properties != null) {
            contentType = properties.getContentType();
        }
        if (contentType != null) {
            if (contentType.equals(MessageProperties.CONTENT_TYPE_JSON)) {
                try {
                    String json = new String(message.getBody(), DEFAULT_CHARSET);
                    if (logger.isTraceEnabled()) {
                        logger.trace("the json fromMessage:{}", json);
                    }
                    content = jsonToObject(json);
                } catch (Exception e) {
                    throw new MessageConversionException("failed to convert serialized Message content", e);
                }
            }
        }
        if (content == null) {
            // 为了兼容老框架数据类型问题
            content = SIMPLE_MESSAGE_CONVERTER.fromMessage(message);
        }
        if (content == null) {
            content = message.getBody();
        }
        return content;
    }

    /**
     * 获取JSON及字符串类型的消息内容
     * 
     * @param message
     * @return
     */
    public static String getMessageContent(Message message) {
        String content = "不是字符串类型消息";
        MessageProperties properties = message.getMessageProperties();
        String contentType = null;
        if (properties != null) {
            contentType = properties.getContentType();
        }
        if (contentType != null) {
            if (contentType.equals(MessageProperties.CONTENT_TYPE_JSON)) {
                try {
                    content = new String(message.getBody(), DEFAULT_CHARSET);
                } catch (UnsupportedEncodingException e) {
                }
            } else if (contentType.startsWith("text")) {
                String encoding = properties.getContentEncoding();
                if (encoding == null) {
                    encoding = DEFAULT_CHARSET;
                }
                try {
                    content = new String(message.getBody(), encoding);
                } catch (UnsupportedEncodingException e) {
                }
            }
        }
        return content;
    }

    /**
     * 将JSON字符串转化为 指定类型
     * 
     * @param json json
     * @return Object 结果
     */
    public Object jsonToObject(String json) throws Exception {
        Object object = null;
        if (json != null && json.trim().length() > 0) {
            if (null == type) {
                throw new Exception(" type is null");
            }
            JavaType javaType = jsonObjectMapper.getTypeFactory().constructType(type);
            object = jsonObjectMapper.readValue(json, javaType);
        }
        return object;
    }

    @Override
    protected Message createMessage(Object object, MessageProperties messageProperties)
            throws MessageConversionException {
        byte[] bytes = null;
        if (object instanceof byte[]) {
            bytes = (byte[]) object;
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_BYTES);
        } else if (object instanceof String) {
            try {
                bytes = ((String) object).getBytes(DEFAULT_CHARSET);
            } catch (UnsupportedEncodingException e) {
                throw new MessageConversionException("failed to convert to Message content", e);
            }
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
            messageProperties.setContentEncoding(DEFAULT_CHARSET);
        } else {
            String contentType = messageProperties.getContentType();
            // 如果外部没有改变contentType，默认使用JSON
            if (null == contentType || MessageProperties.CONTENT_TYPE_JSON.equals(contentType)
                    || MessageProperties.DEFAULT_CONTENT_TYPE.equals(contentType)) {
                try {
                    String json = jsonObjectMapper.writeValueAsString(object);
                    bytes = json.getBytes(DEFAULT_CHARSET);
                    messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
                    messageProperties.setContentEncoding(DEFAULT_CHARSET);
                } catch (Exception e) {
                    throw new MessageConversionException("failed to convert to serialized Message content", e);
                }
            } else {
                // 为了兼容老框架数据类型问题
                return SIMPLE_MESSAGE_CONVERTER.toMessage(object, messageProperties);
            }
        }
        if (bytes != null) {
            messageProperties.setContentLength(bytes.length);
        }
        return new Message(bytes, messageProperties);

    }

}
