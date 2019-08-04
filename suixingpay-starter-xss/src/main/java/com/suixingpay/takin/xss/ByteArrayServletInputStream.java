/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年2月10日 上午12:05:55   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.xss;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年2月10日 上午12:05:55
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年2月10日 上午12:05:55
 */
public class ByteArrayServletInputStream extends ServletInputStream {
    final ByteArrayInputStream byteIn;

    public ByteArrayServletInputStream(ByteArrayInputStream byteIn) {
        this.byteIn = byteIn;
    }

    @Override
    public int read() throws IOException {
        return byteIn.read();
    }

    @Override
    public boolean isFinished() {
        return byteIn.available() == 0;
    }

    @Override
    public boolean isReady() {
        return byteIn.available() > 0;
    }

    @Override
    public void setReadListener(ReadListener readListener) {

    }
}
