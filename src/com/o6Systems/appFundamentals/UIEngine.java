package com.o6Systems.appFundamentals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class UIEngine implements AppStateObserver {
	ArrayList<UserInputObserver> userInputObservers;
	
	public UIEngine(){
		userInputObservers = new ArrayList<UserInputObserver>();
	}
	
	public void sendPromptAll(int prompt, int[] params){
		for(UserInputObserver currentObserver: userInputObservers){
			AppState state = currentObserver.onUserPrompt(prompt, params);
			this.onStateUpdated(state);
		}
	}
	
	
	
	public void addUserInputObserver(UserInputObserver observer){
		userInputObservers.add(observer);
	}
	
	public String readFromFile(String fileName){
		
		String content=null;
		try {
			content = new Scanner(new File(fileName)).useDelimiter("\\A").next();
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			e.printStackTrace();
		}
	
		return content;
	}
	

}
