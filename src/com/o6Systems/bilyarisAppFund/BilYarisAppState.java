package com.o6Systems.bilyarisAppFund;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.o6Systems.bilyarisAppFund.jokers.BilYarisJoker;
import com.o6Systems.bilyarisAppFund.jokers.DoubleAnswerJoker;
import com.o6Systems.bilyarisAppFund.jokers.FiftyPercentJoker;
import com.o6Systems.bilyarisAppFund.jokers.IncreaseTimeJoker;
import com.o6Systems.appFundamentals.AppState;

public class BilYarisAppState extends AppState {
	
	final static int DEFAULT_N_CHOICES = 4;
	final static int DEFAULT_USER_TIME = 50;
	
	// SOund related
	public final static int NO_SOUND = 0;
	public final static int SOUND_CORRECT = 1;
	public final static int SOUND_WRONG = 2;

	
	public enum ChoiceState{
		NORMAL,HIDDEN,HIGHLIGHTED,HIGHLIGHTED_WRONG
	}
	
	QuestionPack questionBase;
	Question currentQuestion;
	String currentCategory;
	
	User currentUser;
	
	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	// TODO: Will be changed to UserStats object
	int userScore;
	int questionIndex;
	int packQuestionIndex;
	int userRemainingTime;
	private boolean gameOver;

	
	
	private int tolerance;
	private int hintChoice;
	
	CreatorInfo creatorInfo;
	
	public String appMessage;
	List<String> categories;
	
	ArrayList<BilYarisJoker> jokers;
	
	ChoiceState[] choiceStates;
	private boolean usingDoubleAnswer;
	
	private int currentSoundID;
	
	public void clearSound(){
		currentSoundID = NO_SOUND;
	}
	
	public BilYarisAppState(){
		reset();
		
	}
	
	public int getQuestionIndex(){
		return questionIndex;
	}
	
	public void setCreatorInfo(String cinfoDefinition){
		creatorInfo = CreatorInfo.constructWithXMLString(cinfoDefinition);
	}
	
	public void setQuestionBase(String qpDefinition){
		setQuestionBase(QuestionPack.constructWithXMLString(qpDefinition));
	}
	
	public void setQuestionBase(QuestionPack qp){
		questionBase = qp;
	}
	
	private void initJokers(){
		jokers=new ArrayList<BilYarisJoker>();
		// Add jokers
		jokers.add(new FiftyPercentJoker());
		jokers.add(new IncreaseTimeJoker());
		jokers.add(new IncreaseTimeJoker());
		jokers.add(new DoubleAnswerJoker());
	}
	
	public void eliminateChoice(int choiceID){
		choiceStates[choiceID] = ChoiceState.HIDDEN; 
	}
	
	public List<String> getCategories(){
		return this.creatorInfo.alCategories;
	}
	
	public String getCurrentCategory(){
		return currentCategory;
	}
	
	public void setCategory(int categoryID){
		this.currentCategory = creatorInfo.alCategories.get(categoryID);
	}
	
	public boolean outOfQuestion(){
		boolean result = false;
		if(packQuestionIndex >= questionBase.getQuestions().size()){
			result = true;
		}
		return result;
	}
		
	/*public void fetchQuestion(){
		setQuestion(questionBase.getQuestion(packQuestionIndex));
		packQuestionIndex++;
		userRemainingTime = DEFAULT_USER_TIME;
		questionIndex++;
		setUsingDoubleAnswer(false);
		setCurrentSoundID(NO_SOUND);
	}*/
	
	private void resetQuestionState(){
		userRemainingTime = DEFAULT_USER_TIME;
		
		if(choiceStates == null)
			choiceStates = new ChoiceState[DEFAULT_N_CHOICES];
		
		for(int i=0; i< choiceStates.length; i++){
			choiceStates[i] = ChoiceState.NORMAL;
		}
	}
	
	public void reset(){
		questionIndex = 0;
		packQuestionIndex = 0;
		userScore = 0;
		setGameOver(false);
		setUsingDoubleAnswer(false);
		initJokers();
		setCurrentSoundID(NO_SOUND);
	}
	
	public void incrementQuestionIndex(){
		questionIndex++;
	}
	
	public void applyJoker(int jokerID){
		BilYarisJoker joker = jokers.get(jokerID);
		joker.apply(this);
	}
	
	public void setQuestion(Question Q){
		currentQuestion = Q;
		resetQuestionState();
		// TODO: Zaman ile ilgili kisimlar gelecek.
	}
	
	public boolean getJokerAvailable(int i){
		return jokers.get(i).available();
	}
	
	public void processAnswer(int answerID){
		if(currentQuestion.getAnswer() == answerID){
			userScore ++;
			//fetchQuestion();
			setCurrentSoundID(SOUND_CORRECT);
		}else{
			choiceStates[answerID] = ChoiceState.HIGHLIGHTED_WRONG;
			setCurrentSoundID(SOUND_WRONG);
			if(isUsingDoubleAnswer()){
				System.out.println("Using double answer");
				setUsingDoubleAnswer(false);
			}else{
				setGameOver(true);
			}
		}
	}
	
	public void elapseTime(){
		userRemainingTime --;
		if(userRemainingTime <= 0){
			setGameOver(true);
			
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
	
	public void setUserRemainingTime(int time){
		userRemainingTime = time;
	}

	public boolean isUsingDoubleAnswer() {
		return usingDoubleAnswer;
	}

	public void setUsingDoubleAnswer(boolean usingDoubleAnswer) {
		this.usingDoubleAnswer = usingDoubleAnswer;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public int getCurrentSoundID() {
		return currentSoundID;
	}

	public void setCurrentSoundID(int currentSoundID) {
		this.currentSoundID = currentSoundID;
	}
}
