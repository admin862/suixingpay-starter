/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年8月30日 下午3:01:14   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.swagger2;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月30日 下午3:01:14
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月30日 下午3:01:14
 */
@Data
@ConfigurationProperties(SwaggerProperties.PREFIX)
public class SwaggerProperties {
    public static final String PREFIX = "suixingpay.swagger2";
    private static final String DEFAULT_LICENSE = "随行付";
    private static final String DEFAULT_LICENSEURL = "http://vbill.cn";
    private static final String DEFAULT_TERMSOFSERVICEURL = "http://vbill.cn";

    /** 是否启用 **/
    private boolean enabled = true;
    /** 标题 **/
    private String title = "";
    /** 描述 **/
    private String description = "";
    /** 版本 **/
    private String version = "1.0.0";
    /** 许可证 **/
    private String license = DEFAULT_LICENSE;
    /** 许可证URL **/
    private String licenseUrl = DEFAULT_LICENSEURL;
    /** 服务条款URL **/
    private String termsOfServiceUrl = DEFAULT_TERMSOFSERVICEURL;

    private Contact contact = new Contact();

    /** swagger会解析的包路径 **/
    private String basePackage = "";

    /** swagger会解析的url规则 **/
    private List<String> basePath = new ArrayList<>();
    /** 在basePath基础上需要排除的url规则 **/
    private List<String> excludePath = new ArrayList<>();

    /** host信息 **/
    private String host = "";

    /**
     * 全局参数
     */
    private List<GlobalParameter> parameters = new ArrayList<>();
    
    private Set<String> produces = new LinkedHashSet<>();
    
    /** 分组文档 **/
    private Map<String, DocketInfo> docket = new LinkedHashMap<>();

    @Data
    @NoArgsConstructor
    public static class DocketInfo {

        /** 标题 **/
        private String title = "";
        /** 描述 **/
        private String description = "";
        /** 版本 **/
        private String version = "";
        /** 许可证 **/
        private String license = DEFAULT_LICENSE;
        /** 许可证URL **/
        private String licenseUrl = DEFAULT_LICENSEURL;
        /** 服务条款URL **/
        private String termsOfServiceUrl = DEFAULT_TERMSOFSERVICEURL;

        private Contact contact = new Contact();

        /** swagger会解析的包路径 **/
        private String basePackage = "";

        /** swagger会解析的url规则 **/
        private List<String> basePath = new ArrayList<>();
        /** 在basePath基础上需要排除的url规则 **/
        private List<String> excludePath = new ArrayList<>();

        /**
         * 全局参数
         */
        private List<GlobalParameter> parameters = new ArrayList<>();
        /**
         * 接口输出格式：application/json;charset=UTF-8
         */
        private Set<String> produces = new LinkedHashSet<>();
    }

    @Data
    @NoArgsConstructor
    public static class Contact {
        /** 联系人 **/
        private String name = "";
        /** 联系人url **/
        private String url = "";
        /** 联系人email **/
        private String email = "";

    }

    /**
     * 全局参数设置
     * 
     * @author: qiujiayu[qiu_jy@suixingpay.com]
     * @date: 2018年1月11日 上午10:04:46
     * @version: V1.0
     * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月11日 上午10:04:46
     */
    @Data
    public static class GlobalParameter {
        /** 参数名称 **/
        private String name = "";
        /** 参数说明 **/
        private String description = "";
        /** 参数类型：header, cookie, body, query **/
        private String parameterType = "query";
        /** 参数数据类型 **/
        private String modelType = "string";
        /** 是否必填 **/
        private boolean required = false;
    }

}
