package com.o6Systems.bilyarisAppFund;

import java.util.ArrayList;
import java.util.Random;

import com.o6Systems.bilyarisAppFund.jokers.*;
import com.o6Systems.appFundamentals.AppEngine;
import com.o6Systems.appFundamentals.AppState;


public class BilYarisAppEngine extends AppEngine{

	// User Prompt Definitions
	public final static int UP_CHOICE = 10;
	public final static int UP_CATEGORY_SELECT = 11;
	public final static int UP_OK = 12;
	public final static int UP_CANCEL = 13;
	public final static int UP_JOKER = 14;
	// Engine States
	public final static int ES_WAITING_CHOICE = 100;
	public final static int ES_PRINTING_TEXT = 101;
	public final static int ES_CATEGORY_SELECTION=102;
	
	// User Prompt Return Definitions
	public final static int SYSTEM_OK = 0;
	public final static int UP_INVALID= 1;
	
	// Engine static variables
	final static String QUESTION_PACK_DEFAULT_FILENAME="qpDefault.xml";
	//final static String QUESTION_PACK_DEFAULT_FILENAME = "/data/user/qpDefault.xml";
	final static String CREATOR_INFO_DEFAULT_FILENAME="cinfo.xml";
	//final static String CREATOR_INFO_DEFAULT_FILENAME="/data/user/cinfo.xml";
	final static int CAT_ID_GENERAL = 0;
	final static int MAX_N_QUESTIONS = 10;
	
	
	// Application State
	//public int currentState.engineState;
	public BilYarisAppState currentBYState;
	public QuestionPack currentQuestionPack;
	public CreatorInfo creatorInfo;

	public BilYarisAppEngine(){
		super();
		currentBYState = new BilYarisAppState();
		currentState = currentBYState;
		currentBYState.majorStateID = ES_CATEGORY_SELECTION;		
	}
	
	private void loadAssets(){
		String cInfoDescripton = uiEngine.readFromFile(CREATOR_INFO_DEFAULT_FILENAME);
		creatorInfo = CreatorInfo.constructWithXMLString(cInfoDescripton);
		currentBYState.categories = creatorInfo.alCategories;
		String strQPDescription = uiEngine.readFromFile(QUESTION_PACK_DEFAULT_FILENAME);
		if(strQPDescription != null)
			currentQuestionPack = QuestionPack.constructWithXMLString(strQPDescription);
	}
	
	@Override
	public void onUserPrompt(int prompt, int[] params) {
		
		currentBYState.userPromptResult = AppState.UP_R_OK;
		
		if(currentBYState.majorStateID == ES_CATEGORY_SELECTION){
			if(prompt == UP_CATEGORY_SELECT){
				int categoryID = params[0];
				changeCategory(categoryID);
				resetAppState();
				currentBYState.majorStateID = ES_WAITING_CHOICE;
			}	
		}else if (currentBYState.majorStateID == ES_WAITING_CHOICE){
			if(prompt == UP_CHOICE){
				int currentChoice = params[0];
				currentBYState.processAnswer(currentChoice);
				if((currentBYState.questionIndex < MAX_N_QUESTIONS) && (currentBYState.gameOver == false)){
					fetchQuestion();
				}else{
					printResults();
					currentBYState.majorStateID = ES_PRINTING_TEXT;
				}
			}
			
			if(prompt == UP_CANCEL){
				currentBYState.majorStateID = ES_CATEGORY_SELECTION;
			}
			
			if(prompt == UP_JOKER){
				int jokerID = params[0];
				if(currentBYState.jokerAvailable(jokerID)){
					currentBYState.applyJoker(jokerID);
				}else{
					currentBYState.userPromptResult = AppState.UP_R_NOT_AVAILABLE;
				}
			}
			
		}else if (currentBYState.majorStateID == ES_PRINTING_TEXT){
			if(prompt == UP_OK){
				currentBYState.majorStateID = ES_CATEGORY_SELECTION;
			}
		}

		postUpdate(AppState.FULL_UPDATE);		
	}
	
	
	
	public void resetAppState(){
		loadAssets();
		currentBYState.reset();
		fetchQuestion();
	}
	
	public void fetchQuestion(){
		// TODO: Random soru algoritmasi eklenecek.
		Random generator = new Random();
		int questionID = generator.nextInt(currentQuestionPack.alQuestions.size());
		currentBYState.setQuestion(currentQuestionPack.getQuestion(questionID));
		currentBYState.questionIndex++;
	}
	
	public void changeCategory(int categoryID){
		
		
		
		if(categoryID != CAT_ID_GENERAL){
			String category = creatorInfo.alCategories.get(categoryID);
			currentQuestionPack = currentQuestionPack.getSubPack(category);
			currentBYState.currentCategory = category;
		}	
	}
	
	public void printResults(){
		if(currentBYState.gameOver){
			currentBYState.appMessage = "Oyun Bitti\r\n";
			currentBYState.appMessage += "Puan: " + currentBYState.userScore;			
		}else{
			currentBYState.appMessage = "Tebrikler\r\n";
			currentBYState.appMessage += "Puan: " + currentBYState.userScore;
		}
	}
	
	public void start(){
		
		loadAssets();
		postUpdate(AppState.FULL_UPDATE);
		
	}
	
	@Override
	public void onUserPrompt(String prompt) {

		System.out.println("String type of user prompts are not supported in this application.");
	}
	
}
