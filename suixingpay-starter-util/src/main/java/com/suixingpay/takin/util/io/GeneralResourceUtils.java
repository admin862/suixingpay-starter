/**
 * All rights Reserved, Designed By Suixingpay.
 * 
 * @author: matieli<ma_tl@suixingpay.com>
 * @date: 2017年3月8日 下午1:25:06
 * @Copyright ©2017 Suixingpay. All rights reserved. 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.util.io;

import java.io.IOException;
import java.io.InputStream;

import com.suixingpay.takin.util.text.StringMoreUtils;

/**
 * 兼容file://与classpath://的情况的工具集
 *
 * @author: matieli[ma_tl@suixingpay.com]
 * @created: 2017-01-22 17:31
 */
public abstract class GeneralResourceUtils {
    /**
     * 
     */
    public static final String CLASSPATH = "classpath://";

    /**
     * 
     */
    public static final String FILE = "file://";

    /**
     * 
     * 兼容file://与classpath://的情况的打开文件成Stream
     * 
     * @param generalPath
     * @return
     * @throws IOException
     */
    public static InputStream asStream(String generalPath) throws IOException {
        if (StringMoreUtils.startsWith(generalPath, CLASSPATH)) {
            String resourceName = StringMoreUtils.substringAfter(generalPath, CLASSPATH);
            return ResourceUtils.asStream(resourceName);
        } else if (StringMoreUtils.startsWith(generalPath, FILE)) {
            String fileName = StringMoreUtils.substringAfter(generalPath, FILE);
            return FileUtils.asInputStream(fileName);
        } else {
            throw new IllegalArgumentException("unsupport resoure type:" + generalPath);
        }
    }
}
