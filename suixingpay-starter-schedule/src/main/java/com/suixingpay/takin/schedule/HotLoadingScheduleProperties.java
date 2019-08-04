package com.suixingpay.takin.schedule;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 
 * @author renjinhao
 *
 */
@Data
@ConfigurationProperties(prefix = "suixingpay.schedule")
public class HotLoadingScheduleProperties {

	private Long refreshInitialDelay = 100000L;
	private Long refreshInterval = 30000L;
	private Boolean runInIsolation = false;
}
