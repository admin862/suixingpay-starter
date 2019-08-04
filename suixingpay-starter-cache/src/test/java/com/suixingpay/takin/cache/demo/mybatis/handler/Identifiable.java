/**
 * 
 */
package com.suixingpay.takin.cache.demo.mybatis.handler;

/**
 * T Integer、Short、Character、Byte、String、Boolean、Long、Double、Float
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月17日 上午9:18:42
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月17日 上午9:18:42
 */
public interface Identifiable<K> {
    /**
     * ID
     * 
     * @return
     */
    K getId();
}