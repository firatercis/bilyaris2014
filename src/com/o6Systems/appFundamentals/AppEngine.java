package com.o6Systems.appFundamentals;


import java.util.ArrayList;

public abstract class AppEngine implements UserInputObserver{
	public AppState currentState;
	ArrayList<AppStateObserver> stateObservers;
	
	
	protected UIEngine uiEngine;
	
	public AppEngine(){
		stateObservers = new ArrayList<AppStateObserver>();
	}
	
	public void postUpdate(int updateLevel){
		currentState.updateLevel = updateLevel;
		postUpdate();
	}
	
	public void postUpdate(){
		for(AppStateObserver currentObserver:stateObservers){
			currentObserver.onStateUpdated(currentState);
		}
	}
	
	public void addStateObserver(AppStateObserver stateObverver){
		stateObservers.add(stateObverver);
	}
	
	public void connectUIEngine(UIEngine uiEngine){
		this.uiEngine = uiEngine;
		addStateObserver(uiEngine);
	}
	
	public void start(){
		postUpdate(AppState.FULL_UPDATE);
	}
	
}
