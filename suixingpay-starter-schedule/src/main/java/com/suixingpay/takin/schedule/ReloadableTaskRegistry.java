package com.suixingpay.takin.schedule;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author renjinhao
 *
 */
public class ReloadableTaskRegistry {

	private static List<ReloadableTask> activeTasks = new ArrayList<>();
	private static List<ReloadableTask> inactiveTasks = new ArrayList<>();

	public List<ReloadableTask> getActiveTasks() {
		List<ReloadableTask> tasks = new ArrayList<>();
		for (int i = activeTasks.size() - 1; i >= 0; i--) {
			tasks.add(activeTasks.remove(i));
		}
		return tasks;
	}

	public List<ReloadableTask> getInactiveTasks() {
		List<ReloadableTask> tasks = new ArrayList<>();
		for (int i = inactiveTasks.size() - 1; i >= 0; i--) {
			tasks.add(inactiveTasks.remove(i));
		}
		return tasks;
	}

	public void addActiveTask(String name, ScheduledTask task, Method method, Object bean) {
		ReloadableTask reloadableTask = new ReloadableTask(name, task, method, bean);
		activeTasks.add(reloadableTask);
	}

	public void addInactiveTask(String name, ScheduledTask task, Method method, Object bean) {
		ReloadableTask reloadableTask = new ReloadableTask(name, task, method, bean);
		inactiveTasks.add(reloadableTask);
	}

	static class ReloadableTask {
		String name;
		ScheduledTask task;
		Method method;
		Object bean;

		public ReloadableTask(String name, ScheduledTask task, Method method, Object bean) {
			super();
			this.name = name;
			this.task = task;
			this.method = method;
			this.bean = bean;
		}

		boolean cancel() {
			return task.cancel(false);
		}
	}
}
