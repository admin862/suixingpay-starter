/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月31日 上午9:29:42   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.token.resolvers;

import java.util.Optional;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.suixingpay.takin.token.TokenHelper;
import com.suixingpay.takin.token.TokenInfo;
import com.suixingpay.takin.token.TokenWapper;

/**
 * 增加方法注入，将含有TokenInfo注解的方法参数注入当前登录用户
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月31日 上午9:29:42
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月31日 上午9:29:42
 */
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 如果参数类型是TokenInfo, 这里需要注意：TokenInfo.class.isAssignableFrom()
        return TokenInfo.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Optional<TokenWapper> tokenOptional = TokenHelper.getTokenWapper(webRequest);
        if (tokenOptional.isPresent()) {
            return tokenOptional.get().getTokenInfo();
        }
        return null;
    }

}
