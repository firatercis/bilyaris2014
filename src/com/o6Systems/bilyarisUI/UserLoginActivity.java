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
import android.widget.ImageView;
import android.widget.TextView;

public class UserLoginActivity extends BilYarisActivity implements OnClickListener {

	Button btnAddUser;
	Button btnDeleteUser;
	Button btnSelectUser;
	Button btnPrevUser;
	Button btnNextUser;
	TextView lbUserSelectName;
	
	User[] users;
	int currentUserID;
	ImageView imgUser;
	// TODO: ImgUser eklenecek.
	
	final static int TAG_ADD_USER = 1;
	final static int TAG_DELETE_USER = 2;
	final static int TAG_SELECT_USER = 3;
	final static int TAG_PREV_USER = 4;
	final static int TAG_NEXT_USER = 5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		//Fetch the users
		users = byEngine.getUsers();
		
		if(users==null || users.length == 0){
			gotoUserCreate();
		}else{
			User currentUser = byEngine.currentBYState.getCurrentUser();
			if(currentUser == null){
				currentUserID = 0;
			}else{
				currentUserID = currentUser.getUserID();
			}
			printCurrentUser();
		}
		
	}
	
	private void printCurrentUser(){
		
		lbUserSelectName.setText(users[currentUserID].getName());
	}
	
		
	@Override
	public void onStateUpdated(AppState currentState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initViews() {
		setContentView(R.layout.activity_user_login);
		btnAddUser = (Button) findViewById(R.id.btnAddUser);
		btnAddUser.setOnClickListener(this);
		btnAddUser.setTag(TAG_ADD_USER);
		btnDeleteUser = (Button) findViewById(R.id.btnDeleteUser);
		btnDeleteUser.setOnClickListener(this);
		btnDeleteUser.setTag(TAG_DELETE_USER);
		btnSelectUser = (Button) findViewById(R.id.btnSelectUser);
		btnSelectUser.setOnClickListener(this);
		btnSelectUser.setTag(TAG_SELECT_USER);
		btnPrevUser = (Button) findViewById(R.id.btnPrevUser);
		btnPrevUser.setOnClickListener(this);
		btnPrevUser.setTag(TAG_PREV_USER);
		btnNextUser = (Button) findViewById(R.id.btnNextUser);
		btnNextUser.setOnClickListener(this);
		btnNextUser.setTag(TAG_NEXT_USER);
		lbUserSelectName = (TextView)findViewById(R.id.lbUserSelectName);
		
		imgUser = (ImageView)findViewById(R.id.imgUser);
		imgUser.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userDBTest();
				
			}
		});
		// TODO: ImgUser eklenecek.
	}
	
	private void gotoUserCreate(){
		startActivity(new Intent(getApplicationContext(), UserCreateActivity.class));
		finish();
	}
	
	private void resetUserView(){

		printCurrentUser();
	}
	
	@Override
	public void onClick(View v) {
		Button clickedButton = (Button) v;
	
		switch((Integer)clickedButton.getTag()){
		case TAG_ADD_USER:
			System.out.println("Add user button clicked");
			gotoUserCreate();
			users = byEngine.getUsers();
			break;
		case TAG_DELETE_USER:
			byEngine.deleteUser(currentUserID);
			resetUserView();
			break;
		case TAG_SELECT_USER:
			byEngine.currentBYState.setCurrentUser(users[currentUserID]);
			startActivity(new Intent(getApplicationContext(), CategoryActivity.class));
			finish();
			break;
		case TAG_PREV_USER:
			
			if(currentUserID - 1 > 0){
				currentUserID --;
				printCurrentUser();
			}
			break;
		case TAG_NEXT_USER:
			
			if(currentUserID + 1 < users.length){
				currentUserID ++;
				printCurrentUser();
			}
			break;
		default:
			System.out.println("Other button clicked");
			userDBTest();
			break;
		}
		
	}
	
	public void userDBTest(){
		User user = new User("deneme1");
		byEngine.addUser(user);
		User[] users = byEngine.getUsers();
		for(int i=0; i<users.length;i++){
			System.out.println(users[i].getName() + "," +users[i].getUserID());
		}
		
	}
	
}
