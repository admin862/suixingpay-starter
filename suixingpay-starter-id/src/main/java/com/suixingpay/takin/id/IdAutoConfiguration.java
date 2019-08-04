package com.suixingpay.takin.id;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.suixingpay.takin.id.IdProperties.IdConfig;
import com.suixingpay.takin.id.sequence.RecordSequenceManager;
import com.suixingpay.takin.id.sequence.SequenceRecord;
import com.suixingpay.takin.id.sequence.TimeSequenceManager;
import com.suixingpay.takin.id.serverid.RedisServerIdProvider;
import com.suixingpay.takin.redis.IRedisOperater;

/**
 * 
 * @author renjinhao
 *
 */
@Configuration
@EnableConfigurationProperties(IdProperties.class)
@ConditionalOnProperty(value = "suixingpay.id.useDefaultImpl", matchIfMissing = true)
public class IdAutoConfiguration {

	@Autowired
	private IdProperties properties;

	@Bean
	@ConditionalOnMissingBean
	public ServerIdProvider serverIdProvider(IRedisOperater redis) {
		return new RedisServerIdProvider(redis);
	}

	@Bean
	public IdGenerator idGenerator(SequenceManager seqManager, ServerIdProvider serverIdProvider) {
		return new DefaultIdGeneratorImpl(seqManager, serverIdProvider);
	}

	@Configuration
	@ConditionalOnProperty(value = "suixingpay.id.sequenceStrategy", havingValue = "time", matchIfMissing = true)
	public class TimeSequenceConfiguration {
		@Bean
		public SequenceManager seqManager() {
			return new TimeSequenceManager(properties);
		}
	}

	@Configuration
	@ConditionalOnProperty(value = "suixingpay.id.sequenceStrategy", havingValue = "record")
	public class RecordSequenceConfiguration {

		@PostConstruct
		public void check() throws IOException {
			for (Entry<String, IdConfig> entry : properties.getConfigs().entrySet()) {
				IdConfig config = entry.getValue();
				if (Integer.valueOf((config.getChunkSize() * 2)).toString().length() > config.getSeqLength())
					throw new IllegalArgumentException(
							"namespace:" + entry.getKey() + "，(chunksize*2).length不能大于seqLength");
			}

			File f = new File(properties.getSeqFilePath());
			if (!f.exists())
				f.createNewFile();
		}

		@Bean
		public SequenceRecord sequenceRecord() throws IOException {
			return new SequenceRecord(properties);
		}

		@Bean
		public RecordSequenceManager seqManager(SequenceRecord record) throws IOException {
			return new RecordSequenceManager(properties, record);
		}
	}
}
