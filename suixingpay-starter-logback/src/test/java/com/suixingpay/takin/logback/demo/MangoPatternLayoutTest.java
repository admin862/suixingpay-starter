/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年12月25日 下午4:07:38   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.logback.demo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.suixingpay.takin.logback.layout.MangoPatternLayout;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年12月25日 下午4:07:38
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年12月25日 下午4:07:38
 */
public class MangoPatternLayoutTest {
    public static void main(String[] args) {
        // String logMsg="13601074043";
        String logMsg = "|2017-12-25 15:11:39.140|||INFO -[当前用户手机号是：13601074043，xxxx]-[c.s.t.logback.LoggerTestApplication.java:44]-[main]";
        for (Pattern pattern : MangoPatternLayout.getRegexMap()) {
            Matcher matcher = pattern.matcher(logMsg);
            while (matcher.find()) {
                logMsg = matcher.replaceAll(MangoPatternLayout.REPLACEMENT);
            }
        }
        System.out.println(logMsg);
    }
}
