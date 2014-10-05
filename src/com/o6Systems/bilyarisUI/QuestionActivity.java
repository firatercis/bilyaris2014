package com.o6Systems.bilyarisUI;

import com.google.common.io.Resources;
import com.o6Systems.bilyarisUI.R;
import com.o6Systems.bilyarisAppFund.BilYarisAppEngine;
import com.o6Systems.bilyarisAppFund.BilYarisAppState;
import com.o6Systems.bilyarisAppFund.BilYarisAppState.ChoiceState;
import com.o6Systems.bilyarisAppFund.Question;
import com.o6Systems.appFundamentals.AppState;
import com.o6Systems.appFundamentals.AppTimerTask;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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
	
	// State variable for highlighting selected answer
	
	final static int GUI_STATE_WAITING_USER = 0;
	final static int GUI_STATE_WAITING_ENGINE = 1;
	final static int GUI_WAITING_ENGINE_SECS = 3;
	int guiStateID;
	int waitingTickCounter = GUI_WAITING_ENGINE_SECS;
	int bufferedPromptID;
	int[] bufferedParams;
	
	// Sound related
	
	final static int SOUND_ID_CORRECT = 0;
	final static int SOUND_ID_WRONG = 1;
	
	
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
    	
    	guiStateID = GUI_STATE_WAITING_USER;
    	
    }
    
    private void highlightButton(Button btn){
    	android.content.res.Resources r = getResources();
    	Drawable[] layers = new Drawable[2];
    	layers[0] = btn.getBackground();
    	layers[1] = r.getDrawable(R.drawable.highlight);
    	LayerDrawable layerDrawable = new LayerDrawable(layers);
    	btn.setBackground(layerDrawable);
    }
    
    private void unHighlightButtons(){
    	Drawable drawableTemp = getResources().getDrawable(R.drawable.choice1);
    	btnChoice0.setBackground(drawableTemp);
    	
    	drawableTemp = getResources().getDrawable(R.drawable.choice2);
    	btnChoice1.setBackground(drawableTemp);
    	
    	drawableTemp = getResources().getDrawable(R.drawable.choice3);
    	btnChoice2.setBackground(drawableTemp);
   
    	drawableTemp = getResources().getDrawable(R.drawable.choice4);
    	btnChoice3.setBackground(drawableTemp);
    	
    	
    }
    
    private void startWaitingEngine(){
    	guiStateID = GUI_STATE_WAITING_ENGINE;
    }
    
    private void bufferSelection(int prompt, int[] params){
    	bufferedPromptID = prompt;
		bufferedParams = params;
    }
    
    @Override
	public void onClick(View v) {
		String tag = (String) v.getTag();
		int choiceID = Integer.parseInt(tag);
		Button clickedButton = (Button) v;
		
		int prompt=0; 
		int[] params = new int[1];
		if(choiceID < 10){
			prompt = BilYarisAppEngine.UP_CHOICE;
			params[0] = choiceID;
			bufferSelection(prompt,params);
			startWaitingEngine();
			highlightButton(clickedButton);
		}else{
			prompt = BilYarisAppEngine.UP_JOKER;
			params[0] = choiceID -10;
			AppState state = BilYarisAppEngine.getInstance().onUserPrompt(prompt,params);
			onStateUpdated(state);
		}
		
		//sendPromptAll(prompt,params);
		
		
	}


    @Override
	public void onTimer(){	
    	
    	final int promptID = BilYarisAppEngine.UP_TIMER;
    	final int[] params = null;
    	
    	
    	runOnUiThread(new Runnable(){
			@Override
			public void run() {
				if(guiStateID == GUI_STATE_WAITING_USER)
					sendPrompt(promptID, params);
				else if(guiStateID == GUI_STATE_WAITING_ENGINE){
					waitingTickCounter --;
					if(waitingTickCounter == 0){
						guiStateID = GUI_STATE_WAITING_USER;
						waitingTickCounter = GUI_WAITING_ENGINE_SECS;
						
						AppState state = BilYarisAppEngine.getInstance().onUserPrompt(bufferedPromptID,bufferedParams);
						onStateUpdated(state);
						unHighlightButtons();
					}
				}
									
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

    private void playSound(int soundID){
    	MediaPlayer mp=null;
    	
    	switch(soundID){
    	case SOUND_ID_CORRECT:
    		mp = MediaPlayer.create(getApplicationContext(), R.raw.correct);
    		break;
    	case SOUND_ID_WRONG:
    		mp = MediaPlayer.create(getApplicationContext(), R.raw.wrong);
    		break;
    	default:
    		break;
    	}
    	
    	if(mp != null){
    		mp.start();
    	}
    	
    }
    
	@Override
	public void onStateUpdated(AppState currentState) {
		
		
		if(currentState.majorStateID == BilYarisAppEngine.ES_WAITING_CHOICE){
			
			BilYarisAppState currentBYState = (BilYarisAppState)currentState;
			if(currentBYState.updateLevel == AppState.FULL_UPDATE){
				playSound(SOUND_ID_CORRECT);
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
	public void onBackPressed(){
		int prompt = BilYarisAppEngine.UP_CANCEL;
		int[] params = null;
		sendPrompt(prompt,params);
	}
	
    
    
}
