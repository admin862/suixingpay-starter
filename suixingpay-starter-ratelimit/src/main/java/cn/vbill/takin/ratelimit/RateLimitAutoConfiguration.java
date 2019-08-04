package cn.vbill.takin.ratelimit;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import cn.vbill.takin.ratelimit.strategy.RateLimitContext;
import cn.vbill.takin.ratelimit.strategy.RateLimitStrategy;
import cn.vbill.takin.ratelimit.strategy.impl.LeakyBucketStrategy;
import cn.vbill.takin.ratelimit.strategy.impl.SemaphoreStrategy;
import cn.vbill.takin.ratelimit.strategy.impl.TokenBucketStrategy;

/**
 * 
 * @author renjinhao
 *
 */
@Configuration
@EnableConfigurationProperties(RateLimitProperties.class)
public class RateLimitAutoConfiguration {

	@Autowired
	private RateLimitProperties properties;

	@Bean
	public FilterRegistrationBean rateLimitFilter(RateLimitContext rateLimitContext) {
		FilterRegistrationBean reg = new FilterRegistrationBean();
		reg.setFilter(new RateLimitFilter(rateLimitContext, properties));
		reg.setUrlPatterns(Arrays.asList(properties.getUrlPatterns()));
		reg.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return reg;
	}

	@Bean
	public RateLimitContext rateLimitContext(RateLimitStrategy strategy) {
		return new RateLimitContext(strategy);
	}

	@Bean
	@ConditionalOnProperty(value = "suixingpay.ratelimit.rateLimitStrategy", havingValue = "semaphore")
	public RateLimitStrategy semaphoreStrategy() {
		return new SemaphoreStrategy(properties.getSemaphore());
	}

	@Bean
	@ConditionalOnProperty(value = "suixingpay.ratelimit.rateLimitStrategy", havingValue = "leaky")
	public RateLimitStrategy leakyBucketStrategy() {
		return new LeakyBucketStrategy(properties.getLeakyBucket());
	}

	@Bean
	@ConditionalOnProperty(value = "suixingpay.ratelimit.rateLimitStrategy", havingValue = "token", matchIfMissing = true)
	public RateLimitStrategy tokenBucketStrategy() {
		return new TokenBucketStrategy(properties.getTokenBucket());
	}

}
