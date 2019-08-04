/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年4月12日 上午9:32:21   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.rabbitmq.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import lombok.extern.slf4j.Slf4j;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年4月12日 上午9:32:21
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年4月12日 上午9:32:21
 */
@Slf4j
public class FileUtil {

    public static void writeFile(String path, byte[] bytes, boolean append) throws Exception {
        if (null == path || path.length() == 0 || bytes == null) {
            return;
        }
        File file = new File(path);
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
        }

        try (OutputStream outputStream = new FileOutputStream(file, append)) {
            outputStream.write(bytes);
            outputStream.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static byte[] readFile(String path) {
        if (null == path || path.length() == 0) {
            return null;
        }
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        try (InputStream inputStream = new FileInputStream(file)) {
            int len = 0;
            byte[] buf = new byte[1024];
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            while ((len = inputStream.read(buf)) != -1) {
                bStream.write(buf, 0, len);
            }
            return bStream.toByteArray();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
