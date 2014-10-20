package com.o6Systems.bilyarisUI;

import com.o6Systems.appFundamentals.AppState;
import com.o6Systems.bilyarisAppFund.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class UserCreateActivity extends BilYarisActivity implements OnClickListener {
	
	Button btnUserOK;
	Button btnAddFacebook;
	Button btnCancelUser;
	TextView edUserName;
	
	final static int TAG_USER_OK = 6;
	final static int TAG_ADD_FACEBOOK = 7;
	final static int TAG_CANCEL_USER = 8;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStateUpdated(AppState currentState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_user_create);
		btnUserOK = (Button)findViewById(R.id.btnUserOK);
		btnUserOK.setTag(TAG_USER_OK);
		btnUserOK.setOnClickListener(this);
		btnAddFacebook = (Button)findViewById(R.id.btnAddFacebook);
		btnAddFacebook.setTag(TAG_ADD_FACEBOOK);
		btnAddFacebook.setOnClickListener(this);
		btnCancelUser = (Button)findViewById(R.id.btnCancelUser);
		btnCancelUser.setTag(TAG_CANCEL_USER);
		btnCancelUser.setOnClickListener(this);
		
		edUserName = (TextView)findViewById(R.id.edUserName);
		
	}

	@Override
	public void onClick(View v) {
		Button clickedButton = (Button)v;
		int buttonTag = (Integer)clickedButton.getTag();
		
		switch(buttonTag){
			case TAG_USER_OK:
				String userName = edUserName.getText().toString();
				User user = new User(userName);
				byEngine.addUser(user);
				byEngine.currentBYState.setCurrentUser(user);
				startActivity(new Intent(getApplicationContext(), UserLoginActivity.class));
				finish();
				break;
			case TAG_ADD_FACEBOOK:
				break;
			case TAG_CANCEL_USER:
				break;
			default:
				break;
		}
		
	}
	
	@Override
	public void onBackPressed(){
		// Do nothing
	}
	
}
