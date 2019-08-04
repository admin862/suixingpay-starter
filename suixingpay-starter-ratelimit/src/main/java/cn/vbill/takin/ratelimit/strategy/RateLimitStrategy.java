package cn.vbill.takin.ratelimit.strategy;

import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * @author renjinhao
 *
 */
public interface RateLimitStrategy {

	LimitResult doRateLimit();

	void afterProcess(Object handle);

	@Data
	@AllArgsConstructor
	public static class LimitResult {
		private boolean through;
		private Object handle;
		private TimeUnit timeUnit;
		private long duration;
	}
}
