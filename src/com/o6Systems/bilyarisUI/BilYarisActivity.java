package com.o6Systems.bilyarisUI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import com.o6Systems.bilyarisUI.R;
import com.o6Systems.bilyarisAppFund.BilYarisAppEngine;
import com.o6Systems.appFundamentals.AppEngine;
import com.o6Systems.appFundamentals.AppState;
import com.o6Systems.appFundamentals.AppStateObserver;
import com.o6Systems.appFundamentals.TimerTaskObserver;


public abstract class BilYarisActivity extends Activity implements AppStateObserver, TimerTaskObserver{

	AppEngine applicationEngine;
	BilYarisAppEngine byEngine;
	
	
	
	 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationEngine = BilYarisAppEngine.getInstance(); 
        byEngine = (BilYarisAppEngine)applicationEngine;
        initViews();
        onStateUpdated(applicationEngine.currentState);
    }
	 
	public abstract void initViews();
	
	public void sendPrompt(int prompt, int[] params){
		AppState state = applicationEngine.onUserPrompt(prompt,params);
		this.onStateUpdated(state);
	}
	
	public void sendPrompt(int prompt, String[] params){
		AppState state = applicationEngine.onUserPrompt(prompt,params);
		this.onStateUpdated(state);
	}
	
	public String readFromFile(String fileName){
		String content = "";
	
		AssetManager assetManager =  this.getAssets(); 
		InputStream is;
		try {
			is = assetManager.open(fileName);
			Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
			content+= s.next();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    
		return content;
	}
	
	  
	
	public void onTimer(){
		// Do nothing in default;
	}
	
}
