/**
 * All rights Reserved, Designed By Suixingpay.
 * 
 * @author: matieli<ma_tl@suixingpay.com>
 * @date: 2017年3月8日 下午1:25:06
 * @Copyright ©2017 Suixingpay. All rights reserved. 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.util.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.suixingpay.takin.util.io.GeneralResourceUtils;
import com.suixingpay.takin.util.io.IOUtils;
import com.suixingpay.takin.util.number.NumberMoreUtils;

/**
 * 关于Properties的工具类 1. 统一读取Properties 2. 从文件或字符串装载Properties
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2017年3月8日 下午2:01:45
 * @version: V1.0
 */
public abstract class PropertiesUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);

    /**
     * 配置文件中读取值，若不存在返回默认值
     * 
     * @param p Properties对象
     * @param name 属性值
     * @param defaultValue 默认值
     * @return Properties对象中取到的属性的值
     */
    public static Boolean getBoolean(Properties p, String name, Boolean defaultValue) {
        return BooleanMoreUtils.toBooleanObject(p.getProperty(name), defaultValue);
    }

    /**
     * 配置文件中读取值，若不存在返回默认值
     * 
     * @param p Properties对象
     * @param name 属性值
     * @param defaultValue 默认值
     * @return Properties对象中取到的属性的值
     */
    public static Integer getInt(Properties p, String name, Integer defaultValue) {
        return NumberMoreUtils.toIntObject(p.getProperty(name), defaultValue);
    }

    /**
     * 配置文件中读取值，若不存在返回默认值
     * 
     * @param p Properties对象
     * @param name 属性值
     * @param defaultValue 默认值
     * @return Properties对象中取到的属性的值
     */
    public static Long getLong(Properties p, String name, Long defaultValue) {
        return NumberMoreUtils.toLongObject(p.getProperty(name), defaultValue);
    }

    /**
     * 配置文件中读取值，若不存在返回默认值
     * 
     * @param p Properties对象
     * @param name 属性值
     * @param defaultValue 默认值
     * @return Properties对象中取到的属性的值
     */
    public static Double getDouble(Properties p, String name, Double defaultValue) {
        return NumberMoreUtils.toDoubleObject(p.getProperty(name), defaultValue);
    }

    /**
     * 配置文件中读取值，若不存在返回默认值
     * 
     * @param p Properties对象
     * @param name 属性值
     * @param defaultValue 默认值
     * @return Properties对象中取到的属性的值
     */
    public static String getString(Properties p, String name, String defaultValue) {
        return p.getProperty(name, defaultValue);
    }

    /**
     * 从文件路径加载properties. 路径支持从外部文件或resources文件加载, "file://"代表外部文件, "classpath://"代表resources,
     * 
     * @param generalPath 路径
     * @return Properties对象
     */
    public static Properties loadFromFile(String generalPath) {
        Properties p = new Properties();
        InputStream is = null;
        try {
            is = GeneralResourceUtils.asStream(generalPath);
            p.load(is);
        } catch (IOException e) {
            LOGGER.warn("Load property from " + generalPath + " fail ", e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return p;
    }

    /**
     * 从字符串内容加载Properties
     * 
     * @param content 内容
     * @return Properties
     */
    public static Properties loadFromString(String content) {
        Properties p = new Properties();
        Reader reader = new StringReader(content);
        try {
            p.load(reader);
        } catch (IOException ignored) {
        } finally {
            IOUtils.closeQuietly(reader);
        }

        return p;
    }
}
