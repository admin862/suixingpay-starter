package com.suixingpay.takin.schedule;

import java.util.List;

/**
 * 
 * @author renjinhao
 *
 */
public interface HotLoadingSchedulesInfoProvider {

	List<String> getScheduleNames();

	String getInitialdelaystring(String scheduleName);

	String getCron(String scheduleName);
	
	String getFixeddelaystring(String scheduleName);
	
	String getFixedratestring(String scheduleName);
	
	boolean shouldRun(String scheduleName);
	
	boolean isDirty(String scheduleName);
}
