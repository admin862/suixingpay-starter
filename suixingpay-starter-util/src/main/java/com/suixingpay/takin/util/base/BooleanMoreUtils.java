/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli<ma_tl@suixingpay.com> 
 * @date: 2017年3月8日 下午1:25:06   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.util.base;

import org.apache.commons.lang3.BooleanUtils;

/**
 * BooleanUtils
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2017年3月8日 下午2:03:49
 * @version: V1.0
 */
public abstract class BooleanMoreUtils {
    /**
     * 使用标准JDK，只分析是否忽略大小写的"true", 为空时返回false
     * 
     * @param str 字符串
     * @return boolean true or false
     */
    public static boolean toBoolean(String str) {
        return Boolean.parseBoolean(str);
    }

    /**
     * 使用标准JDK，只分析是否忽略大小写的"true", 为空时返回null
     * 
     * @param str 字符串
     * @return Boolean true or false or null
     */

    public static Boolean toBooleanObject(String str) {
        return str != null ? Boolean.valueOf(str) : null;
    }

    /**
     * 使用标准JDK，只分析是否忽略大小写的"true", 为空时返回defaultValue
     * 
     * @param str 字符串
     * @param defaultValue 默认值
     * @return Boolean true or false or defaultValue
     */
    public static Boolean toBooleanObject(String str, Boolean defaultValue) {
        return str != null ? Boolean.valueOf(str) : defaultValue;
    }

    /**
     * 支持true/false,on/off, y/n, yes/no的转换, str为空或无法分析时返回null
     * 
     * @param str 字符串
     * @return Boolean true or false or null
     */
    public static Boolean parseGeneralString(String str) {
        return BooleanUtils.toBooleanObject(str);
    }

    /**
     * 支持true/false,on/off, y/n, yes/no的转换, str为空或无法分析时返回defaultValue
     * 
     * @param str 字符串
     * @param defaultValue 默认值
     * @return Boolean true or false or defaultValue
     */
    public static Boolean parseGeneralString(String str, Boolean defaultValue) {
        return BooleanUtils.toBooleanDefaultIfNull(BooleanUtils.toBooleanObject(str), defaultValue);
    }

    /**
     * 取反
     * 
     * @param bool 原值
     * @return boolean 取反后的值
     */
    public static boolean negate(final boolean bool) {
        return !bool;
    }

    /**
     * 取反
     * 
     * @param bool 原值
     * @return boolean 取反后的值
     */
    public static Boolean negate(final Boolean bool) {
        return BooleanUtils.negate(bool);
    }

    /**
     * 多个值的and
     * 
     * <pre>
     *   BooleanMoreUtils.and(true, true)         = true
     *   BooleanMoreUtils.and(false, false)       = false
     *   BooleanMoreUtils.and(true, false)        = false
     *   BooleanMoreUtils.and(true, true, false)  = false
     *   BooleanMoreUtils.and(true, true, true)   = true
     * </pre>
     * 
     * @param array boolean[]
     * @return boolean true or false
     */
    public static boolean and(final boolean... array) {
        return BooleanUtils.and(array);
    }

    /**
     * 多个值的or
     * 
     * <pre>
     *   BooleanMoreUtils.or(true, true)          = true
     *   BooleanMoreUtils.or(false, false)        = false
     *   BooleanMoreUtils.or(true, false)         = true
     *   BooleanMoreUtils.or(true, true, false)   = true
     *   BooleanMoreUtils.or(true, true, true)    = true
     *   BooleanMoreUtils.or(false, false, false) = false
     * </pre>
     * 
     * @param array boolean[]
     * @return boolean true or false
     */
    public static boolean or(final boolean... array) {
        return BooleanUtils.or(array);
    }

    /**
     * 比较两个boolean值
     * 
     * @param x 第一个值
     * @param y 第二个值
     * @return if {x=y} return 0; if {!x && y} return -1;if {x && !y} return 1;
     */
    public static int compare(boolean x, boolean y) {
        return BooleanUtils.compare(x, y);
    }

}
