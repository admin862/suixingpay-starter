package cn.vbill.takin.ratelimit.strategy;

import cn.vbill.takin.ratelimit.strategy.RateLimitStrategy.LimitResult;

/**
 * 
 * @author renjinhao
 *
 */
public class RateLimitContext {
	
	private RateLimitStrategy rateLimitStrategy;

	public RateLimitContext(RateLimitStrategy rateLimitStrategy) {
		this.rateLimitStrategy = rateLimitStrategy;
	}

	public void execute(Invocation invocation) throws Exception {
		LimitResult result = rateLimitStrategy.doRateLimit();
		if (result.isThrough()) {
			try {
				result.getTimeUnit().sleep(result.getDuration());
			} catch (InterruptedException e) {
			}
			try {
				invocation.invoke();
			} finally {
				rateLimitStrategy.afterProcess(result.getHandle());
			}
		} else
			invocation.fallback();
	}
}
