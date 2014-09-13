package com.o6Systems.bilyarisUI;

import com.o6Systems.bilyarisUI.R;
import com.o6Systems.bilyarisAppFund.BilYarisAppEngine;
import com.o6Systems.bilyarisAppFund.BilYarisAppState;
import com.o6Systems.bilyarisAppFund.Question;
import com.o6Systems.appFundamentals.AppState;
import com.o6Systems.uiFundementalsAndroid.O6BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class QuestionActivity extends BilYarisActivity implements OnClickListener{
		
	TextView lbQuestionText;
	Button btnChoice0;
	Button btnChoice1;
	Button btnChoice2;
	Button btnChoice3;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        initViews();
       // activityAppEngine.start();
        
        this.onStateUpdated(activityAppEngine.currentState);
        
    }
    
    public void initViews(){
    	lbQuestionText = (TextView)findViewById(R.id.lbQuestionText);
    	btnChoice0 = (Button)findViewById(R.id.btnChoice0);
    	btnChoice1 = (Button)findViewById(R.id.btnChoice1);
    	btnChoice2 = (Button)findViewById(R.id.btnChoice2);
    	btnChoice3 = (Button)findViewById(R.id.btnChoice3);
    	
    	btnChoice0.setOnClickListener(this);
    	btnChoice1.setOnClickListener(this);
    	btnChoice2.setOnClickListener(this);
    	btnChoice3.setOnClickListener(this);
    }
    
    public void printQuestion(Question Q){
    	lbQuestionText.setText(Q.text);
    	btnChoice0.setText(Q.getAlternative(0));
    	btnChoice1.setText(Q.getAlternative(1));
    	btnChoice2.setText(Q.getAlternative(2));
    	btnChoice3.setText(Q.getAlternative(3));
    }

	@Override
	public void onStateUpdated(AppState currentState) {
		
		
		if(currentState.majorStateID == BilYarisAppEngine.ES_WAITING_CHOICE){
			
			BilYarisAppState currentBYState = (BilYarisAppState)currentState;
			printQuestion(currentBYState.getCurrentQuestion());
			
			
		}else if(currentState.majorStateID == BilYarisAppEngine.ES_PRINTING_TEXT){
			startActivity(new Intent(getApplicationContext(), ResultActivity.class));
			finish();
		}else if(currentState.majorStateID == BilYarisAppEngine.ES_CATEGORY_SELECTION){
			startActivity(new Intent(getApplicationContext(), CategoryActivity.class));
			finish();
		}
	
	}

	@Override
	public void onClick(View v) {
		String tag = (String) v.getTag();
		int choiceID = Integer.parseInt(tag);
		int prompt = BilYarisAppEngine.UP_CHOICE;
		int[] params = new int[1];
		
		params[0] = choiceID;
		sendPromptAll(prompt,params);
		
	}
	
	@Override
	public void onBackPressed(){
		int prompt = BilYarisAppEngine.UP_CANCEL;
		int[] params = null;
		sendPromptAll(prompt,params);
	}
	
    
    
}
