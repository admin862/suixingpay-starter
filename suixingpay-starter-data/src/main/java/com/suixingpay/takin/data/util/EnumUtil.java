/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年2月1日 上午9:55:33   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.data.util;

import com.suixingpay.takin.data.enums.BaseEnum;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年2月1日 上午9:55:33
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年2月1日 上午9:55:33
 */
public class EnumUtil {
    public static void isEnumType(Class<?> targetType) {
        if (null == targetType || !targetType.isEnum()) {
            throw new IllegalArgumentException(
                    "The target type " + targetType.getName() + " does not refer to an enum");
        }
    }

    /**
     * 根据code获得枚举
     * 
     * @param cls
     * @param code
     * @return
     */
    public static <T extends Enum<? extends BaseEnum>> T getByCode(Class<T> cls, int code) {
        isEnumType(cls);
        T enums[] = cls.getEnumConstants();
        if (enums == null) {
            throw new IllegalArgumentException(cls.getSimpleName() + " does not represent an enum type.");
        }
        for (T e : enums) {
            BaseEnum itype = (BaseEnum) e;
            if (code == itype.getCode()) {
                return e;
            }
        }
        return null;
    }

    /**
     * @param cls
     * @param name
     * @return
     */
    public static <T extends Enum<?>> T getByName(Class<T> cls, String name) {
        if (null == name || name.isEmpty()) {
            return null;
        }
        isEnumType(cls);
        T enums[] = cls.getEnumConstants();
        if (enums == null) {
            throw new IllegalArgumentException(cls.getSimpleName() + " does not represent an enum type.");
        }
        for (T e : enums) {
            Enum<?> itype = (Enum<?>) e;
            if (name.equals(itype.name())) {
                return e;
            }
        }
        return null;
    }
}
