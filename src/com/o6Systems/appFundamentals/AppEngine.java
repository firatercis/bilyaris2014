package com.o6Systems.appFundamentals;


import java.util.ArrayList;

import com.o6Systems.utils.database.DatabaseInterface;

public abstract class AppEngine implements UserInputObserver{
	public AppState currentState;
	ArrayList<AppStateObserver> stateObservers;
	protected DatabaseInterface dbInterface;
	
	protected AppEngine(){
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
	
	// Database
	public void setDatabaseInterface(DatabaseInterface dbInterface){
		this.dbInterface = dbInterface;
	}
	
	
	public void start(){
		postUpdate(AppState.FULL_UPDATE);
	}
	
}
