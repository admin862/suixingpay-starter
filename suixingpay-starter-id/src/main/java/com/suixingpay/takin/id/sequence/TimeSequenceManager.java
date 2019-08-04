package com.suixingpay.takin.id.sequence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.suixingpay.takin.id.IdProperties;
import com.suixingpay.takin.id.IdProperties.IdConfig;
import com.suixingpay.takin.id.SequenceManager;
import com.suixingpay.takin.id.util.FormatUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author renjinhao
 *
 */
@Slf4j
public class TimeSequenceManager implements SequenceManager, InitializingBean, DisposableBean {

	private static final int DEFAULT_CAPACITY = 6;
	private static final int DEFAULT_MAX = 999999;
	private static final long BLOCK_CHECK_INTERVAL = 5;
	private static final int BLOCK_WARN_TIMES = 3;
	private static final int CLEANUP_INTERVAL = 1;
	private static final int LIFE = 3;

	private Map<String, Integer> configs;
	private Map<String, IdConfig> properties;
	private Map<Long, SPool> pools = new HashMap<>();
	private ScheduledExecutorService scheduler;
	private ScheduledFuture<?> task;

	public TimeSequenceManager(IdProperties properties) {
		Map<String, IdConfig> configs = properties.getConfigs();
		this.configs = new HashMap<>(configs.size());
		for (Entry<String, IdConfig> config : configs.entrySet()) {
			this.configs.put(config.getKey(), calMax(config.getValue().getCapacityPs()));
		}
		this.properties = properties.getConfigs();
		scheduler = Executors
				.newSingleThreadScheduledExecutor(r -> new Thread(r, "suixingpay-id-timesequence-cleanup"));
	}

	private Integer calMax(int capacity) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < capacity; i++) {
			sb.append("9");
		}
		return Integer.valueOf(sb.toString());
	}

	@Override
	public String getNextSequence(String namespace) throws Exception {
		return getNextSequence(namespace, 0);
	}

	public String getNextSequence(String namespace, int times) throws Exception {
		if (times + 1 >= BLOCK_WARN_TIMES)
			log.warn("namespace:" + namespace + "，获取序号尝试次数>=" + BLOCK_WARN_TIMES + "次");
		Long s = System.currentTimeMillis() / 1000;
		SPool sPool = getSPool(s);
		NmPool nmPool = sPool.getNmPool(namespace);
		int seqInt = nmPool.acquire();
		if (seqInt > -1) {
			return s.toString() + format(seqInt, namespace);
		} else {
			blockUntilNextSecond(s);
			return getNextSequence(namespace, times + 1);
		}
	}

	private void blockUntilNextSecond(Long s) {
		while (System.currentTimeMillis() / 1000 == s) {
			try {
				Thread.sleep(BLOCK_CHECK_INTERVAL);
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	private String format(int seqInt, String namespace) {
		int c = getCapacity(namespace);
		return FormatUtil.formatSeq(seqInt, c);
	}

	private SPool getSPool(Long s) {
		SPool sPool = pools.get(s);
		if (sPool == null) {
			synchronized (this) {
				sPool = pools.get(s);
				if (sPool == null) {
					sPool = new SPool(s);
					pools.put(s, sPool);
				}
			}
		}
		return sPool;
	}

	private Integer getMax(String namespace) {
		Integer max = configs.get(namespace);
		if (max == null)
			return DEFAULT_MAX;
		return max;
	}

	private int getCapacity(String namespace) {
		IdConfig c = properties.get(namespace);
		if (c != null)
			return c.getCapacityPs();
		return DEFAULT_CAPACITY;
	}

	@Override
	public void destroy() throws Exception {
		if (task != null)
			task.cancel(true);
		if (scheduler != null)
			scheduler.shutdown();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		task = scheduler.scheduleAtFixedRate(() -> {
			Collection<SPool> sPools = pools.values();
			Long nowSecond = System.currentTimeMillis() / 1000;
			for (SPool sp : sPools) {
				if (sp.getCurrentSecond() + LIFE < nowSecond) {
					pools.remove(sp.getCurrentSecond());
				}
			}
		}, CLEANUP_INTERVAL, CLEANUP_INTERVAL, TimeUnit.SECONDS);
	}

	@Data
	class SPool {
		private Map<String, NmPool> pools = new HashMap<>();
		private long currentSecond;

		SPool(long currentSecond) {
			this.currentSecond = currentSecond;
		}

		NmPool getNmPool(String namespace) {
			NmPool nmPool = pools.get(namespace);
			if (nmPool == null) {
				synchronized (namespace.intern()) {
					nmPool = pools.get(namespace);
					if (nmPool == null) {
						nmPool = new NmPool(getMax(namespace));
						pools.put(namespace, nmPool);
					}
				}
			}
			return nmPool;
		}
	}

	@Data
	class NmPool {
		private int max;
		private AtomicInteger current;

		NmPool(int max) {
			this.max = max;
			this.current = new AtomicInteger(0);
		}

		int acquire() {
			int c = current.incrementAndGet();
			if (c > max)
				return -1;
			return c;
		}
	}

}
