package com.o6Systems.bilyarisAppFund;

import java.util.ArrayList;
import java.util.List;

import com.o6Systems.bilyarisAppFund.jokers.BilYarisJoker;
import com.o6Systems.bilyarisAppFund.jokers.FiftyPercentJoker;
import com.o6Systems.appFundamentals.AppState;

public class BilYarisAppState extends AppState {
	
	final static int DEFAULT_N_CHOICES = 4;

	Question currentQuestion;
	String currentCategory;
	
	// TODO: Will be changed to UserStats object
	int userScore;
	int questionIndex;
	int userRemainingTime;
	boolean gameOver;
	
	
	public String appMessage;
	List<String> categories;
	
	ArrayList<BilYarisJoker> jokers;
	
	boolean[] choicesAvailable;
	
	
	public BilYarisAppState(){
		reset();
		
	}
	
	private void initJokers(){
		jokers=new ArrayList<BilYarisJoker>();
		// Add jokers
		jokers.add(new FiftyPercentJoker());
	}
	
	public void eliminateChoice(int choiceID){
		choicesAvailable[choiceID] = false; 
	}
	
	private void resetChoicesAvailable(){
		
		if(choicesAvailable == null)
			choicesAvailable = new boolean[DEFAULT_N_CHOICES];
		
		for(int i=0; i< choicesAvailable.length; i++){
			choicesAvailable[i] = true;
		}
	}
	
	public void reset(){
		questionIndex = 0;
		userScore = 0;
		gameOver = false;
		initJokers();
	}
	
	public void applyJoker(int jokerID){
		BilYarisJoker joker = jokers.get(jokerID);
		joker.apply(this);
	}
	
	public void setQuestion(Question Q){
		currentQuestion = Q;
		resetChoicesAvailable();
		// TODO: Zaman ile ilgili kisimlar gelecek.
	}
	
	public void processAnswer(int answerID){
		if(currentQuestion.getAnswer() == answerID){
			userScore ++;
		}else{
			gameOver = true;
		}
	}
	
	public boolean jokerAvailable(int jokerID){
		return jokers.get(jokerID).available();
	}
	
	public boolean[] getChoicesAvailable(){
		return choicesAvailable;
	}
	
	public Question getCurrentQuestion(){
		return currentQuestion;
	}
	
}
