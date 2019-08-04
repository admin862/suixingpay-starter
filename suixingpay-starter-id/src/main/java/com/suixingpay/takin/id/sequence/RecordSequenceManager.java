package com.suixingpay.takin.id.sequence;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.suixingpay.takin.id.IdProperties;
import com.suixingpay.takin.id.SequenceManager;
import com.suixingpay.takin.id.sequence.SequenceRecord.NamespaceRecord;
import com.suixingpay.takin.id.util.FormatUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author renjinhao
 *
 */
@Slf4j
public class RecordSequenceManager implements SequenceManager, InitializingBean, DisposableBean {

	private SequenceRecord record;
	private Map<String, Sequence> sequences = new HashMap<>();
	private ScheduledExecutorService scheduler;
	private ScheduledFuture<?> task;
	private IdProperties properties;

	public RecordSequenceManager(IdProperties properties, SequenceRecord record) {
		this.properties = properties;
		this.record = record;
	}

	public String getNextSequence(String namespace) throws IOException {
		BigDecimal seqVal = getNextSeq(namespace);
		return FormatUtil.formatSeq(seqVal, properties.getConfigs().get(namespace).getSeqLength());
	}

	private BigDecimal getNextSeq(String namespace) throws IOException {
		synchronized (namespace.intern()) {
			Sequence sequence = sequences.get(namespace);
			if (sequence == null) {
				sequence = new Sequence(record.getNamespaceRecord(namespace), properties);
				sequences.put(namespace, sequence);
			}
			return sequence.next();
		}
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
		scheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "id-record-task");
			}
		});
		task = scheduler.scheduleAtFixedRate(() -> {
			List<String> needLoadNamespaces = sequences.values().stream().filter((s) -> s.needLoadNext)
					.map((s) -> s.namespace).collect(Collectors.toList());
			if (needLoadNamespaces == null || needLoadNamespaces.size() == 0)
				return;
			try {
				record.loadNextChunk(needLoadNamespaces);
				needLoadNamespaces.stream().map((n) -> sequences.get(n)).forEach((s) -> {
					s.needLoadNext = false;
				});
			} catch (IOException e) {
				log.error("id生成器更新文件异常", e);
			}
		}, properties.getRecordTaskDelay(), properties.getRecordTaskRate(), TimeUnit.MILLISECONDS);
	}

	static class Sequence {
		String namespace;
		NamespaceRecord record;
		IdProperties properties;
		BigDecimal current;
		BigDecimal capacity;
		volatile boolean needLoadNext = false;

		Sequence(NamespaceRecord record, IdProperties properties) {
			this.record = record;
			this.namespace = record.getNamespace();
			this.properties = properties;
			this.current = record.getFirstPosition().subtract(BigDecimal.ONE);
			this.capacity = BigDecimal.valueOf(record.getFirstChunkSize()).add(record.getFirstPosition())
					.subtract(BigDecimal.ONE);
		}

		BigDecimal next() {
			current = current.add(BigDecimal.ONE);
			if (!needLoadNext) {
				if (current.compareTo(capacity) <= 0)
					return current;
				else {
					current = record.getSecondPosition();
					capacity = BigDecimal.valueOf(record.getSecondChunkSize()).add(record.getSecondPosition())
							.subtract(BigDecimal.ONE);
					needLoadNext = true;
					return current;
				}
			} else {
				if (current.compareTo(capacity) <= 0)
					return current;
				else {
					// 容量用完，下一块仍没加载完？
					long now = System.currentTimeMillis();
					while (System.currentTimeMillis() - now < properties.getRecordWait()) {
						try {
							Thread.sleep(properties.getRecordTaskRate());
						} catch (InterruptedException e) {
						}
						if (!needLoadNext) {
							current = record.getSecondPosition();
							capacity = BigDecimal.valueOf(record.getSecondChunkSize()).add(record.getSecondPosition())
									.subtract(BigDecimal.ONE);
							needLoadNext = true;
							return current;
						}
					}
					throw new SequenceRecordException(namespace);
				}
			}
		}
	}
}
