/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年8月30日 下午3:04:41   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.swagger2;

import static org.springframework.http.HttpStatus.OK;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.suixingpay.takin.manager.autoconfigure.ManagerAutoConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.suixingpay.takin.exception.type.ExceptionCode;
import com.suixingpay.takin.manager.ManagerFilterUrlPatternsRegistrationBean;
import com.suixingpay.takin.manager.ManagerUrlRegistrationBean;
import com.suixingpay.takin.swagger2.SwaggerProperties.GlobalParameter;
import com.suixingpay.takin.swagger2.proxy.LogbackPropertiesProxy;
import com.suixingpay.takin.swagger2.proxy.TokenPropertiesProxy;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger2配置
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月30日 下午3:04:41
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月30日 下午3:04:41
 */
@Slf4j
@EnableSwagger2
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(SwaggerProperties.class)
@AutoConfigureAfter({ TokenPropertiesProxyConfiguration.class, LogbackPropertiesProxyConfiguration.class })
@ConditionalOnProperty(value = SwaggerProperties.PREFIX + ".enabled", matchIfMissing = true)
public class SwaggerAutoConfiguration implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @Autowired
    private SwaggerProperties swaggerProperties;

    @Autowired(required = false)
    private TokenPropertiesProxy sxfSwagger2TokenPropertiesProxy;

    @Autowired(required = false)
    private LogbackPropertiesProxy sxfSwagger2LogbackPropertiesProxy;

    @PostConstruct
    public List<Docket> createRestApi() {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
        // 没有分组
        if (swaggerProperties.getDocket().size() == 0) {
            configurableBeanFactory.registerSingleton("defaultDocket", defaultDocket());
            return null;
        }

        // 分组创建
        List<Docket> docketList = new LinkedList<>();
        for (String groupName : swaggerProperties.getDocket().keySet()) {
            SwaggerProperties.DocketInfo docketInfo = swaggerProperties.getDocket().get(groupName);
            Docket docket = buildDocket(groupName, docketInfo);
            configurableBeanFactory.registerSingleton(groupName, docket);
            docketList.add(docket);
        }
        return docketList;
    }

    private Docket defaultDocket() {
        ApiInfo apiInfo = new ApiInfoBuilder().title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription()).version(swaggerProperties.getVersion())
                .license(swaggerProperties.getLicense()).licenseUrl(swaggerProperties.getLicenseUrl())
                .contact(new Contact(swaggerProperties.getContact().getName(), swaggerProperties.getContact().getUrl(),
                        swaggerProperties.getContact().getEmail()))
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl()).build();

        // base-path处理
        // 当没有配置任何path的时候，解析/**
        if (swaggerProperties.getBasePath().isEmpty()) {
            swaggerProperties.getBasePath().add("/**");
        }
        List<Predicate<String>> basePath = new ArrayList<>();
        for (String path : swaggerProperties.getBasePath()) {
            basePath.add(PathSelectors.ant(path));
        }

        // exclude-path处理
        List<Predicate<String>> excludePath = new ArrayList<>();
        for (String path : swaggerProperties.getExcludePath()) {
            excludePath.add(PathSelectors.ant(path));
        }
        Set<String> produces = swaggerProperties.getProduces();
        if (null == produces) {
            produces = new LinkedHashSet<>();
        }
        if (produces.isEmpty()) {
            produces.add(MediaType.APPLICATION_JSON_UTF8_VALUE);
        }
        List<ResponseMessage> responseMessages = getResponseMessages();
        ApiSelectorBuilder builder = new Docket(DocumentationType.SWAGGER_2).host(swaggerProperties.getHost())
                .apiInfo(apiInfo)
                .globalOperationParameters(getParameter(sxfSwagger2TokenPropertiesProxy,
                        sxfSwagger2LogbackPropertiesProxy, swaggerProperties.getParameters()))
                .useDefaultResponseMessages(false).globalResponseMessage(RequestMethod.GET, responseMessages)
                .globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.PUT, responseMessages)
                .globalResponseMessage(RequestMethod.PATCH, responseMessages)
                .globalResponseMessage(RequestMethod.DELETE, responseMessages).produces(produces).select();
        String basePackage = swaggerProperties.getBasePackage();
        if (null != basePackage && basePackage.length() > 0) {
            builder.apis(RequestHandlerSelectors.basePackage(basePackage));
        }

        Docket docket = builder
                .paths(Predicates.and(Predicates.not(Predicates.or(excludePath)), Predicates.or(basePath))).build();
        return docket;
    }

    private Docket buildDocket(String groupName, SwaggerProperties.DocketInfo docketInfo) {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title(docketInfo.getTitle().isEmpty() ? swaggerProperties.getTitle() : docketInfo.getTitle())
                .description(docketInfo.getDescription().isEmpty() ? swaggerProperties.getDescription()
                        : docketInfo.getDescription())
                .version(docketInfo.getVersion().isEmpty() ? swaggerProperties.getVersion() : docketInfo.getVersion())
                .license(docketInfo.getLicense().isEmpty() ? swaggerProperties.getLicense() : docketInfo.getLicense())
                .licenseUrl(docketInfo.getLicenseUrl().isEmpty() ? swaggerProperties.getLicenseUrl()
                        : docketInfo.getLicenseUrl())
                .contact(new Contact(
                        docketInfo.getContact().getName().isEmpty() ? swaggerProperties.getContact().getName()
                                : docketInfo.getContact().getName(),
                        docketInfo.getContact().getUrl().isEmpty() ? swaggerProperties.getContact().getUrl()
                                : docketInfo.getContact().getUrl(),
                        docketInfo.getContact().getEmail().isEmpty() ? swaggerProperties.getContact().getEmail()
                                : docketInfo.getContact().getEmail()))
                .termsOfServiceUrl(
                        docketInfo.getTermsOfServiceUrl().isEmpty() ? swaggerProperties.getTermsOfServiceUrl()
                                : docketInfo.getTermsOfServiceUrl())
                .build();

        // base-path处理
        // 当没有配置任何path的时候，解析/**
        if (docketInfo.getBasePath().isEmpty()) {
            docketInfo.getBasePath().add("/**");
        }
        List<Predicate<String>> basePath = new ArrayList<>();
        for (String path : docketInfo.getBasePath()) {
            basePath.add(PathSelectors.ant(path));
        }

        // exclude-path处理
        List<Predicate<String>> excludePath = new ArrayList<>();
        for (String path : docketInfo.getExcludePath()) {
            excludePath.add(PathSelectors.ant(path));
        }
        Set<String> produces = swaggerProperties.getProduces();
        if (null == produces) {
            produces = docketInfo.getProduces();
        }
        if (null == produces) {
            produces = new LinkedHashSet<>();
        }
        List<ResponseMessage> responseMessages = getResponseMessages();
        ApiSelectorBuilder builder = new Docket(DocumentationType.SWAGGER_2).host(swaggerProperties.getHost())
                .apiInfo(apiInfo)
                .globalOperationParameters(getParameter(sxfSwagger2TokenPropertiesProxy,
                        sxfSwagger2LogbackPropertiesProxy, docketInfo.getParameters()))
                .useDefaultResponseMessages(false).globalResponseMessage(RequestMethod.GET, responseMessages)
                .globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.PUT, responseMessages)
                .globalResponseMessage(RequestMethod.PATCH, responseMessages)
                .globalResponseMessage(RequestMethod.DELETE, responseMessages).produces(produces).groupName(groupName)
                .select();
        String basePackage = docketInfo.getBasePackage();
        if (null != basePackage && basePackage.length() > 0) {
            builder.apis(RequestHandlerSelectors.basePackage(basePackage));
        }
        Docket docket = builder
                .paths(Predicates.and(Predicates.not(Predicates.or(excludePath)), Predicates.or(basePath))).build();
        return docket;

    }

    private List<Parameter> getParameter(TokenPropertiesProxy swagger2TokenPropertiesProxy,
            LogbackPropertiesProxy swagger2LogbackPropertiesProxy, List<GlobalParameter> aiparameters) {
        // 可以添加多个header或参数
        List<Parameter> parameters = new ArrayList<Parameter>();
        ParameterBuilder parameterBuilder = null;
        if (null != swagger2TokenPropertiesProxy && swagger2TokenPropertiesProxy.isEnabled()) {
            Set<String> types = swagger2TokenPropertiesProxy.getParameterTypes();
            for (String type : types) {
                parameterBuilder = new ParameterBuilder();
                parameterBuilder.name(swagger2TokenPropertiesProxy.getTokenName()).description("token认证")
                        .modelRef(new ModelRef("string")).parameterType(type).required(false);
                parameters.add(parameterBuilder.build());
            }
        }
        if (null != swagger2LogbackPropertiesProxy && swagger2LogbackPropertiesProxy.isEnabled()) {
            // traceId
            parameterBuilder = new ParameterBuilder();
            StringBuilder description = new StringBuilder("日志追踪ID");
            description.append(",允许的最长字符串为：").append(swagger2LogbackPropertiesProxy.getTraceIdMaxLength());
            description.append("，注意：将会使用下面正则表达式，去除一些字符，避免打乱日志格式(")
                    .append(swagger2LogbackPropertiesProxy.getInvalidChars()).append("");
            parameterBuilder.name(swagger2LogbackPropertiesProxy.getTraceId()).description(description.toString())
                    .modelRef(new ModelRef("string")).parameterType("header").required(false);
            parameters.add(parameterBuilder.build());

            parameterBuilder = new ParameterBuilder();
            parameterBuilder.name(swagger2LogbackPropertiesProxy.getTraceId()).description(description.toString())
                    .modelRef(new ModelRef("string")).parameterType("query").required(false);
            parameters.add(parameterBuilder.build());

        }
        for (GlobalParameter api : aiparameters) {
            parameterBuilder = new ParameterBuilder();
            parameterBuilder.name(api.getName()).description(api.getDescription())
                    .modelRef(new ModelRef(api.getModelType())).parameterType(api.getParameterType()).required(false);
            parameters.add(parameterBuilder.build());
        }
        return parameters;
    }

    private List<ResponseMessage> getResponseMessages() {
        List<ResponseMessage> responseMessages = new ArrayList<>();
        responseMessages.add(new ResponseMessageBuilder().code(OK.value()).message(OK.getReasonPhrase())
                .responseModel(null).build());
        for (ExceptionCode code : ExceptionCode.values()) {
            responseMessages.add(new ResponseMessageBuilder().code(code.value()).message(code.defaultMessage())
                    .responseModel(null).build());
        }
        return responseMessages;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Configuration
    @ConditionalOnWebApplication
    @AutoConfigureBefore(ManagerAutoConfiguration.class)
    @ConditionalOnClass(ManagerUrlRegistrationBean.class)
    @ConditionalOnProperty(value = SwaggerManagerConfiguration.PREFIX + ".enabled", matchIfMissing = true)
    public static class SwaggerManagerConfiguration {
        public final static String PREFIX = "suixingpay.manager.swagger";

        /**
         * 配置管理器列表页面展示与链接需要的 url和title
         * @return
         */
        @Bean
        public ManagerUrlRegistrationBean swaggerManagerUrlRegistrationBean() {
            ManagerUrlRegistrationBean registrationBean = new ManagerUrlRegistrationBean();
            registrationBean.setTitle("Swagger管理页面");
            registrationBean.setUrl("swagger-ui.html");
            log.debug("注册管理web[{}]", registrationBean.getTitle());
            return registrationBean;
        }

        /**
         * 配置受管理器权限拦截的 url
         * @return
         */
        @Bean
        public ManagerFilterUrlPatternsRegistrationBean swaggerManagerFilterUrlPatternsRegistrationBean() {
            Collection<String> urlPatterns = Arrays.asList("/swagger-ui.html", "/v2/api-docs");
            log.debug("注册应用url[{}]到权限过滤器", StringUtils.collectionToCommaDelimitedString(urlPatterns));
            return new ManagerFilterUrlPatternsRegistrationBean(urlPatterns);
        }
    }
}
