/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.serializer.hessian;

import java.math.BigDecimal;

import com.caucho.hessian.io.AbstractSerializerFactory;
import com.caucho.hessian.io.BigDecimalDeserializer;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;
import com.caucho.hessian.io.StringValueSerializer;

public class HessionBigDecimalSerializerFactory extends AbstractSerializerFactory {

    private static final StringValueSerializer BIG_DECIMAL_SERIALIZER = new StringValueSerializer();

    private static final BigDecimalDeserializer BIGD_ECIMAL_DESERIALIZER = new BigDecimalDeserializer();

    @Override
    public Serializer getSerializer(@SuppressWarnings("rawtypes") Class cl) throws HessianProtocolException {
        if (BigDecimal.class.isAssignableFrom(cl)) {
            return BIG_DECIMAL_SERIALIZER;
        }
        return null;
    }

    @Override
    public Deserializer getDeserializer(@SuppressWarnings("rawtypes") Class cl) throws HessianProtocolException {
        if (BigDecimal.class.isAssignableFrom(cl)) {
            return BIGD_ECIMAL_DESERIALIZER;
        }
        return null;
    }

}
