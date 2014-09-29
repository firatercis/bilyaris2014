package com.o6Systems.bilyarisUI;

import com.o6Systems.bilyarisUI.R;
import com.o6Systems.bilyarisUI.sqlite.BilYarisSQLiteHelper;
import com.o6Systems.bilyarisAppFund.BYDatabaseInterface;
import com.o6Systems.bilyarisAppFund.BilYarisAppEngine;
import com.o6Systems.bilyarisAppFund.BilYarisAppState;
import com.o6Systems.appFundamentals.AppState;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CategoryActivity extends BilYarisActivity implements OnClickListener{
	
	BilYarisAppState currentByState;
	final static int N_CATEGORIES = 10;
	
	Button btnCategory1;
	Button btnCategory2;
	Button btnCategory3;
	Button btnCategory4;
	Button btnCategory5;
	Button btnCategory6;
	Button btnCategory7;
	Button btnCategory8;
	Button btnCategory9;
	Button btnCategory10;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public void onClick(View v) {
		
		BilYarisSQLiteHelper sqlitehelper = new BilYarisSQLiteHelper(this);
		SQLiteDatabase db = sqlitehelper.getReadableDatabase();
		sqlitehelper.printQuestionDatabase(db);
	
		String tag = (String) v.getTag();
		int categoryID = Integer.parseInt(tag);
		int prompt = BilYarisAppEngine.UP_CATEGORY_SELECT;
		int[] params = new int[1];
		
		params[0] = categoryID;
		sendPrompt(prompt,params);
		
	}

	@Override
	public void onStateUpdated(AppState currentState) {
		if(currentState.majorStateID == BilYarisAppEngine.ES_WAITING_CHOICE){
			startActivity(new Intent(getApplicationContext(), QuestionActivity.class));
			finish();
		}
		
		if(currentState.majorStateID == BilYarisAppEngine.ES_INIT){
			initAppEngine();
		}
		
	}
	
	private void initAppEngine(){
		String cInfoDescription = readFromFile(BilYarisAppEngine.CREATOR_INFO_DEFAULT_FILENAME);
		String qpDescription = readFromFile(BilYarisAppEngine.QUESTION_PACK_DEFAULT_FILENAME);
		//int promptID = BilYarisAppEngine.UP_INIT;
		//String[] params = {cInfoDescription,qpDescription};
		//sendPrompt(promptID, params);
		
		BYDatabaseInterface dbInterface = new BilYarisSQLiteHelper(this);
		
		byEngine.initApplication(cInfoDescription,qpDescription,dbInterface);
	}



	@Override
	public void initViews() {
setContentView(R.layout.activity_category);
		
		btnCategory1 = (Button)findViewById(R.id.btnCategory1);
		btnCategory2 = (Button)findViewById(R.id.btnCategory2);
		btnCategory3 = (Button)findViewById(R.id.btnCategory3);
		btnCategory4 = (Button)findViewById(R.id.btnCategory4);
		btnCategory5 = (Button)findViewById(R.id.btnCategory5);
		btnCategory6 = (Button)findViewById(R.id.btnCategory6);
		btnCategory7 = (Button)findViewById(R.id.btnCategory7);
		btnCategory8 = (Button)findViewById(R.id.btnCategory8);
		btnCategory9 = (Button)findViewById(R.id.btnCategory9);
		btnCategory10 = (Button)findViewById(R.id.btnCategory10);
		
		
		btnCategory1.setOnClickListener(this);
		btnCategory2.setOnClickListener(this);
		btnCategory3.setOnClickListener(this);
		btnCategory4.setOnClickListener(this);
		btnCategory5.setOnClickListener(this);
		btnCategory6.setOnClickListener(this);
		btnCategory7.setOnClickListener(this);
		btnCategory8.setOnClickListener(this);
		btnCategory9.setOnClickListener(this);
		btnCategory10.setOnClickListener(this);
		
		
	}

	
}
