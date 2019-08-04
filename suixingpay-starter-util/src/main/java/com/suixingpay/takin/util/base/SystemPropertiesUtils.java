/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli<ma_tl@suixingpay.com> 
 * @date: 2017年3月8日 下午1:25:06   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.util.base;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import com.suixingpay.takin.util.number.NumberMoreUtils;

/**
 * 关于系统Properties的工具类
 * <p>
 * 1. 统一的读取系统变量，其中Boolean.readBoolean的风格不统一，Double则不支持，都进行了扩展.
 * </p>
 * <p>
 * 2. 简单的合并系统变量(-D)，环境变量 和默认值，以系统变量优先，在未引入Commons Config时使用.
 * </p>
 * <p>
 * 3. Properties
 * 本质上是一个HashTable，每次读写都会加锁，所以不支持频繁的System.getProperty(name)来检查系统内容变化
 * 因此扩展了一个ListenableProperties, 在其所关心的属性变化时进行通知.
 * </p>
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2017年3月8日 下午1:59:57
 * @version: V1.0
 */
public class SystemPropertiesUtils {
    /**
     * 读取Boolean类型的系统变量，为空时返回null，代表未设置，而不是Boolean.getBoolean()的false.
     * 
     * @param name 获取的属性名
     * @return true or false or null
     */
    public static Boolean getBoolean(String name) {
        String stringResult = System.getProperty(name);
        return BooleanMoreUtils.toBooleanObject(stringResult);
    }

    /**
     * 读取Boolean类型的系统变量，为空时返回默认值, 而不是Boolean.getBoolean()的false.
     * 
     * @param name 获取的属性名
     * @param defaultValue 默认值
     * @return true or false or null
     */
    public static Boolean getBoolean(String name, Boolean defaultValue) {
        String stringResult = System.getProperty(name);
        return BooleanMoreUtils.toBooleanObject(stringResult, defaultValue);
    }

    /**
     * 读取String类型的系统变量，为空时返回null.
     * 
     * @param name 获取的属性名
     * @return String
     */
    public static String getString(String name) {
        return System.getProperty(name);
    }

    /**
     * 读取String类型的系统变量，为空时返回默认值
     * 
     * @param name 获取的属性名
     * @param defaultValue 默认值
     * @return String
     */
    public static String getString(String name, String defaultValue) {
        return System.getProperty(name, defaultValue);
    }

    /**
     * 读取Integer类型的系统变量，为空时返回null.
     * 
     * @param name 获取的属性名
     * @return Integer
     */
    public static Integer getInteger(String name) {
        return Integer.getInteger(name);
    }

    /**
     * 读取Integer类型的系统变量，为空时返回默认值.
     * 
     * @param name 获取的属性名
     * @param defaultValue 默认值
     * @return Integer
     */
    public static Integer getInteger(String name, Integer defaultValue) {
        return Integer.getInteger(name, defaultValue);
    }

    /**
     * 读取Long类型的系统变量，为空时返回null.
     * 
     * @param name 获取的属性名
     * @return Long
     */
    public static Long getLong(String name) {
        return Long.getLong(name);
    }

    /**
     * 读取Integer类型的系统变量，为空时返回默认值.
     * 
     * @param name 获取的属性名
     * @param defaultValue 默认值
     * @return Long
     */
    public static Long getLong(String name, Long defaultValue) {
        return Long.getLong(name, defaultValue);
    }

    /**
     * 读取Double类型的系统变量，为空时返回null.
     * 
     * @param name 获取的属性名
     * @return Double
     */
    public static Double getDouble(String name) {
        return NumberMoreUtils.toDoubleObject(System.getProperty(name));
    }

    /**
     * 读取Double类型的系统变量，为空时返回默认值.
     * 
     * @param name 获取的属性名
     * @param defaultValue
     * @return Double
     */
    public static Double getDouble(String name, Double defaultValue) {
        Double propertyValue = NumberMoreUtils.toDoubleObject(System.getProperty(name));
        return propertyValue != null ? propertyValue : defaultValue;
    }

    /**
     * 合并系统变量(-D)，环境变量 和默认值，以系统变量优先
     * 
     * @param propertyName 系统变量
     * @param envName 环境变量
     * @param defaultValue 默认值
     * @return String
     */
    public static String getString(String propertyName, String envName, String defaultValue) {
        checkEnvName(envName);
        String propertyValue = System.getProperty(propertyName);
        if (propertyValue != null) {
            return propertyValue;
        } else {
            propertyValue = System.getenv(envName);
            return propertyValue != null ? propertyValue : defaultValue;
        }
    }

    /**
     * 合并系统变量(-D)，环境变量 和默认值，以系统变量优先
     * 
     * @param propertyName 系统变量
     * @param envName 环境变量
     * @param defaultValue 默认值
     * @return Integer
     */
    public static Integer getInteger(String propertyName, String envName, Integer defaultValue) {
        checkEnvName(envName);
        Integer propertyValue = NumberMoreUtils.toIntObject(System.getProperty(propertyName));
        if (propertyValue != null) {
            return propertyValue;
        } else {
            propertyValue = NumberMoreUtils.toIntObject(System.getenv(envName));
            return propertyValue != null ? propertyValue : defaultValue;
        }
    }

