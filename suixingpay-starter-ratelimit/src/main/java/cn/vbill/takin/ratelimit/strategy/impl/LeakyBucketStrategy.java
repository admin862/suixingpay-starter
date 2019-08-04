package cn.vbill.takin.ratelimit.strategy.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import cn.vbill.takin.ratelimit.RateLimitProperties;
import cn.vbill.takin.ratelimit.RateLimitProperties.LeakyBucket;
import cn.vbill.takin.ratelimit.strategy.RateLimitStrategy;

/**
 * 
 * @author renjinhao
 *
 */
public class LeakyBucketStrategy implements RateLimitStrategy, InitializingBean, DisposableBean {

	private RateLimitProperties.LeakyBucket properties;
	private ArrayBlockingQueue<Drop> bucket;
	private boolean start = true;
	private long lastTime = System.nanoTime();
	private long delay;

	public LeakyBucketStrategy(LeakyBucket properties) {
		this.properties = properties;
		bucket = new ArrayBlockingQueue<>(this.properties.getBucketSize());
		delay = TimeUnit.SECONDS.toNanos(1) / this.properties.getQps();
	}

	@Override
	public LimitResult doRateLimit() {
		boolean b = false;
		try {
			b = bucket.offer(new Drop(), properties.getInflowWaitTimeoutMs(), TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
		}
		TimeUnit tu = TimeUnit.NANOSECONDS;
		long duration = 0;
		if (b) {
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
		return new LimitResult(b, null, tu, duration);
	}

	@Override
	public void destroy() throws Exception {
		start = false;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		int outflowSize = properties.getOutflowSize();
		long timeWindow = TimeUnit.MILLISECONDS.toNanos(properties.getOutflowTimeWindowMs());
		long interval = timeWindow / outflowSize;
		Thread t = new Thread(() -> {
			while (start) {
				try {
					bucket.take();
				} catch (InterruptedException e1) {
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

	static class Drop {

	}
}
