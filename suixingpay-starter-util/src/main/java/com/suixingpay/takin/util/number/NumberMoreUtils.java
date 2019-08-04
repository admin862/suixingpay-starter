/**
 * All rights Reserved, Designed By Suixingpay.
 * 
 * @author: matieli<ma_tl@suixingpay.com>
 * @date: 2017年3月8日 下午1:25:06
 * @Copyright ©2017 Suixingpay. All rights reserved. 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.util.number;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

/**
 * 数字的工具类.
 * <p>
 * 1.原始类型数字与byte[]的双向转换(via Guava)
 * </p>
 * <p>
 * 2.判断字符串是否数字, 是否16进制字符串(via Common Lang)
 * </p>
 * <p>
 * 3.10机制/16进制字符串 与 原始类型数字/数字对象 的双向转换(参考Common Lang自写)
 * </p>
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2017年3月8日 下午1:46:37
 * @version: V1.0
 */
public abstract class NumberMoreUtils {

    /**
     * 
     * int 转byte[]
     * 
     * @param value
     * @return
     */
    public static byte[] toBytes(int value) {
        return Ints.toByteArray(value);
    }

    /**
     * long 转byte[]
     * 
     * @param value
     * @return
     */
    public static byte[] toBytes(long value) {
        return Longs.toByteArray(value);
    }

    /**
     * from ElasticSearch Numbers
     * 
     * @param val
     * @return
     */
    public static byte[] toBytes(double val) {
        return toBytes(Double.doubleToRawLongBits(val));
    }

    /**
     * byte[] 转int
     * 
     * @param bytes
     * @return
     */
    public static int toInt(byte[] bytes) {
        return Ints.fromByteArray(bytes);
    }

    /**
     * byte[] 转int
     * 
     * @param bytes
     * @return
     */
    public static long toLong(byte[] bytes) {
        return Longs.fromByteArray(bytes);
    }

    /**
     * from ElasticSearch Numbers
     * 
     * @param bytes
     * @return
     */
    public static double toDouble(byte[] bytes) {
        return Double.longBitsToDouble(toLong(bytes));
    }

    /////// 判断字符串类型//////////
    /**
     * 判断字符串是否合法数字
     * 
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        return NumberUtils.isCreatable(str);
    }

    /**
     * 判断字符串是否16进制
     * 
     * @param value
     * @return
     */
    public static boolean isHexNumber(String value) {
        int index = value.startsWith("-") ? 1 : 0;
        return value.startsWith("0x", index) || value.startsWith("0X", index)
                || value.startsWith("#", index);
    }

    /////////// 将字符串安全的转化为原始类型数字/////////

    /**
     * 将10进制的String安全的转化为int，当str为空或非数字字符串时，返回0
     * 
     * @param str
     * @return
     */
    public static int toInt(String str) {
        return NumberUtils.toInt(str, 0);
    }

    /**
     * 将10进制的String安全的转化为int，当str为空或非数字字符串时，返回default值
     * 
     * @param str
     * @param defaultValue
     * @return
     */
    public static int toInt(String str, int defaultValue) {
        return NumberUtils.toInt(str, defaultValue);
    }

    /**
     * 将10进制的String安全的转化为long，当str为空或非数字字符串时，返回0
     * 
     * @param str
     * @return
     */
    public static long toLong(String str) {
        return NumberUtils.toLong(str, 0L);
    }

    /**
     * 将10进制的String安全的转化为long，当str为空或非数字字符串时，返回default值
     * 
     * @param str
     * @param defaultValue
     * @return
     */
    public static long toLong(String str, long defaultValue) {
        return NumberUtils.toLong(str, defaultValue);
    }

    /**
     * 将10进制的String安全的转化为double，当str为空或非数字字符串时，返回0
     * 
     * @param str
     * @return
     */
    public static double toDouble(String str) {
        return NumberUtils.toDouble(str, 0L);
    }

    /**
     * 将10进制的String安全的转化为double，当str为空或非数字字符串时，返回default值
     * 
     * @param str
     * @param defaultValue
     * @return
     */
    public static double toDouble(String str, double defaultValue) {
        return NumberUtils.toDouble(str, defaultValue);
    }

