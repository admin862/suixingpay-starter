/**
 * All rights Reserved, Designed By Suixingpay.
 * 
 * @author: matieli<ma_tl@suixingpay.com>
 * @date: 2017年3月8日 下午1:25:06
 * @Copyright ©2017 Suixingpay. All rights reserved. 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.util.io;

import java.io.File;

import org.apache.commons.lang3.Validate;

import com.google.common.io.Files;
import com.suixingpay.takin.util.text.StringMoreUtils;

/**
 * 关于文件名，文件路径的工具集
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2017年3月8日 下午2:12:20
 * @version: V1.0
 */
public abstract class FilePathUtils {

    /**
     * 获取文件名(不包含路径)
     * 
     * @param fullName
     * @return
     */
    public static String getFileName(String fullName) {
        Validate.notEmpty(fullName);
        int last = fullName.lastIndexOf(File.separatorChar);
        return fullName.substring(last + 1);
    }

    /**
     * 获取文件名的扩展名部分(不包含.)
     * 
     * @param file
     * @return
     */
    public static String getFileExtension(File file) {
        return Files.getFileExtension(file.getName());
    }

    /**
     * 获取文件名的扩展名部分(不包含.)
     * 
     * @param fullName
     * @return
     */
    public static String getFileExtension(String fullName) {
        return Files.getFileExtension(fullName);
    }

    /**
     * 将路径整理，如 "a/../b"，整理成 "b"
     * 
     * @param pathName
     * @return
     */
    public static String simplifyPath(String pathName) {
        return Files.simplifyPath(pathName);
    }

    /**
     * 以拼接路径名
     * 
     * @param baseName
     * @param appendName
     * @return
     */
    public static String contact(String baseName, String... appendName) {
        if (appendName.length == 0) {
            return baseName;
        }

        String contactName;
        if (StringMoreUtils.endWith(baseName, File.separatorChar)) {
            contactName = baseName + appendName[0];
        } else {
            contactName = baseName + File.separatorChar + appendName[0];
        }

        if (appendName.length > 1) {
            for (int i = 1; i < appendName.length; i++) {
                contactName += File.separatorChar + appendName[i];
            }
        }

        return contactName;
    }
}
