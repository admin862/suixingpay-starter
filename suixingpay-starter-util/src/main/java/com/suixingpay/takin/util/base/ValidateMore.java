/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli<ma_tl@suixingpay.com> 
 * @date: 2017年3月8日 下午1:25:06   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.util.base;

import java.util.regex.Pattern;

import com.suixingpay.takin.util.text.StringMoreUtils;

/**
 * 参数校验统一使用Apache Common Lange Validate, 补充一些缺少的.
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2017年3月8日 下午1:42:40
 * @version: V1.0
 */
public class ValidateMore {
    /**
     * 正则：手机号（简单）, 1字头＋10位数字即可.
     */
    private static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";
    private static final Pattern PATTERN_REGEX_MOBILE_SIMPLE = Pattern.compile(REGEX_MOBILE_SIMPLE);

    /**
     * 正则：手机号（精确）, 已知3位前缀＋8位数字
     * <p>
     * 移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188
     * </p>
     * <p>
     * 联通：130、131、132、145、155、156、175、176、185、186
     * </p>
     * <p>
     * 电信：133、153、173、177、180、181、189
     * </p>
     * <p>
     * 全球星：1349
     * </p>
     * <p>
     * 虚拟运营商：170
     * </p>
     */
    private static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$";
    private static final Pattern PATTERN_REGEX_MOBILE_EXACT = Pattern.compile(REGEX_MOBILE_EXACT);

    /**
     * 正则：固定电话号码，可带区号，然后6至少8位数字
     */
    private static final String REGEX_TEL = "^(\\d{3,4}-)?\\d{6,8}$";
    private static final Pattern PATTERN_REGEX_TEL = Pattern.compile(REGEX_TEL);

    /**
     * 正则：身份证号码15位, 数字且关于生日的部分必须正确
     */
    private static final String REGEX_ID_CARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    private static final Pattern PATTERN_REGEX_ID_CARD15 = Pattern.compile(REGEX_ID_CARD15);

    /**
     * 正则：身份证号码18位, 数字且关于生日的部分必须正确
     */
    private static final String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
    private static final Pattern PATTERN_REGEX_ID_CARD18 = Pattern.compile(REGEX_ID_CARD18);

    /**
     * 正则：邮箱, 有效字符(不支持中文), 且中间必须有@，后半部分必须有.
     */
    private static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    private static final Pattern PATTERN_REGEX_EMAIL = Pattern.compile(REGEX_EMAIL);

    /**
     * 正则：URL, 必须有"://",前面必须是英文，后面不能有空格
     */
    private static final String REGEX_URL = "[a-zA-z]+://[^\\s]*";
    private static final Pattern PATTERN_REGEX_URL = Pattern.compile(REGEX_URL);

    /**
     * 正则：yyyy-MM-dd格式的日期校验，已考虑平闰年
     */
    private static final String REGEX_DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";
    private static final Pattern PATTERN_REGEX_DATE = Pattern.compile(REGEX_DATE);

    /**
     * 正则：IP地址
     */
    private static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
    private static final Pattern PATTERN_REGEX_IP = Pattern.compile(REGEX_IP);

    /**
     * 验证手机号（简单）
     * 
     * @param input
     * @return boolean
     */
    public static boolean isMobileSimple(CharSequence input) {
        return isMatch(PATTERN_REGEX_MOBILE_SIMPLE, input);
    }

    /**
     * 验证手机号（精确）
     * 
     * @param input
     * @return boolean
     */
    public static boolean isMobileExact(CharSequence input) {
        return isMatch(PATTERN_REGEX_MOBILE_EXACT, input);
    }

    /**
     * 验证固定电话号码
     * 
     * @param input
     * @return boolean
     */
    public static boolean isTel(CharSequence input) {
        return isMatch(PATTERN_REGEX_TEL, input);
    }

    /**
     * 验证15或18位身份证号码
     * 
     * @param input
     * @return boolean
     */
    public static boolean isIdCard(CharSequence input) {
        return isMatch(PATTERN_REGEX_ID_CARD15, input) || isMatch(PATTERN_REGEX_ID_CARD18, input);
    }

    /**
     * 验证邮箱
     * 
     * @param input
     * @return boolean
     */
    public static boolean isEmail(CharSequence input) {
        return isMatch(PATTERN_REGEX_EMAIL, input);
    }

    /**
     * 验证URL
     * 
     * @param input
     * @return boolean
     */
    public static boolean isUrl(CharSequence input) {
        return isMatch(PATTERN_REGEX_URL, input);
    }

    /**
     * 验证yyyy-MM-dd格式的日期校验，已考虑平闰年
     * 
     * @param input
     * @return boolean
     */
    public static boolean isDate(CharSequence input) {
        return isMatch(PATTERN_REGEX_DATE, input);
    }

    /**
     * 验证IP地址
     * 
     * @param input
     * @return boolean
     */
    public static boolean isIp(CharSequence input) {
        return isMatch(PATTERN_REGEX_IP, input);
    }

    /**
     * 字符匹配正则
     * 
     * @param pattern
     * @param input
     * @return
     */
    public static boolean isMatch(Pattern pattern, CharSequence input) {
        return StringMoreUtils.isNotEmpty(input) && pattern.matcher(input).matches();
    }

    /**
     * 校验为正数则返回该数字，否则抛出异常.
     * 
     * @param role
     * @param x
     * @return int
     */
    public static int nonNegative(String role, int x) {
        if (x < 0) {
            throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
        }
        return x;
    }

    /**
     * 校验为正数则返回该数字，否则抛出异常.
     * 
     * @param role
     * @param x
     * @return Integer
     */
    public static Integer nonNegative(String role, Integer x) {
        if (x.intValue() < 0) {
            throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
        }
        return x;
    }

    /**
     * 校验为正数则返回该数字，否则抛出异常.
     * 
     * @param role
     * @param x
     * @return long
     */
    public static long nonNegative(String role, long x) {
        if (x < 0) {
            throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
        }
        return x;
    }

    /**
     * 校验为正数则返回该数字，否则抛出异常.
     * 
     * @param role
     * @param x
     * @return Long
     */
    public static Long nonNegative(String role, Long x) {
        if (x.longValue() < 0) {
            throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
        }
        return x;
    }

    /**
     * 校验为正数则返回该数字，否则抛出异常.
     * 
     * @param role
     * @param x
     * @return double
     */
    public static double nonNegative(String role, double x) {
        if (!(x >= 0)) { // not x < 0, to work with NaN.
            throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
        }
        return x;
    }
}
