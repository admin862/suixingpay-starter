package com.suixingpay.takin.schedule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author renjinhao
 *
 */
@Configuration
public class HotLoadingScheduleMarkerConfiguration {
	
	@Bean
	public Marker scheduleMarker() {
		return new Marker();
	}
	
	class Marker{
		
	}
}
