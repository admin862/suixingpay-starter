package cn.vbill.takin.ratelimit.strategy;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.vbill.takin.ratelimit.RateLimitProperties;
import lombok.AllArgsConstructor;

/**
 * 
 * @author renjinhao
 *
 */
@AllArgsConstructor
public class FilterInvocation implements Invocation {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private FilterChain filterChain;
	private RateLimitProperties properties;

	@Override
	public void invoke() throws Exception {
		filterChain.doFilter(request, response);
	}

	@Override
	public void fallback() throws Exception {
		response.sendError(properties.getErrorStatusCode(), properties.getErrorMsg());
	}

}
