/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.token.autoconfigure;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.suixingpay.takin.exception.config.ExceptionAutoConfiguration;
import com.suixingpay.takin.exception.handler.SuixingPayExceptionHandler;
import com.suixingpay.takin.redis.IRedisOperater;
import com.suixingpay.takin.redis.autoconfigure.RedisOperaterAutoConfigure;
import com.suixingpay.takin.token.TokenHelper;
import com.suixingpay.takin.token.TokenInfo;
import com.suixingpay.takin.token.TokenProperties;
import com.suixingpay.takin.token.auth.AuthenticationChecker;
import com.suixingpay.takin.token.auth.DefaultAuthenticationChecker;
import com.suixingpay.takin.token.interceptor.TokenInterceptor;
import com.suixingpay.takin.token.repository.client.ClientRepositoryType;
import com.suixingpay.takin.token.repository.client.CookieRepository;
import com.suixingpay.takin.token.repository.client.DefaultRepository;
import com.suixingpay.takin.token.repository.client.HeaderRepository;
import com.suixingpay.takin.token.repository.client.ParameterRepository;
import com.suixingpay.takin.token.repository.client.TokenClientRepository;
import com.suixingpay.takin.token.repository.server.ServerTokenRedisRepository;
import com.suixingpay.takin.token.repository.server.ServerTokenRepository;
import com.suixingpay.takin.token.repository.server.UserTokenRedisRepository;
import com.suixingpay.takin.token.repository.server.UserTokenRepository;
import com.suixingpay.takin.token.resolvers.CurrentUserMethodArgumentResolver;

/**
 * token 自动配置
 *
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties({ TokenProperties.class })
@AutoConfigureAfter(value = { RedisOperaterAutoConfigure.class, ExceptionAutoConfiguration.class })
@ConditionalOnProperty(value = TokenProperties.PREFIX + ".enabled", matchIfMissing = true)
public class TokenAutoConfiguration {
    @Autowired
    private TokenProperties tokenProperties;

    @Autowired
    private IRedisOperater redisOperater;

    @Bean
    @ConditionalOnMissingBean(ServerTokenRepository.class)
    public ServerTokenRepository<TokenInfo> sxfServerTokenRepository() {
        return new ServerTokenRedisRepository<>(redisOperater);
    }

    @Bean
    @ConditionalOnMissingBean(UserTokenRepository.class)
    public UserTokenRepository sxfServerUserRepository() {
        return new UserTokenRedisRepository(redisOperater);
    }

    @Bean
    @ConditionalOnMissingBean(TokenClientRepository.class)
    public TokenClientRepository sxfTokenClientRepository() {
        ClientRepositoryType[] clientRepositoryTypes = tokenProperties.getClientRepositoryTypes();
        if (null == clientRepositoryTypes || clientRepositoryTypes.length == 0) {
            clientRepositoryTypes = ClientRepositoryType.values();
        }
        Set<TokenClientRepository> repositories = new LinkedHashSet<>(clientRepositoryTypes.length);
        for (int i = 0; i < clientRepositoryTypes.length; i++) {
            ClientRepositoryType type = clientRepositoryTypes[i];
            TokenClientRepository tmp = null;
            switch (type) {
                case PARAM:
                    tmp = new ParameterRepository();
                    break;
                case HEADER:
                    tmp = new HeaderRepository();
                    break;
                case COOKIE:
                    tmp = new CookieRepository(tokenProperties);
                    break;
                default:
                    throw new IllegalArgumentException(
                            "'" + TokenProperties.PREFIX + ".clientRepositoryTypes'中设置的" + type.name() + "有误！");
            }
            repositories.add(tmp);
        }
        TokenClientRepository[] tmp = new TokenClientRepository[repositories.size()];
        tmp = repositories.toArray(tmp);
        return new DefaultRepository(tmp);
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationChecker.class)
    public AuthenticationChecker sxfAuthenticationChecker() {
        return new DefaultAuthenticationChecker();
    }

    @Bean
    @ConditionalOnMissingBean(TokenHelper.class)
    public TokenHelper tokenHelper(ServerTokenRepository<TokenInfo> sxfServerTokenRepository,
            UserTokenRepository sxfServerUserRepository, TokenClientRepository sxfTokenClientRepository) {
        return new TokenHelper(sxfServerTokenRepository, sxfServerUserRepository, sxfTokenClientRepository,
                tokenProperties);
    }

    @Bean
    @ConditionalOnMissingBean(TokenInterceptor.class)
    public TokenInterceptor sxfTokenInterceptor(TokenHelper tokenHelper, AuthenticationChecker sxfAuthenticationChecker,
            SuixingPayExceptionHandler sxfExceptionHandler) {
        return new TokenInterceptor(tokenHelper, sxfAuthenticationChecker, sxfExceptionHandler);
    }

    @Bean
    @ConditionalOnMissingBean(CurrentUserMethodArgumentResolver.class)
    public CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver() {
        return new CurrentUserMethodArgumentResolver();
    }

    @Configuration
    public static class TokenInterceptorRegistry extends WebMvcConfigurerAdapter implements Ordered {

        @Autowired
        private TokenProperties tokenProperties;

        @Autowired
        private TokenInterceptor sxfTokenInterceptor;

        @Autowired
        private CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            Set<String> set = new HashSet<>();
            set.add("/error");
            set.add("/js/**");
            set.add("/css/**");
            set.add("/images/**");
            set.add("/static/**");
            set.add("/favicon.ico");
            set.add("/**/favicon.ico");
            set.add("/webjars/**");
            set.add("/swagger-ui.html");
            set.add("/v2/api-docs");
            set.add("/swagger-resources/**");
            for (String s : tokenProperties.getExcludePathPatterns()) {
                set.add(s);
            }
            String[] excludePaths = new String[set.size()];
            excludePaths = set.toArray(excludePaths);
            registry.addInterceptor(sxfTokenInterceptor).addPathPatterns(tokenProperties.getPathPatterns())
                    .excludePathPatterns(excludePaths);
        }

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
            argumentResolvers.add(currentUserMethodArgumentResolver);
        }

        @Override
        public int getOrder() {
            return tokenProperties.getOrder();
        }
    }
}
