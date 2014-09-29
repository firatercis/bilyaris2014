package com.o6Systems.bilyarisUI;

import com.o6Systems.bilyarisUI.R;
import com.o6Systems.bilyarisAppFund.BilYarisAppEngine;
import com.o6Systems.bilyarisAppFund.BilYarisAppState;
import com.o6Systems.bilyarisAppFund.BilYarisAppState.ChoiceState;
import com.o6Systems.bilyarisAppFund.Question;
import com.o6Systems.appFundamentals.AppState;
import com.o6Systems.appFundamentals.AppTimerTask;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class QuestionActivity extends BilYarisActivity implements OnClickListener{
	
	
	TextView lbQuestionText;
	TextView lbTimer;
	Button btnChoice0;
	Button btnChoice1;
	Button btnChoice2;
	Button btnChoice3;
	Button btnFiftyPercent;
	Button btnAddTime;
	Button[] choiceButtons=new Button[Question.N_POSSIBLE_ANSWERS];
    
	boolean timerInitiated = false;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        
    }

	
    public void initViews(){
    	setContentView(R.layout.activity_question);
    	lbQuestionText = (TextView)findViewById(R.id.lbQuestionText);
    	lbTimer = (TextView)findViewById(R.id.lbTimer);
    	btnChoice0 = (Button)findViewById(R.id.btnChoice0);
    	btnChoice1 = (Button)findViewById(R.id.btnChoice1);
    	btnChoice2 = (Button)findViewById(R.id.btnChoice2);
    	btnChoice3 = (Button)findViewById(R.id.btnChoice3);
    	btnFiftyPercent = (Button) findViewById(R.id.btnFiftyPercent);
    	btnAddTime = (Button)findViewById(R.id.btnAddTime);
    	
    	
    	btnChoice0.setOnClickListener(this);
    	btnChoice1.setOnClickListener(this);
    	btnChoice2.setOnClickListener(this);
    	btnChoice3.setOnClickListener(this);
    	btnFiftyPercent.setOnClickListener(this);
    	btnAddTime.setOnClickListener(this);
    	
    	choiceButtons[0] = btnChoice0;
    	choiceButtons[1] = btnChoice1;
    	choiceButtons[2] = btnChoice2;
    	choiceButtons[3] = btnChoice3;
    	
    	if(!timerInitiated){
    		AppTimerTask.getInstance().addObserver(this);
        	timerInitiated = true;
    	}
    	
    }
    
    public void onTimer(){	
    	
		System.out.println("QuestionActivity: Timer event received!");
    	final int promptID = BilYarisAppEngine.UP_TIMER;
    	final int[] params = null;
    	
    	
    	runOnUiThread(new Runnable(){
			@Override
			public void run() {
				//AppState state =  applicationEngine.onUserPrompt(promptID,params);
		    	//final BilYarisAppState bystate = (BilYarisAppState) state;
				sendPrompt(promptID, params);
				//updateTimeLabel(bystate.getUserRemainingTime());
			
			}
    	});
    	
    }
    
    private void updateTimeLabel(final int time){
    	lbTimer.setText("" + time);
    }
    
    public void printQuestion(Question Q){
    	BilYarisAppState currentBYState = (BilYarisAppState)applicationEngine.currentState;
    	
    	ChoiceState[] choiceStates = currentBYState.getChoiceStates();
    	
    	lbQuestionText.setText(Q.text);
    	for(int i=0; i<choiceStates.length; i++){
    		if(choiceStates[i] == ChoiceState.NORMAL){
    			choiceButtons[i].setVisibility(View.VISIBLE);
    			choiceButtons[i].setText(Q.getAlternative(i));
    		}
    		else if(choiceStates[i] == ChoiceState.HIDDEN){
    			choiceButtons[i].setVisibility(View.INVISIBLE);
    		}
    	}
    	
    	// print jokers
    	
    	// Print timer
    	
    	
    }

	@Override
	public void onStateUpdated(AppState currentState) {
		
		
		if(currentState.majorStateID == BilYarisAppEngine.ES_WAITING_CHOICE){
			
			BilYarisAppState currentBYState = (BilYarisAppState)currentState;
			if(currentBYState.updateLevel == AppState.FULL_UPDATE){
				printQuestion(currentBYState.getCurrentQuestion());
			}
			
			updateTimeLabel(currentBYState.getUserRemainingTime());
			
			
		}else if(currentState.majorStateID == BilYarisAppEngine.ES_PRINTING_TEXT){
			startActivity(new Intent(getApplicationContext(), ResultActivity.class));
			AppTimerTask.getInstance().removeObserver(this);
			finish();
		}else if(currentState.majorStateID == BilYarisAppEngine.ES_CATEGORY_SELECTION){
			startActivity(new Intent(getApplicationContext(), CategoryActivity.class));
			AppTimerTask.getInstance().removeObserver(this);
			finish();
		}
	
	}

	@Override
	public void onClick(View v) {
		String tag = (String) v.getTag();
		int choiceID = Integer.parseInt(tag);
		
		int prompt=0; 
		int[] params = new int[1];
		if(choiceID < 10){
			prompt = BilYarisAppEngine.UP_CHOICE;
			params[0] = choiceID;
		}else{
			prompt = BilYarisAppEngine.UP_JOKER;
			params[0] = choiceID -10;
		}
		
		
		
		
	
		//sendPromptAll(prompt,params);
		
		AppState state = BilYarisAppEngine.getInstance().onUserPrompt(prompt,params);
		onStateUpdated(state);
	}
	
	@Override
	public void onBackPressed(){
		int prompt = BilYarisAppEngine.UP_CANCEL;
		int[] params = null;
		sendPrompt(prompt,params);
	}
	
    
    
}
