package com.o6Systems.appFundamentals;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
public class AppTimerTask extends TimerTask{
	
	final static long DEFAULT_APP_TIMER_PERIOD = 1000;
	
	long period;
	long delay;
	
	Timer timer;
	ArrayList<TimerTaskObserver> observers;
	
	TimerTaskObserver currentObserver;
	
	private static AppTimerTask instance;
	
	private boolean isRunning;
	
	
	private AppTimerTask(long period){
		observers = new ArrayList<TimerTaskObserver>();
		timer = new Timer();
		this.period = period;
		start(0,period);
	}
	
	public void addObserver(TimerTaskObserver observer){
		//observers.add(observer);
		currentObserver = observer;
	}
	public void removeObserver(TimerTaskObserver observer){
		//observers.remove(observer);
		currentObserver = null;
	}
	
	public void start(long delay, long period){
		
		timer.schedule(this,delay,period );
	}
	
	@Override
	public void run() {
		//for(TimerTaskObserver currentObserver:observers){
		if(currentObserver  != null){
			currentObserver.onTimer();
		}
			
		//}
	}
	
	public static AppTimerTask getInstance(){
		if(instance == null){
			instance = new AppTimerTask(DEFAULT_APP_TIMER_PERIOD);
		}
		
		return instance;
	}

}
