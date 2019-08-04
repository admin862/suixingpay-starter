/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli<ma_tl@suixingpay.com> 
 * @date: 2017年3月8日 下午1:25:06   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.util.id;

import java.util.UUID;

/**
 * 分布式id生成器
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2017年3月8日 下午2:07:55
 * @version: V1.0
 */
public class IdUtils {
    private static IdWorker ID = new IdWorker(1);

    /**
     * 生产随机ID
     * 
     * @return 
     */
    public static long getNextId() {
        return ID.nextId();
    }

    /**
     * 生产随机ID
     * 
     * @param wordId
     * @return
     */
    public static long getNextId(int wordId) {
        ID = new IdWorker(wordId);
        return ID.nextId();
    }
    
    public static synchronized String get32UUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
