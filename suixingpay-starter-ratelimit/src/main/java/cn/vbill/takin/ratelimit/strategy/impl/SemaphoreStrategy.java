package cn.vbill.takin.ratelimit.strategy.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import cn.vbill.takin.ratelimit.RateLimitProperties;
import cn.vbill.takin.ratelimit.strategy.RateLimitStrategy;

/**
 * 
 * @author renjinhao
 *
 */
public class SemaphoreStrategy implements RateLimitStrategy {

	private ArrayBlockingQueue<Semaphore> pool;
	private RateLimitProperties.Semaphore properties;

	public SemaphoreStrategy(RateLimitProperties.Semaphore properties) {
		this.properties = properties;
		this.pool = new ArrayBlockingQueue<>(this.properties.getMaxSemaphore());
		for (int i = 0; i < this.properties.getMaxSemaphore(); i++) {
			pool.offer(new Semaphore());
		}
	}

	@Override
	public LimitResult doRateLimit() {
		Semaphore semaphore = null;
		try {
			semaphore = pool.poll(properties.getAcquireTimeoutMs(), TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
		}
		return new LimitResult(semaphore != null, semaphore, TimeUnit.NANOSECONDS, 0);
	}

	@Override
	public void afterProcess(Object handle) {
		pool.offer((Semaphore) handle);
	}

	static class Semaphore {

	}

}
