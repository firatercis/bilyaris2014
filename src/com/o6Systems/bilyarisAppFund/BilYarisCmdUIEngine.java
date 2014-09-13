package com.o6Systems.bilyarisAppFund;

import java.util.Scanner;

import com.o6Systems.appFundamentals.AppState;
import com.o6Systems.appFundamentals.UIEngine;

public class BilYarisCmdUIEngine extends UIEngine{

	final static int PROMPT_JOKER_THRESHOLD = 10;
	Scanner userScanner;
		
	
	public BilYarisCmdUIEngine(){
		super();
		userScanner = new Scanner(System.in);
	}
	
	public static void main(String[] args) {
		System.out.println("Hello World");
		BilYarisAppEngine appEngine = new BilYarisAppEngine();
		BilYarisCmdUIEngine uiEngine = new BilYarisCmdUIEngine();
		
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		
		appEngine.connectUIEngine(uiEngine);
		uiEngine.addUserInputObserver(appEngine);
		
		appEngine.start();
	}
	

	@Override
	public void onStateUpdated(AppState currentState) {
		
		BilYarisAppState currentBYState = (BilYarisAppState) currentState;
		
		switch(currentBYState.majorStateID){
		case BilYarisAppEngine.ES_CATEGORY_SELECTION:
			printCategoryPage(currentBYState);
			break;
		case BilYarisAppEngine.ES_WAITING_CHOICE:
			printQuestionPage(currentBYState);
			break;
		case BilYarisAppEngine.ES_PRINTING_TEXT:
			printResultsPage(currentBYState);
			break;
		}	
	}
	
	public void printCategoryPage(BilYarisAppState currentBYState){
		System.out.println("********************Welcome to Bilyaris*********************");
		System.out.println("CATEGORIES:");
		
		for(int i=0; i<currentBYState.categories.size(); i++){
			String category = currentBYState.categories.get(i);
			System.out.println("" + i + ": " + category);
			
		}
		
		System.out.println("SELECT YOUR CATEGORY:");
		int categoryID = userScanner.nextInt();
		
		int prompt = BilYarisAppEngine.UP_CATEGORY_SELECT;
		int[] params = new int[1];
		params[0] = categoryID;
		
		sendPromptAll(prompt,params);
		
	}
	
	public void printQuestionPage(BilYarisAppState currentBYState){
		System.out.println("********************" + "QUESTION " + currentBYState.questionIndex + "*****************************");
		System.out.println(currentBYState.currentQuestion.text);
		System.out.println("*************************************************");
		
		String[] alternatives = currentBYState.currentQuestion.getAlternatives();
		for(int i=0; i<alternatives.length; i++){
			if(currentBYState.choicesAvailable[i] == true){
				System.out.println(Question.ALTERNATIVE_CAPTIONS[i] + ": " + alternatives[i]);
			}
			
		}
		
		System.out.println("SELECT YOUR ANSWER:");
		int userAnswerID = userScanner.nextInt();
		
		if(userAnswerID >= PROMPT_JOKER_THRESHOLD){
			int prompt = BilYarisAppEngine.UP_JOKER;
			int[] params = new int[1];
			params[0] = userAnswerID - PROMPT_JOKER_THRESHOLD;
			sendPromptAll(prompt,params);
		}else{
			int prompt = BilYarisAppEngine.UP_CHOICE;
			int[] params = new int[1];
			params[0] = userAnswerID;
			sendPromptAll(prompt,params);
		}
		
		
		
		
		
	}
	
	public void printResultsPage(BilYarisAppState currentBYState){
		System.out.println("**********************RESULTS***************************");
		System.out.println(currentBYState.appMessage);
		String ok = userScanner.next();
		
		int prompt = BilYarisAppEngine.UP_OK;
		int[] params = null;
		sendPromptAll(prompt,params);
	
	}
	
	
}
