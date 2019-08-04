package com.suixingpay.takin.id;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 
 * @author renjinhao
 *
 */
@Data
@ConfigurationProperties(prefix = "suixingpay.id")
public class IdProperties {
	private String seqFilePath = "./seq.json";
	private int recordTaskDelay = 10;
	private int recordTaskRate = 5;
	private int recordWait = 1000;
	private String sequenceStrategy = "time";
	private Map<String, IdConfig> configs = new HashMap<>();

	@Data
	public static class IdConfig {
		private Integer chunkSize = 10000;
		private Integer seqLength = 8;
		private Integer capacityPs = 6;
	}
}
