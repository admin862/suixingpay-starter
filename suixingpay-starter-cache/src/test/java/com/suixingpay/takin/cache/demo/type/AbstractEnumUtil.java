package com.suixingpay.takin.cache.demo.type;

/**
 * @author jiayu.qiu
 */
public class AbstractEnumUtil {

    /**
     * 根据id获得枚举
     * 
     * @param cls Class
     * @param id id
     * @return T 枚举
     */
    public static <T extends Enum<? extends AbstractEnum>> T getById(Class<T> cls, Integer id) {
        T enums[] = cls.getEnumConstants();
        if (enums == null) {
            throw new IllegalArgumentException(cls.getSimpleName() + " does not represent an enum type.");
        }
        if (null == id) {
            return null;
        }
        for (T e : enums) {
            AbstractEnum itype = (AbstractEnum) e;
            if (id.intValue() == itype.getId().intValue()) {
                return e;
            }
        }
        return null;
    }
}
