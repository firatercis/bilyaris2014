package com.o6Systems.bilyarisAppFund;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.o6Systems.bilyarisAppFund.jokers.BilYarisJoker;
import com.o6Systems.bilyarisAppFund.jokers.FiftyPercentJoker;
import com.o6Systems.appFundamentals.AppState;

public class BilYarisAppState extends AppState {
	
	final static int DEFAULT_N_CHOICES = 4;
	final static int DEFAULT_USER_TIME = 50;
	
	public enum ChoiceState{
		NORMAL,HIDDEN,HIGHLIGHTED
	}
	
	QuestionPack questionBase;
	QuestionPack currentQuestionPack;
	Question currentQuestion;
	String currentCategory;
	
	// TODO: Will be changed to UserStats object
	int userScore;
	int questionIndex;
	int userRemainingTime;
	boolean gameOver;
	
	private int tolerance;
	private int hintChoice;
	
	CreatorInfo creatorInfo;
	
	public String appMessage;
	List<String> categories;
	
	ArrayList<BilYarisJoker> jokers;
	
	ChoiceState[] choiceStates;
	
	
	public BilYarisAppState(){
		reset();
		
	}
	
	public void setCreatorInfo(String cinfoDefinition){
		creatorInfo = CreatorInfo.constructWithXMLString(cinfoDefinition);
	}
	
	public void setQuestionBase(String qpDefinition){
		questionBase = QuestionPack.constructWithXMLString(qpDefinition);
	}
	
	private void initJokers(){
		jokers=new ArrayList<BilYarisJoker>();
		// Add jokers
		jokers.add(new FiftyPercentJoker());
	}
	
	public void eliminateChoice(int choiceID){
		choiceStates[choiceID] = ChoiceState.HIDDEN; 
	}
	
	public List<String> getCategories(){
		return this.creatorInfo.alCategories;
	}
	
	public void setCategory(int categoryID){
		this.currentCategory = creatorInfo.alCategories.get(categoryID);
		currentQuestionPack = questionBase.getSubPack(currentCategory);
		fetchQuestion();
	}
	
	public void fetchQuestion(){
		// TODO: Random soru algoritmasi eklenecek.
		Random generator = new Random();
		int questionID = generator.nextInt(currentQuestionPack.alQuestions.size());
		setQuestion(currentQuestionPack.getQuestion(questionID));
		userRemainingTime = DEFAULT_USER_TIME;
		questionIndex++;
	}
	
	private void resetChoicesAvailable(){
		
		if(choiceStates == null)
			choiceStates = new ChoiceState[DEFAULT_N_CHOICES];
		
		for(int i=0; i< choiceStates.length; i++){
			choiceStates[i] = ChoiceState.NORMAL;
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
	
	public void elapseTime(){
		userRemainingTime --;
		if(userRemainingTime <= 0){
			gameOver = true;
			
		}
	}
	
	public boolean jokerAvailable(int jokerID){
		return jokers.get(jokerID).available();
	}
	
	public ChoiceState[] getChoiceStates(){
		return choiceStates;
	}
	
	public Question getCurrentQuestion(){
		return currentQuestion;
	}

	int getTolerance() {
		return tolerance;
	}

	void setTolerance(int tolerance) {
		this.tolerance = tolerance;
	}

	int getHintChoice() {
		return hintChoice;
	}

	void setHintChoice(int hintChoice) {
		this.hintChoice = hintChoice;
	}
	
	public int getUserRemainingTime(){
		if(userRemainingTime <0)
			userRemainingTime = 0;
		return userRemainingTime;
	}
}
