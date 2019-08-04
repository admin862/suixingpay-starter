/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年12月25日 下午3:24:23   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.logback.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 替换手机号
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年12月25日 下午3:24:23
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年12月25日 下午3:24:23
 */
public class MangoPatternLayout extends PatternLayout {

    public static final String REPLACEMENT = "【**敏感屏蔽**】";

    // 敏感屏蔽
    private boolean sensitive = false;

    private static List<Pattern> regexMap = new ArrayList<Pattern>();

    static {
        // 手机号码
        regexMap.add(Pattern.compile("(?<!\\d)(?:(?:1[34578]\\d{9}))(?!\\d)"));
        // 电话号码 带 -
        regexMap.add(Pattern.compile("(?<!\\d)(?:(?:[0]{1}[0-9]{2,3}-[0-9]{7,8}))(?!\\d)"));
        // 电话号码 不带 -
        regexMap.add(Pattern.compile("(?<!\\d)(?:(?:[0]{1}[0-9]{2,3}[0-9]{7,8}))(?!\\d)"));
    }

    public void setPatternRegex(String patternRegex) {
        sensitive = "Y".equalsIgnoreCase(patternRegex);
    }

    public static List<Pattern> getRegexMap() {
        return regexMap;
    }

    @Override
    public String doLayout(ILoggingEvent event) {
        String logMsg = super.doLayout(event);
        if (!sensitive) {
            return logMsg;
        }
        for (Pattern pattern : regexMap) {
            Matcher matcher = pattern.matcher(logMsg);
            while (matcher.find()) {
                logMsg = matcher.replaceAll(REPLACEMENT);
            }
        }
        return logMsg;
    }
}
