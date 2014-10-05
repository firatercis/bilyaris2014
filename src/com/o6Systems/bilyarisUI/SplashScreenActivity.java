package com.o6Systems.bilyarisUI;

import com.o6Systems.appFundamentals.AppState;
import com.o6Systems.appFundamentals.AppTimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class SplashScreenActivity extends BilYarisActivity {
	
	final static int MIN_SPLASH_SCREEN_TIME_SEC = 2;
	int timeRemaining;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
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
		timeRemaining--;
		if(timeRemaining == 0){
			startActivity(new Intent(getApplicationContext(), CategoryActivity.class));
			finish();
		}
	}

}
