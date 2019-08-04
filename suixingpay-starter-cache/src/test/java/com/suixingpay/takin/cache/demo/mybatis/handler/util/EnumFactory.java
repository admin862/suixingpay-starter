/**
 * 
 */
package com.suixingpay.takin.cache.demo.mybatis.handler.util;

import java.util.EnumSet;

import com.suixingpay.takin.cache.demo.mybatis.handler.Identifiable;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月31日 下午1:34:59
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月31日 下午1:34:59
 */
public class EnumFactory {

    public static <T extends Enum<T> & Identifiable<K>, K> T getEnum(Class<T> type, K id) {
        T[] arr = type.getEnumConstants();
        if (arr == null || arr.length <= 0) {
            return null;
        }
        for (T t : arr) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }

    public static <T extends Enum<T> & Identifiable<K>, K> T get(Class<T> type, K id) {
        EnumSet<T> set = EnumSet.allOf(type);
        if (set == null || set.size() <= 0) {
            return null;
        }
        for (T t : set) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }
}
