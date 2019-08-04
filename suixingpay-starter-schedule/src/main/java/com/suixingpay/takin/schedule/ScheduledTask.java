package com.suixingpay.takin.schedule;

import java.util.concurrent.ScheduledFuture;
/**
 * 
 * @author renjinhao
 *
 */
public class ScheduledTask {
	volatile ScheduledFuture<?> future;

	ScheduledTask() {
	}

	/**
	 * Trigger cancellation of this scheduled task.
	 */
	public boolean cancel(boolean mayInterruptIfRunning) {
		ScheduledFuture<?> future = this.future;
		if (future != null) {
			return future.cancel(mayInterruptIfRunning);
		}
		return true;
	}
}
