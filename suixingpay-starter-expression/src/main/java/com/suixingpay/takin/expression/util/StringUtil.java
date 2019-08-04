/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.expression.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * 字符串工具类
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
public class StringUtil {

    private static final String SPLIT_STR = "_";

    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj) {
        if (null == obj) {
            return true;
        }
        if (obj instanceof String) {
            return ((String) obj).length() == 0;
        }
        Class cl = obj.getClass();
        if (cl.isArray()) {
            int len = Array.getLength(obj);
            return len == 0;
        }
        if (obj instanceof Collection) {
            Collection tempCol = (Collection) obj;
            return tempCol.isEmpty();
        }
        if (obj instanceof Map) {
            Map tempMap = (Map) obj;
            return tempMap.isEmpty();
        }
        return false;
    }

    /**
     * 生成字符串的HashCode
     * 
     * @param buf
     * @return int hashCode
     */
    private static int getHashCode(String buf) {
        int hash = 5381;
        int len = buf.length();

        while (len-- > 0) {
            hash = ((hash << 5) + hash) + buf.charAt(len); /* hash * 33 + c */
        }
        return hash;
    }

    /**
     * 将Object 对象转换为唯一的Hash字符串
     * 
     * @param obj Object
     * @return Hash字符串
     */
    public static String getUniqueHashStr(Object obj) {
        return getMiscHashCode(BeanUtil.toString(obj));
    }

    /**
     * 通过混合Hash算法，将长字符串转为短字符串（字符串长度小于等于20时，不做处理）
     * 
     * @param str String
     * @return Hash字符串
     */
    public static String getMiscHashCode(String str) {
        if (null == str || str.length() == 0) {
            return "";
        }
        if (str.length() <= 20) {
            return str;
        }
        StringBuilder tmp = new StringBuilder();
        tmp.append(str.hashCode()).append(SPLIT_STR).append(getHashCode(str));

        int mid = str.length() / 2;
        String str1 = str.substring(0, mid);
        String str2 = str.substring(mid);
        tmp.append(SPLIT_STR).append(str1.hashCode());
        tmp.append(SPLIT_STR).append(str2.hashCode());

        return tmp.toString();
    }

}
