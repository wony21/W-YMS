package com.compact.base.scheduler;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;

public class CTQDynamicScheduler {
	
	private ThreadPoolTaskScheduler scheduler;
	
	public void startScheduler() {
		
	}
	
	public void stopScheduler() {
		
	}
	
	private Runnable getRunable() {
		return () -> {
			System.out.println(new Date());
		};
	}
	
	private Trigger getTrigger() {
		return new PeriodicTrigger(1, TimeUnit.SECONDS);
	}
	
}
