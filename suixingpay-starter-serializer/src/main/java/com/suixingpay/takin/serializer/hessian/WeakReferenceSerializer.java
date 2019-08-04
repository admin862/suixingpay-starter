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

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.AbstractSerializer;
import com.caucho.hessian.io.ObjectSerializer;
import com.caucho.hessian.io.Serializer;

public class WeakReferenceSerializer extends AbstractSerializer implements ObjectSerializer {

    @Override
    public Serializer getObjectSerializer() {
        return this;
    }

    @Override
    public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
        if (out.addRef(obj)) {
            return;
        }
        @SuppressWarnings("unchecked")
        WeakReference<Object> data = (WeakReference<Object>) obj;

        int refV = out.writeObjectBegin(WeakReference.class.getName());

        if (refV == -1) {
            out.writeInt(1);
            out.writeString("ref");
            out.writeObjectBegin(WeakReference.class.getName());
        }
        if (data != null) {
            Object ref = data.get();
            if (null != ref) {
                out.writeObject(ref);
            } else {
                out.writeNull();
            }
        } else {
            out.writeNull();
        }
    }
}
