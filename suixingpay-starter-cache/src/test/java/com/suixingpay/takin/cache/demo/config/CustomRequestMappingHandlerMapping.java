/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年8月15日 下午3:23:37   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.cache.demo.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月15日 下午3:23:37
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月15日 下午3:23:37
 */
public class CustomRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMapping classAnnotation = AnnotationUtils.findAnnotation(handlerType, RequestMapping.class);

        if (classAnnotation == null && handlerType.getName().startsWith("com.suixingpay.")
                && Modifier.isPublic(method.getModifiers())) {
            RequestMappingInfo info = createRequestMappingInfo(method);
            if (info != null) {
                String handlername = handlerType.getName().substring(handlerType.getName().lastIndexOf(".") + 1)
                        .replaceAll("Controller", "");
                StringBuilder sb = new StringBuilder();
                sb.append("/").append(handlername.toLowerCase());
                System.out.println(sb);
                info = createRequestMappingInfo(createReplaceAnnotation(sb.toString()), null).combine(info);
                return info;
            }
            return info;
        }
        return super.getMappingForMethod(method, handlerType);

    }

    private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
        RequestCondition<?> condition = (element instanceof Class ? getCustomTypeCondition((Class<?>) element)
                : getCustomMethodCondition((Method) element));
        return (requestMapping != null ? createRequestMappingInfo(requestMapping, condition) : null);
    }

    private RequestMapping createReplaceAnnotation(final String name) {
        return new RequestMapping() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return RequestMapping.class;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public String[] value() {
                return new String[] { name };
            }

            @Override
            public String[] path() {
                return new String[] { name };
            }

            @Override
            public RequestMethod[] method() {
                return new RequestMethod[0];
            }

            @Override
            public String[] params() {
                return new String[0];
            }

            @Override
            public String[] headers() {
                return new String[0];
            }

            @Override
            public String[] consumes() {
                return new String[0];
            }

            @Override
            public String[] produces() {
                return new String[0];
            }
        };
    }

}
