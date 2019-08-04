/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年8月30日 下午3:01:14   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.web.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月30日 下午3:01:14
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月30日 下午3:01:14
 */
@Data
@ConfigurationProperties(WebProperties.PREFIX)
public class WebProperties {
    public static final String PREFIX = "suixingpay.web";

    private Converter converter = new Converter();

    @Data
    static class Converter {
        private StringToDate stringToDate = new StringToDate();
        private StringToBaseEnum stringToBaseEnum = new StringToBaseEnum();
    }

    @Data
    static class StringToDate {
        // 字符串转 Date
        private boolean enabled = true;
    }

    @Data
    static class StringToBaseEnum {
        // 字符串 转 BaseEnum
        private boolean enabled = true;
    }

}
