/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月21日 上午11:50:56   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.serializer;

/**
 * 序列化，反序列化工具
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月21日 上午11:50:56
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月21日 上午11:50:56
 */
public interface ISerializer {
    /**
     * Serialize the given object to binary data.
     * 
     * @param obj object to serialize
     * @return the equivalent binary data
     * @throws Exception 异常
     */
    byte[] serialize(final Object obj) throws Exception;

    /**
     * Deserialize an object from the given binary data.
     * 
     * @param bytes object binary representation
     * @return the equivalent object instance
     * @throws Exception 异常
     */
    Object deserialize(final byte[] bytes) throws Exception;
}
