/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli[ma_tl@suixingpay.com] 
 * @date: 2017年5月24日 下午5:45:59   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.util.collection;

import java.util.Map;

/**
 * MapUtils
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2017年5月24日 下午5:45:59
 * @version: V1.0
 * @review: matieli[ma_tl@suixingpay.com]/2017年5月24日 下午5:45:59
 */
public class MapUtils {
    /**
     * 判断Map是否为空
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    /**
     * 判断Map是否不为空
     *
     * @param map
     * @return
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
}
