package com.o6Systems.bilyarisUI;

import android.os.Bundle;

import com.o6Systems.bilyarisUI.R;
import com.o6Systems.bilyarisAppFund.BilYarisAppEngine;
import com.o6Systems.appFundamentals.AppState;
import com.o6Systems.uiFundementalsAndroid.AndroidAppUIEngine;
import com.o6Systems.uiFundementalsAndroid.O6BaseActivity;

public abstract class BilYarisActivity extends O6BaseActivity{


	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	    }
	@Override
	public void createAppEngine() {
		activityAppEngine = new BilYarisAppEngine();
	}

	@Override
	public void createUIEngine() {
		activityUIEngine = new AndroidAppUIEngine();
		
	}


}