    ////////////// 10进制字符串 转换对象类型数字/////////////
    /**
     * 将10进制的String安全的转化为Integer，当str为空或非数字字符串时，返回null
     * 
     * @param str
     * @return
     */
    public static Integer toIntObject(String str) {
        return toIntObject(str, null);
    }

    /**
     * 将10进制的String安全的转化为Integer，当str为空或非数字字符串时，返回default值
     * 
     * @param str
     * @param defaultValue
     * @return
     */
    public static Integer toIntObject(String str, Integer defaultValue) {
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * 将10进制的String安全的转化为Long，当str为空或非数字字符串时，返回null
     * 
     * @param str
     * @return
     */
    public static Long toLongObject(String str) {
        return toLongObject(str, null);
    }

    /**
     * 将10进制的String安全的转化为Long，当str为空或非数字字符串时，返回default值
     * 
     * @param str
     * @param defaultValue
     * @return
     */
    public static Long toLongObject(String str, Long defaultValue) {
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        try {
            return Long.valueOf(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * 将10进制的String安全的转化为Double，当str为空或非数字字符串时，返回null
     * 
     * @param str
     * @return
     */
    public static Double toDoubleObject(String str) {
        return toDoubleObject(str, null);
    }

    /**
     * 将10进制的String安全的转化为Long，当str为空或非数字字符串时，返回default值
     * 
     * @param str
     * @param defaultValue
     * @return
     */
    public static Double toDoubleObject(String str, Double defaultValue) {
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        try {
            return Double.valueOf(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    //////////// 16进制 字符串转换为数字对象//////////

    /**
     * 将16进制的String转化为Integer，出错时返回null.
     * 
     * @param str
     * @return
     */
    public static Integer hexToIntObject(String str) {
        return hexToIntObject(str, null);
    }

    /**
     * 将16进制的String转化为Integer，出错时返回默认值.
     * 
     * @param str
     * @param defaultValue
     * @return
     */
    public static Integer hexToIntObject(String str, Integer defaultValue) {
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        try {
            return Integer.decode(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * 将16进制的String转化为Long，出错时返回null.
     * 
     * @param str
     * @return
     */
    public static Long hexToLongObject(String str) {
        return hexToLongObject(str, null);
    }

    /**
     * 将16进制的String转化为Long，出错时返回默认值.
     * 
     * @param str
     * @param defaultValue
     * @return
     */
    public static Long hexToLongObject(String str, Long defaultValue) {
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        try {
            return Long.decode(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /////// toString (定义了原子类型与对象类型的参数，保证不会用错函数) ///////

    /**
     * 
     * int 转成String
     * 
     * @param i
     * @return
     */
    public static String toString(int i) {
        return Integer.toString(i);
    }

    /**
     * Integer 转成String
     * 
     * @param i
     * @return
     */
    public static String toString(Integer i) {
        return i.toString();
    }

    /**
     * long 转成String
     * 
     * @param l
     * @return
     */
    public static String toString(long l) {
        return Long.toString(l);
    }

    /**
     * 
     * Long 转String
     * 
     * @param l
     * @return
     */

    public static String toString(Long l) {
        return l.toString();
    }

    /**
     * 
     * double 转String
     * 
     * @param d
     * @return
     */
    public static String toString(double d) {
        return Double.toString(d);
    }

    /**
     * Double 转String
     * 
     * @param d
     * @return
     */
    public static String toString(Double d) {
        return d.toString();
    }

    /**
     * 
     * 输出格式化为小数后两位的double字符串
     * 
     * @param d
     * @return
     */
    public static String to2DigitString(double d) {
        return String.format(Locale.ROOT, "%.2f", d);
    }

    /////////// 杂项 ///////

    /**
     * 安全的将小于Integer.MAX的long转为int，否则抛出错误
     * 
     * @param x
     * @return
     * @throws IllegalArgumentException
     */
    public static int toInt32(long x) throws IllegalArgumentException {
        if ((int) x == x) {
            return (int) x;
        }
        throw new IllegalArgumentException("Int " + x + " out of range");
    }
}
