/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年8月21日 下午2:06:31   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.serializer.jdk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.suixingpay.takin.serializer.ISerializer;

/**
 * JDK序列化
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月21日 下午2:06:31
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月21日 下午2:06:31
 */
public class JdkSerializer implements ISerializer {

    @Override
    public byte[] serialize(Object obj) throws Exception {
        if (obj == null) {
            return new byte[0];
        }
        // 将对象写到流里
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(outputStream);
        output.writeObject(obj);
        output.flush();
        return outputStream.toByteArray();
    }

    @Override
    public Object deserialize(byte[] bytes) throws Exception {
        if (null == bytes || bytes.length == 0) {
            return null;
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream input = new ObjectInputStream(inputStream);
        return input.readObject();
    }

}
