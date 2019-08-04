package cn.vbill.takin.ratelimit.strategy.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import cn.vbill.takin.ratelimit.RateLimitProperties;
import cn.vbill.takin.ratelimit.RateLimitProperties.TokenBucket;
import cn.vbill.takin.ratelimit.strategy.RateLimitStrategy;

/**
 * 
 * @author renjinhao
 *
 */
public class TokenBucketStrategy implements RateLimitStrategy, InitializingBean, DisposableBean {

	private RateLimitProperties.TokenBucket properties;
	private ArrayBlockingQueue<Token> bucket;
	private boolean start = true;
	private long lastTime = System.nanoTime();
	private long delay;

	public TokenBucketStrategy(TokenBucket properties) {
		this.properties = properties;
		bucket = new ArrayBlockingQueue<>(this.properties.getBucketSize());
		for (int i = 0; i < this.properties.getBucketSize(); i++) {
			bucket.offer(new Token());
		}
		delay = TimeUnit.SECONDS.toNanos(1) / this.properties.getQps();
	}

	@Override
	public LimitResult doRateLimit() {
		Token token = null;
		try {
			token = bucket.poll(properties.getAcquireTimeoutMs(), TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
		}
		TimeUnit tu = TimeUnit.NANOSECONDS;
		long duration = 0;
		if (token != null) {
			synchronized (this) {
				long offset = System.nanoTime() - lastTime;
				if (offset < delay) {
					duration = delay - offset;
					lastTime = lastTime + delay;
				} else {
					lastTime = System.nanoTime();
				}
			}
		}
		return new LimitResult(token != null, token, tu, duration);
	}

	@Override
	public void destroy() throws Exception {
		start = false;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		int addSize = properties.getAddSize();
		long timeWindow = TimeUnit.MILLISECONDS.toNanos(properties.getAddTimeWindowMs());
		long interval = timeWindow / addSize;
		Thread t = new Thread(() -> {
			while (start) {
				try {
					bucket.put(new Token());
				} catch (InterruptedException e) {
				}
				try {
					TimeUnit.NANOSECONDS.sleep(interval);
				} catch (InterruptedException e) {
				}
			}
		});
		t.start();
	}

	@Override
	public void afterProcess(Object handle) {

	}

	static class Token {

	}

}
