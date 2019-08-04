/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli[ma_tl@suixingpay.com] 
 * @date: 2017年5月31日 下午7:57:27   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.util.base;

import com.suixingpay.takin.util.text.StringMoreUtils;

/**
 * ObjectUtils
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2017年5月31日 下午7:57:27
 * @version: V1.0
 * @review: matieli[ma_tl@suixingpay.com]/2017年5月31日 下午7:57:27
 */
public abstract class ObjectUtils {
    public static long toLong(Object object, long defaultValue) {
        return object instanceof Number ? ((Number) object).longValue()
                : (isInt(object) ? Long.parseLong(object.toString())
                        : (isDouble(object) ? (long) Double.parseDouble(object.toString()) : defaultValue));
    }

    public static long toLong(Object object) {
        return toLong(object, 0L);
    }

    public static int toInt(Object object, int defaultValue) {
        return object instanceof Number ? ((Number) object).intValue()
                : (isInt(object) ? Integer.parseInt(object.toString())
                        : (isDouble(object) ? (int) Double.parseDouble(object.toString()) : defaultValue));
    }

    public static int toInt(Object object) {
        return toInt(object, 0);
    }

    public static double toDouble(Object object, double defaultValue) {
        return object instanceof Number ? ((Number) object).doubleValue()
                : (isNumber(object) ? Double.parseDouble(object.toString()) : (null == object ? defaultValue : 0.0D));
    }

    public static double toDouble(Object object) {
        return toDouble(object, 0.0D);
    }

    public static boolean isTrue(Object obj) {
        return "true".equals(String.valueOf(obj));
    }

    public static boolean isInt(Object obj) {
        return isNullOrEmpty(obj) ? false : (obj instanceof Integer ? true : obj.toString().matches("[-+]?\\d+"));
    }

    public static boolean isDouble(Object obj) {
        return isNullOrEmpty(obj) ? false
                : (!(obj instanceof Double) && !(obj instanceof Float)
                        ? StringMoreUtils.compileRegex("[-+]?\\d+\\.\\d+").matcher(obj.toString()).matches() : true);
    }

    public static boolean isNumber(Object obj) {
        return obj instanceof Number ? true : isInt(obj) || isDouble(obj);
    }

    public static boolean isNullOrEmpty(Object obj) {
        return obj == null || "".equals(obj.toString());
    }

}
