/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.serializer.hessian;

import java.lang.ref.SoftReference;

import com.caucho.hessian.io.AbstractSerializerFactory;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;

public class HessionSoftReferenceSerializerFactory extends AbstractSerializerFactory {

    private final SoftReferenceSerializer beanSerializer = new SoftReferenceSerializer();

    private final SoftReferenceDeserializer beanDeserializer = new SoftReferenceDeserializer();

    @Override
    public Serializer getSerializer(@SuppressWarnings("rawtypes") Class cl) throws HessianProtocolException {
        if (SoftReference.class.isAssignableFrom(cl)) {
            return beanSerializer;
        }
        return null;
    }

    @Override
    public Deserializer getDeserializer(@SuppressWarnings("rawtypes") Class cl) throws HessianProtocolException {
        if (SoftReference.class.isAssignableFrom(cl)) {
            return beanDeserializer;
        }
        return null;
    }

}
