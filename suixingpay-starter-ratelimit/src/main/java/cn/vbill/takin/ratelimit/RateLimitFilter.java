package cn.vbill.takin.ratelimit;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import cn.vbill.takin.ratelimit.strategy.FilterInvocation;
import cn.vbill.takin.ratelimit.strategy.RateLimitContext;

/**
 * 
 * @author renjinhao
 *
 */
public class RateLimitFilter extends OncePerRequestFilter {

	private RateLimitContext context;
	private RateLimitProperties properties;

	public RateLimitFilter(RateLimitContext context, RateLimitProperties properties) {
		this.context = context;
		this.properties = properties;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			context.execute(new FilterInvocation(request, response, filterChain, properties));
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

}
