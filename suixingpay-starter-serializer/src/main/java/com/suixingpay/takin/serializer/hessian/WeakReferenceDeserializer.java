/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.serializer.hessian;

import java.io.IOException;
import java.lang.ref.WeakReference;

import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractMapDeserializer;
import com.caucho.hessian.io.IOExceptionWrapper;

public class WeakReferenceDeserializer extends AbstractMapDeserializer {

    @Override
    public Object readObject(AbstractHessianInput in, Object[] fields) throws IOException {
        try {
            WeakReference<Object> obj = instantiate();
            in.addRef(obj);
            Object value = in.readObject();
            obj = null;
            return new WeakReference<Object>(value);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOExceptionWrapper(e);
        }

    }

    protected WeakReference<Object> instantiate() throws Exception {
        Object obj = new Object();
        return new WeakReference<Object>(obj);
    }

}
