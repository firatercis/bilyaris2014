package com.o6Systems.bilyarisAppFund;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.o6Systems.bilyarisAppFund.jokers.*;
import com.o6Systems.utils.database.DatabaseInterface;
import com.o6Systems.appFundamentals.AppEngine;
import com.o6Systems.appFundamentals.AppState;


public class BilYarisAppEngine extends AppEngine{

	// User Prompt Definitions
	public final static int UP_CHOICE = 10;
	public final static int UP_CATEGORY_SELECT = 11;
	public final static int UP_OK = 12;
	public final static int UP_CANCEL = 13;
	public final static int UP_JOKER = 14;
	public final static int UP_INIT = 15;
	public final static int UP_TIMER = 16;
	// Engine States
	public final static int ES_INIT = 99;
	public final static int ES_WAITING_CHOICE = 100;
	public final static int ES_PRINTING_TEXT = 101;
	public final static int ES_CATEGORY_SELECTION=102;
	
	// User Prompt Return Definitions
	public final static int SYSTEM_OK = 0;
	public final static int UP_INVALID= 1;
	
	// Engine static variables
	public final static String QUESTION_PACK_DEFAULT_FILENAME="qpDefault.xml";
	//final static String QUESTION_PACK_DEFAULT_FILENAME = "/data/user/qpDefault.xml";
	public final static String CREATOR_INFO_DEFAULT_FILENAME="cinfo.xml";
	//final static String CREATOR_INFO_DEFAULT_FILENAME="/data/user/cinfo.xml";
	final static int CAT_ID_GENERAL = 0;
	final static int MAX_N_QUESTIONS = 10;
	
	final static int USER_ID_DEFAULT = 0;
	
	public final static int QP_DB_FETCH_SIZE = 1;
	
	// Database related
	public final static String DATABASE_INIT_SCRIPT = "CREATE TABLE QUESTIONS(qID int, text varchar(255),category varchar(30), "
			+ "choice1 varchar(100), choice2 varchar(100),choice3 varchar(100), choice4 varchar(100),"
			+ "author varchar(50));";
	
	// Application State
	//public int currentState.engineState;
	public BilYarisAppState currentBYState;
	private static AppEngine instance;
	
	private BYDatabaseInterface databaseInterface;

	
	private BilYarisAppEngine(){
		super();
		currentBYState = new BilYarisAppState();
		currentState = currentBYState;
		currentBYState.majorStateID = ES_INIT;		
	}
	
	public void registerDatabaseInterface(BYDatabaseInterface dbInterface){
		this.databaseInterface = dbInterface;
	}
	
	@Override
	public AppState onUserPrompt(int prompt, int[] params) {
		
		currentBYState.userPromptResult = AppState.UP_R_OK;
		currentBYState.updateLevel = AppState.FULL_UPDATE;
		
		if(currentBYState.majorStateID == ES_INIT){
			
		}else if(currentBYState.majorStateID == ES_CATEGORY_SELECTION){
			categorySelectionPage(prompt,params);
		}else if (currentBYState.majorStateID == ES_WAITING_CHOICE){
			
			questionPage(prompt, params);
		}else if (currentBYState.majorStateID == ES_PRINTING_TEXT){
			if(prompt == UP_OK){
				currentBYState.majorStateID = ES_CATEGORY_SELECTION;
			}
		}

		return currentBYState;		
	}
	
	
	public Question fetchQuestionFromDB(int userID, String category, int minDifficulty, int maxDifficulty){
		QuestionPack qp = databaseInterface.getQuestions(category, 0, 0, 100, QP_DB_FETCH_SIZE);
		return qp.getQuestion(0);
	}
	
	
	
	// State response functions
	private void categorySelectionPage(int prompt, int[] params){
		if(prompt == UP_CATEGORY_SELECT){
			int categoryID = params[0];
			String category = currentBYState.getCategories().get(categoryID);
			QuestionPack questionBase = databaseInterface.getQuestions(category, 0, 0, 100, QP_DB_FETCH_SIZE);
			//currentBYState.setQuestionBase(questionBase);
			currentBYState.setCategory(categoryID);
			
			Question currentQuestion= fetchQuestionFromDB(0,category,0,100);
			currentBYState.setQuestion(currentQuestion);
			//currentBYState.fetchQuestion();
			// TODO: User ve difficulty ayarlanacak.
			
			currentBYState.reset();
			currentBYState.majorStateID = ES_WAITING_CHOICE;
		}	
	}
	
	private void questionPage(int prompt, int[] params){
		if(prompt == UP_CHOICE){
			int currentChoice = params[0];
			currentBYState.processAnswer(currentChoice);
			databaseInterface.setQuestionAsked(currentBYState.currentQuestion, currentBYState.getCurrentUser());
			
			if(currentBYState.isGameOver() == true){
				printResults();
				currentBYState.majorStateID = ES_PRINTING_TEXT;
			}else{
				Question currentQuestion= fetchQuestionFromDB(0,currentBYState.getCurrentCategory(),0,100);
				currentBYState.setQuestion(currentQuestion);
				currentBYState.incrementQuestionIndex();
			}
			
			/*if(currentBYState.outOfQuestion()){
				String category = currentBYState.getCurrentCategory();
				User currentUser = currentBYState.getCurrentUser();
				QuestionPack newPack = databaseInterface.getQuestions(category,currentUser.userID,0,100,QP_DB_FETCH_SIZE);
				currentBYState.setQuestionBase(newPack);
			}*/
			
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
		
		if(prompt == UP_TIMER){
			currentBYState.elapseTime();
			if(currentBYState.isGameOver() == true){
				printResults();
				currentBYState.majorStateID = ES_PRINTING_TEXT;
				currentBYState.updateLevel = AppState.SEMI_UPDATE;
			}
		}
	}
	

	
	public void printResults(){
		if(currentBYState.isGameOver()){
			currentBYState.appMessage = "Oyun Bitti\r\n";
			currentBYState.appMessage += "Puan: " + currentBYState.userScore;			
		}else{
			currentBYState.appMessage = "Tebrikler\r\n";
			currentBYState.appMessage += "Puan: " + currentBYState.userScore;
		}
	}
	
	public void start(){

		postUpdate(AppState.FULL_UPDATE);
		
	}
	
	public static AppEngine getInstance(){
		if(instance == null){
			instance = new BilYarisAppEngine();
		}
		return instance;
	}
		
	public void initApplication(String cInfoDescription,String qpDescription, BYDatabaseInterface dbInterface,boolean upToDate){
		currentBYState.setCreatorInfo(cInfoDescription);
		registerDatabaseInterface(dbInterface);
		currentBYState.majorStateID = ES_CATEGORY_SELECTION;
		
		if(!upToDate){
			dbInterface.clearQuestions();
			QuestionPack importQuestions = QuestionPack.constructWithXMLString(qpDescription);
			dbInterface.insertQuestionPack(importQuestions);
		}
			
		// TODO: user selection eklenecek
		User defaultUser = new User("default");
		currentBYState.setCurrentUser(defaultUser);
	}
	
	/*private void initDatabase(){
		if(dbInterface != null){
			dbInterface.setCreateScript(DATABASE_INIT_SCRIPT);
		}
	}*/
	
	
	
	@Override
	public AppState onUserPrompt(int prompt, String[] params) {
	
		return currentBYState;
	}
	
}
