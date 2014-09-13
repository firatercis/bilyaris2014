package com.o6Systems.bilyarisUI;


import com.o6Systems.bilyarisAppFund.BilYarisAppEngine;
import com.o6Systems.bilyarisAppFund.BilYarisAppState;
import com.o6Systems.appFundamentals.AppState;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ResultActivity extends BilYarisActivity implements OnClickListener{
	
	TextView lbResult;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		lbResult = (TextView) findViewById(R.id.lbResult);
		lbResult.setOnClickListener(this);
		this.onStateUpdated(activityAppEngine.currentState);
		
	}

	@Override
	public void onStateUpdated(AppState currentState) {
		
		if(currentState.majorStateID == BilYarisAppEngine.ES_CATEGORY_SELECTION){
			startActivity(new Intent(getApplicationContext(), CategoryActivity.class));
			finish();
		}else if(currentState.majorStateID == BilYarisAppEngine.ES_PRINTING_TEXT){
			BilYarisAppState currentBYState = (BilYarisAppState) currentState;
			lbResult.setText(currentBYState.appMessage);
		}
	}

	@Override
	public void onClick(View v) {
		int prompt = BilYarisAppEngine.UP_OK;
		int[] params = null;
		sendPromptAll(prompt,params);
	}
	
	@Override
	public void onBackPressed(){
		// Do nothing
	}
	
	
	
}
