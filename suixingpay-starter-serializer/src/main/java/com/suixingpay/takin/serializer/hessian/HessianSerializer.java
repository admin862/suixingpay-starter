/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.serializer.hessian;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.AbstractSerializerFactory;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import com.suixingpay.takin.serializer.ISerializer;

/**
 * @author jiayu.qiu
 */
public class HessianSerializer implements ISerializer {

    private static final SerializerFactory SERIALIZER_FACTORY = new SerializerFactory();

    static {
        SERIALIZER_FACTORY.addFactory(new HessionBigDecimalSerializerFactory());
        SERIALIZER_FACTORY.addFactory(new HessionSoftReferenceSerializerFactory());
    }

    /**
     * 添加自定义SerializerFactory
     * 
     * @param factory AbstractSerializerFactory
     */
    public void addSerializerFactory(AbstractSerializerFactory factory) {
        SERIALIZER_FACTORY.addFactory(factory);
    }

    @Override
    public byte[] serialize(final Object obj) throws Exception {
        if (obj == null) {
            return null;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        AbstractHessianOutput output = new Hessian2Output(outputStream);
        output.setSerializerFactory(SERIALIZER_FACTORY);
        // 将对象写到流里
        output.writeObject(obj);
        output.flush();
        byte[] val = outputStream.toByteArray();
        output.close();
        return val;
    }

    @Override
    public Object deserialize(final byte[] bytes) throws Exception {
        if (null == bytes || bytes.length == 0) {
            return null;
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        AbstractHessianInput input = new Hessian2Input(inputStream);
        input.setSerializerFactory(SERIALIZER_FACTORY);
        Object obj = input.readObject();
        input.close();
        return obj;
    }
}
