package cn.vbill.takin.ratelimit;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 
 * @author renjinhao
 *
 */
@ConfigurationProperties(prefix = "suixingpay.ratelimit")
@Data
public class RateLimitProperties {

	private String[] urlPatterns = new String[] { "/*" };
	private int errorStatusCode = 503;
	private String errorMsg = "\u7531\u4e8e\u8bbf\u95ee\u9891\u7387\u9650\u5236\uff0c\u8be5\u8bf7\u6c42\u88ab\u964d\u7ea7";
	private LeakyBucket leakyBucket = new LeakyBucket();
	private TokenBucket tokenBucket = new TokenBucket();
	private Semaphore semaphore = new Semaphore();

	@Data
	public static class LeakyBucket {
		private int bucketSize = 300;
		private int outflowTimeWindowMs = 1000;
		private int outflowSize = 300;
		private int inflowWaitTimeoutMs = 0;
		private int qps = 500;
	}

	@Data
	public static class TokenBucket {
		private int bucketSize = 300;
		private int addTimeWindowMs = 1000;
		private int addSize = 300;
		private int acquireTimeoutMs = 0;
		private int qps = 500;
	}

	@Data
	public static class Semaphore {
		private int maxSemaphore = 300;
		private int acquireTimeoutMs = 0;
	}
}
