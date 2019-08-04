/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.exception.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.suixingpay.takin.exception.type.SendErrorType;

import lombok.Data;

/**
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
@Data
@ConfigurationProperties(prefix = ExceptionProperties.PREFIX)
public class ExceptionProperties {
    public static final String PREFIX = "suixingpay.exception";

    /** 是否启用全局异常处理器 **/
    private boolean enabled = true;

    /**
     * 发送错误到错误页面的方法
     */
    private SendErrorType sendErrorType = SendErrorType.FORWARD;

    /**
     * 错误页面
     */
    private String errorPageUrl;

    /**
     * 把错误信息转给错误页面的参数或属性名称
     */
    private String errorMessageName = "errorMessage";

    /**
     * 把错误码值转给错误页面的参数或属性名称
     */
    private String codeName = "errorCode";

    private String charSet = "UTF-8";
    /**
     * 是否显示验证失败的字段名称
     */
    private boolean showErrorFieldName = false;

    /**
     * 是否返回未知异常信息
     */
    private boolean returnUnkownExceptionMessage = true;

    /**
     * 自定义未知异常信息，如果不自定义会获取Exception中的Message
     */
    private String unkownExceptionMessage = null;

}
