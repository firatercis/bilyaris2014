package com.o6Systems.uiFundementalsAndroid;

import java.util.ArrayList;

import com.o6Systems.bilyarisUI.R;
import com.o6Systems.appFundamentals.AppEngine;
import com.o6Systems.appFundamentals.AppState;
import com.o6Systems.appFundamentals.AppStateObserver;
import com.o6Systems.appFundamentals.UIEngine;
import com.o6Systems.appFundamentals.UserInputObserver;

import android.app.Activity;
import android.os.Bundle;


public abstract class O6BaseActivity extends Activity implements AppStateObserver{
	// Since we cannot extend to two class, every content in class UIEngine is copied here.
	 protected static AppEngine activityAppEngine; // those will be used as Singleton Objects (is singleton evil?)
	 protected static AndroidAppUIEngine activityUIEngine;
	 public ArrayList<UserInputObserver> userInputObservers;
	 
	 public int activityMajorPageID;
	 private boolean addedToUIEngine = false;
	 private static boolean appEngineStarted = false;
	 
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
	     super.onCreate(savedInstanceState); 
	     //Connect App Engine and UI Engine
	     userInputObservers = new ArrayList<UserInputObserver>();
	     
	     if(activityAppEngine == null){
	    	  createAppEngine();
	    	  createUIEngine();
	 	     activityAppEngine.connectUIEngine(activityUIEngine);
	 	   
	    	  
	     }
	     if(!addedToUIEngine){
	    	// activityUIEngine.addPage(this);
	    	 activityUIEngine.currentPage = this;
	    	 addedToUIEngine = true;
	    	 this.addUserInputObserver(activityAppEngine);
	     }
	     if(!appEngineStarted){
	    	 activityAppEngine.start();
	    	 appEngineStarted = true;
	     }
	     
	 }
	 
	 public abstract void createAppEngine();
	 public abstract void createUIEngine();
	
	 public void sendPromptAll(String prompt){
			for(UserInputObserver currentObserver: userInputObservers){
				currentObserver.onUserPrompt(prompt);
			}
		}
	 
	 public void sendPromptAll(int prompt, int[] params){
			for(UserInputObserver currentObserver: userInputObservers){
				currentObserver.onUserPrompt(prompt,params);
			}
		}
	 
	 public void addUserInputObserver(UserInputObserver observer){
			userInputObservers.add(observer);
		}
	
	 public abstract void onStateUpdated(AppState currentState);
}
