package com.suixingpay.takin.schedule;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.suixingpay.takin.schedule.ReloadableTaskRegistry.ReloadableTask;

/**
 * 定时任务热加载器
 * 
 * @author renjinhao
 *
 */
public abstract class ScheduleHotLoader {

	private static Log logger = LogFactory.getLog(ScheduleHotLoader.class);

	private Timer timer;

	abstract HotLoadingSchedulesInfoProvider getSchedulesInfo();

	abstract ReloadableTaskRegistry getReloadableTaskRegistry();

	abstract HotLoadingScheduleBeanPostProcessor getProcessor();

	abstract HotLoadingScheduleProperties getProperties();

	void start() {
		if (logger.isDebugEnabled()) {
			logger.debug("定时任务热加载任务启动");
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				List<ReloadableTask> inactiveTasks = getReloadableTaskRegistry().getInactiveTasks();
				for (ReloadableTask task : inactiveTasks) {
					getProcessor().processScheduled(task.name, task.method, task.bean);
				}
				List<ReloadableTask> activeTasks = getReloadableTaskRegistry().getActiveTasks();
				for (ReloadableTask task : activeTasks) {
					String name = task.name;
					if (getSchedulesInfo().isDirty(name)) {
						if (logger.isDebugEnabled()) {
							logger.debug("定时任务：" + name + "检测到变化");
						}
						if (task.cancel()) {
							if (logger.isDebugEnabled()) {
								logger.debug("定时任务：" + name + "原定义撤销成功");
							}
							getProcessor().processScheduled(name, task.method, task.bean);
						}
					} else {
						getReloadableTaskRegistry().addActiveTask(name, task.task, task.method, task.bean);
					}
				}
			}
		}, getProperties().getRefreshInitialDelay(), getProperties().getRefreshInterval());
	}

	void close() {
		if (timer != null)
			timer.cancel();
	}
}
