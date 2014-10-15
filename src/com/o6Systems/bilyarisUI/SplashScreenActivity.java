package com.o6Systems.bilyarisUI;

import java.io.IOException;

import com.o6Systems.appFundamentals.AppState;
import com.o6Systems.appFundamentals.AppTimerTask;
import com.o6Systems.bilyarisAppFund.BYDatabaseInterface;
import com.o6Systems.bilyarisAppFund.BilYarisAppEngine;
import com.o6Systems.bilyarisAppFund.CreatorInfo;
import com.o6Systems.bilyarisUI.sqlite.BilYarisSQLiteHelper;
import com.o6Systems.utils.net.FTPModule;
import com.o6Systems.utils.net.HTTPModule;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class SplashScreenActivity extends BilYarisActivity {
	
	final static int MIN_SPLASH_SCREEN_TIME_SEC = 2;
	int timeRemaining = MIN_SPLASH_SCREEN_TIME_SEC;
	static volatile boolean appInit = false;
	
	final static String FTP_USER_NAME = "ftpadmin";
	final static String FTP_PASSWORD = "ftppass";
	final static String FTP_SERVER = "31.210.54.136";
	
	InitApplicationTask initTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		System.out.println("Starting application");
		
		if(appInit == false){
			System.out.println("Init Application");
			initTask = new InitApplicationTask();
			initTask.execute((Void[])null);
			//initAppEngine();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		
		return true;
	}

	@Override
	public void onStateUpdated(AppState currentState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initViews() {
		timeRemaining = MIN_SPLASH_SCREEN_TIME_SEC;
		AppTimerTask.getInstance().addObserver(this);
	}
	
	@Override
	public void onTimer(){
		if(timeRemaining <= 0 && appInit){
			AppTimerTask.getInstance().removeObserver(this);
			startActivity(new Intent(getApplicationContext(), CategoryActivity.class));
			finish();
		}else{
			timeRemaining --;
		}
	}
	
	private void initAppEngine(){
		
		String[] descriptions = fetchDescriptions();
		String cInfoDescription = descriptions[0];
		String qpDescription = descriptions[1];	
		BYDatabaseInterface dbInterface = new BilYarisSQLiteHelper(this);
		
		byEngine.initApplication(cInfoDescription,qpDescription,dbInterface,descriptionsUpToDate);
	}
	
	
	
	private class InitApplicationTask extends AsyncTask<Void, Void, Void> {
	     
		@Override
		protected Void doInBackground(Void... params) {
			initAppEngine();
			appInit = true;

			System.out.println("Application init");
			
			return null;
		}

	     
		
	 }
	 

}
