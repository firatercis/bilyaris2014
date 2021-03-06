package com.o6Systems.bilyarisUI;

import java.util.Currency;

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
	TextView lbQuestionIndex;
	Button btnChoice0;
	Button btnChoice1;
	Button btnChoice2;
	Button btnChoice3;
	Button btnFiftyPercent;
	Button btnAddTime;
	Button btnStatistics;
	Button btnDoubleAnswer;
	
	Button[] choiceButtons=new Button[Question.N_POSSIBLE_ANSWERS];
     Button[] jokerButtons = new Button[4];
	
	
	boolean timerInitiated = false;
	
	// State variable for highlighting selected answer
	
	final static int GUI_STATE_WAITING_USER = 0;
	final static int GUI_STATE_WAITING_ENGINE = 1;
	final static int GUI_WAITING_ENGINE_SECS = 1;
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
    	lbQuestionIndex = (TextView)findViewById(R.id.lbQuestionIndex);
    	btnChoice0 = (Button)findViewById(R.id.btnChoice0);
    	btnChoice1 = (Button)findViewById(R.id.btnChoice1);
    	btnChoice2 = (Button)findViewById(R.id.btnChoice2);
    	btnChoice3 = (Button)findViewById(R.id.btnChoice3);
    	btnFiftyPercent = (Button) findViewById(R.id.btnFiftyPercent);
    	btnAddTime = (Button)findViewById(R.id.btnAddTime);
    	btnStatistics = (Button) findViewById(R.id.btnStatistics);
    	btnDoubleAnswer = (Button)findViewById(R.id.btnDoubleAnswer);
    	
    	choiceButtons[0] = btnChoice0;
    	choiceButtons[1] = btnChoice1;
    	choiceButtons[2] = btnChoice2;
    	choiceButtons[3] = btnChoice3;
    	
    	for(int i=0;i<choiceButtons.length;i++){
    		choiceButtons[i].setOnClickListener(this);
    	}
    	
    	jokerButtons[0] = btnFiftyPercent;
    	jokerButtons[1] = btnAddTime;
    	jokerButtons[2] = btnStatistics;
    	jokerButtons[3] = btnDoubleAnswer;
    	
    	for(int i=0; i<jokerButtons.length;i++){
    		jokerButtons[i].setOnClickListener(this);
    	}
    	
    	if(!timerInitiated){
    		AppTimerTask.getInstance().addObserver(this);
        	timerInitiated = true;
    	}
    	guiStateID = GUI_STATE_WAITING_USER;
    	
    }
    
    private void overLayerButton(Button btn, int overDrawableID){
    	android.content.res.Resources r = getResources();
    	Drawable[] layers = new Drawable[2];
    	layers[0] = btn.getBackground();
    	layers[1] = r.getDrawable(overDrawableID);
    	LayerDrawable layerDrawable = new LayerDrawable(layers);
    	btn.setBackground(layerDrawable);
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
			
			if(guiStateID == GUI_STATE_WAITING_USER){
				prompt = BilYarisAppEngine.UP_CHOICE;
				params[0] = choiceID;
				
				bufferSelection(prompt,params);
				highlightButton(clickedButton);
				guiStateID = GUI_STATE_WAITING_ENGINE;
			}
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
    
    private void updateQuestionIndex(final int index){
    	lbQuestionIndex.setText("Soru " + index);
    }
    
    private void updateTimeLabel(final int time){
    	lbTimer.setText("" + time);
    }
    
    public void printQuestion(Question Q){
    	BilYarisAppState currentBYState = (BilYarisAppState)applicationEngine.currentState;
    	
    	ChoiceState[] choiceStates = currentBYState.getChoiceStates();
    	
    	String questionText = "#"+Q.getID()+":";
    	questionText+=Q.text;
    	lbQuestionText.setText(questionText);
    	//unHighlightButtons();
    	int[] statistics = currentBYState.getStatistics();
    	for(int i=0; i<choiceStates.length; i++){
    		if(choiceStates[i] == ChoiceState.NORMAL){
    			choiceButtons[i].setVisibility(View.VISIBLE);
    			
    			String choiceText = Q.getAlternative(i);
    			
    			if(statistics != null){
    				choiceText += "(%" + statistics[i] + ")";
    			}
    			
    			choiceButtons[i].setText(choiceText);
    		}
    		else if(choiceStates[i] == ChoiceState.HIDDEN){
    			choiceButtons[i].setVisibility(View.INVISIBLE);
    		}else if(choiceStates[i] == ChoiceState.HIGHLIGHTED){
    			highlightButton(choiceButtons[i]);
    		}else if(choiceStates[i] == ChoiceState.HIGHLIGHTED_WRONG){
    			overLayerButton(choiceButtons[i], R.drawable.discardchoice);
    		}
    	}
    	
    	updateQuestionIndex(currentBYState.getQuestionIndex());
    	
    	
    	// print jokers
    	printJokers(currentBYState);
    	
    	
    }
    
    public void printJokers(BilYarisAppState currentBYState){
    	
    	for(int i=0; i<jokerButtons.length; i++){
    		if (currentBYState.getJokerAvailable(i) == false){
    			overLayerButton(jokerButtons[i],R.drawable.discardjoker);
    		}
    	}
    	
    }
    
    private void playSound(BilYarisAppState currentBYState){
    	int soundID = currentBYState.getCurrentSoundID();
    	MediaPlayer mp = null;
    	switch(soundID){
	    	case BilYarisAppState.NO_SOUND:
	    		break;
	    	case BilYarisAppState.SOUND_CORRECT:
	    		mp = MediaPlayer.create(getApplicationContext(), R.raw.correct);
	    		break;
	    	case BilYarisAppState.SOUND_WRONG:
	    		mp = MediaPlayer.create(getApplicationContext(), R.raw.wrong);
	    		break;
	    	default:
	    			break;
    	}
    	currentBYState.clearSound();
    	
    	if(mp != null){
    		mp.start();
    	}
    }
    
   
    
	@Override
	public void onStateUpdated(AppState currentState) {
		
		BilYarisAppState currentBYState = (BilYarisAppState)currentState;
		if(currentState.majorStateID == BilYarisAppEngine.ES_WAITING_CHOICE){	
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
		
		// Play the sound
		playSound(currentBYState);
		
	
	}

	@Override
	public void onBackPressed(){
		int prompt = BilYarisAppEngine.UP_CANCEL;
		int[] params = null;
		sendPrompt(prompt,params);
	}
	
    
    
}
