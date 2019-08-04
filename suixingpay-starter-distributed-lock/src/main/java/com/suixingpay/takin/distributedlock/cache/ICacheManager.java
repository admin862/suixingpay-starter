/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.distributedlock.cache;

/**
 * 缓存管理器
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
public interface ICacheManager {

    String KEY_CHAR_SET = "UTF-8";

    /**
     * 往缓存写数据
     * 
     * @param cacheKey 缓存Key
     * @param result 缓存数据
     * @param expire 缓存过期时间，如果是0，则长期缓存
     * @throws Exception
     */
    void setCache(final String cacheKey, final Object result, int expire) throws Exception;

    /**
     * 根据缓存Key获得缓存中的数据
     * 
     * @param key 缓存key
     * @return 缓存数据
     * @throws Exception
     */
    Object get(final String key) throws Exception;

    /**
     * 删除缓存
     * 
     * @param key 缓存key
     * @throws Exception
     */
    void delete(final String key) throws Exception;
}