    /**
     * 合并系统变量(-D)，环境变量 和默认值，以系统变量优先
     * 
     * @param propertyName 系统变量
     * @param envName 环境变量
     * @param defaultValue 默认值
     * @return Long
     */
    public static Long getLong(String propertyName, String envName, Long defaultValue) {
        checkEnvName(envName);
        Long propertyValue = NumberMoreUtils.toLongObject(System.getProperty(propertyName));
        if (propertyValue != null) {
            return propertyValue;
        } else {
            propertyValue = NumberMoreUtils.toLongObject(System.getenv(envName));
            return propertyValue != null ? propertyValue : defaultValue;
        }
    }

    /**
     * 合并系统变量(-D)，环境变量 和默认值，以系统变量优先
     * 
     * @param propertyName 系统变量
     * @param envName 环境变量
     * @param defaultValue 默认值
     * @return Double
     */
    public static Double getDouble(String propertyName, String envName, Double defaultValue) {
        checkEnvName(envName);
        Double propertyValue = NumberMoreUtils.toDoubleObject(System.getProperty(propertyName));
        if (propertyValue != null) {
            return propertyValue;
        } else {
            propertyValue = NumberMoreUtils.toDoubleObject(System.getenv(envName));
            return propertyValue != null ? propertyValue : defaultValue;
        }
    }

    /**
     * 合并系统变量(-D)，环境变量 和默认值，以系统变量优先
     * 
     * @param propertyName 系统变量
     * @param envName 环境变量
     * @param defaultValue 默认值
     * @return Boolean
     */
    public static Boolean getBoolean(String propertyName, String envName, Boolean defaultValue) {
        checkEnvName(envName);
        Boolean propertyValue = BooleanMoreUtils.toBooleanObject(System.getProperty(propertyName), null);
        if (propertyValue != null) {
            return propertyValue;
        } else {
            propertyValue = BooleanMoreUtils.toBooleanObject(System.getenv(envName), null);
            return propertyValue != null ? propertyValue : defaultValue;
        }
    }

    /**
     * Properties
     * 本质上是一个HashTable，每次读写都会加锁，所以不支持频繁的System.getProperty(name)来检查系统内容变化
     * 因此扩展了一个ListenableProperties, 在其所关心的属性变化时进行通知.
     *
     * @see ListenableProperties
     */
    public static synchronized void registerSystemPropertiesListener(PropertiesListener listener) {
        Properties currentProperties = System.getProperties();

        if (!(currentProperties instanceof ListenableProperties)) {
            ListenableProperties newProperties = new ListenableProperties(currentProperties);
            System.setProperties(newProperties);
            currentProperties = newProperties;
        }

        ((ListenableProperties) currentProperties).register(listener);
    }

    /**
     * 检查环境变量名不能有'.'，在linux下不支持
     * 
     * @param envName 环境变量
     */
    private static void checkEnvName(String envName) {
        if (envName == null || envName.indexOf('.') != -1) {
            throw new IllegalArgumentException("envName " + envName + " has dot which is not valid");
        }
    }

    /**
     * Properties
     * 本质上是一个HashTable，每次读写都会加锁，所以不支持频繁的System.getProperty(name)来检查系统内容变化
     * 因此扩展了一个ListenableProperties, 在其所关心的属性变化时进行通知.
     *
     * @see PropertiesListener
     */
    static class ListenableProperties extends Properties {

        private static final long serialVersionUID = -8282465702074684324L;

        private List<PropertiesListener> listeners = new CopyOnWriteArrayList<PropertiesListener>();

        ListenableProperties(Properties properties) {
            super(properties);
        }

        /**
         * @return the listeners
         */
        List<PropertiesListener> getListeners() {
            return listeners;
        }

        /**
         * @param listeners the listeners to set
         */
        void setListeners(List<PropertiesListener> listeners) {
            this.listeners = listeners;
        }

        /**
         * register
         * 
         * @param listener
         */
        void register(PropertiesListener listener) {
            listeners.add(listener);
        }

        @Override
        public synchronized Object setProperty(String key, String value) {
            Object result = put(key, value);
            for (PropertiesListener listener : listeners) {
                if (listener.propertyName.equals(key)) {
                    listener.onChange(key, value);
                }
            }
            return result;
        }
    }

    /**
     * 获取所关心的Properties变更的Listener基类.
     */
    abstract static class PropertiesListener {

        private String propertyName;

        PropertiesListener(String propertyName) {
            this.propertyName = propertyName;
        }

        /**
         * @return the propertyName
         */
        String getPropertyName() {
            return propertyName;
        }

        /**
         * @param propertyName the propertyName to set
         */
        void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        /**
         * onChange
         * 
         * @param propertyName
         * @param value
         */
        abstract void onChange(String propertyName, String value);
    }
}
