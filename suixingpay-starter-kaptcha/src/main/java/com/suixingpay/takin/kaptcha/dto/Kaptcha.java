/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年11月14日 下午5:52:15   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.kaptcha.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年11月14日 下午5:52:15
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年11月14日 下午5:52:15
 */
@Getter
@Setter
public class Kaptcha {
    /**
     * Redis缓存验证码的key
     */
    private String key;
    /**
     * 使用Base64编码的验证码图片
     */
    private String base64Code;
}
