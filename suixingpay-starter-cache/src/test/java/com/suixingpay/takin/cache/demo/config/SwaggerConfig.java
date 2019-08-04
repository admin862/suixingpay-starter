package com.suixingpay.takin.cache.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月31日 下午1:34:33
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月31日 下午1:34:33
 */
@Configuration
@EnableSwagger2 // Enable swagger 2.0 spec
// @ConditionalOnResource(resources="classpath:/META-INF/resources/swagger-ui.html")
// @Profile({"dev"}) // 为了安全考虑，只在开发环境开启
public class SwaggerConfig {

    public static final String GROUP_NAME = "xxx接口";

    @Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2)//
                .groupName(GROUP_NAME)//
                .genericModelSubstitutes(DeferredResult.class)//
                .useDefaultResponseMessages(false)//
                .forCodeGeneration(true)//
                .pathMapping("/")//
                .select()//
                .apis(RequestHandlerSelectors.basePackage("com.suixingpay"))//
                .build()//
                // .securitySchemes(newArrayList(oauth()))
                // .securityContexts(newArrayList(securityContext()))
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("邱家榆", "http://www.test.com", "qiu_jy@suixingpay.com");

        ApiInfo apiInfo = new ApiInfoBuilder().title("xxx接口").description("xxx接口").version("0.1").contact(contact)
                .termsOfServiceUrl("http://www.test.com").build();

        return apiInfo;
    }
}
