/**
 * All rights Reserved, Designed By Suixingpay.
 * 
 * @author: matieli<ma_tl@suixingpay.com>
 * @date: 2017年3月8日 下午1:25:06
 * @Copyright ©2017 Suixingpay. All rights reserved. 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.util.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.common.io.Resources;

/**
 * 针对Jar包内的文件的工具类
 *
 * @author: matieli[ma_tl@suixingpay.com]
 * @created: 2017-01-22 17:39
 */
public abstract class ResourceUtils {
    /**
     * 
     * 打开文件
     * 
     * @param resourceName
     * @return
     * @throws IOException
     */
    public static File asFile(String resourceName) throws IOException {
        try {
            return new File(Resources.getResource(resourceName).toURI());
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * 
     * 打开文件
     * 
     * @param contextClass
     * @param resourceName
     * @return
     * @throws IOException
     */
    public static File asFile(Class<?> contextClass, String resourceName) throws IOException {
        try {
            return new File(Resources.getResource(contextClass, resourceName).toURI());
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * 
     * 打开文件
     * 
     * @param resourceName
     * @return
     * @throws IOException
     */
    public static InputStream asStream(String resourceName) throws IOException {
        return Resources.getResource(resourceName).openStream();
    }

    /**
     * 
     * 打开文件
     * 
     * @param contextClass
     * @param resourceName
     * @return
     * @throws IOException
     */
    public static InputStream asStream(Class<?> contextClass, String resourceName)
            throws IOException {
        return Resources.getResource(contextClass, resourceName).openStream();
    }

    ////// 读取内容／／／／／

    /**
     * 读取内容
     * 
     * @param resourceName
     * @return
     * @throws IOException
     */
    public static String toString(String resourceName) throws IOException {
        return Resources.toString(Resources.getResource(resourceName), StandardCharsets.UTF_8);
    }

    /**
     * 
     * 根据Class的相对路径计算resource name
     * 
     * @param contextClass
     * @param resourceName
     * @return
     * @throws IOException
     */
    public static String toString(Class<?> contextClass, String resourceName) throws IOException {
        return Resources.toString(Resources.getResource(contextClass, resourceName),
                StandardCharsets.UTF_8);
    }

    /**
     * 
     * toLines
     * 
     * @param resourceName
     * @return
     * @throws IOException
     */
    public static List<String> toLines(String resourceName) throws IOException {
        return Resources.readLines(Resources.getResource(resourceName), StandardCharsets.UTF_8);
    }

    /**
     * 
     * 根据Class的相对路径计算resource name
     * 
     * @param contextClass
     * @param resourceName
     * @return
     * @throws IOException
     */
    public static List<String> toLines(Class<?> contextClass, String resourceName)
            throws IOException {
        return Resources.readLines(Resources.getResource(contextClass, resourceName),
                StandardCharsets.UTF_8);
    }
}
