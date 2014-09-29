package com.o6Systems.appFundamentals;

public interface UserInputObserver {
	
	public AppState onUserPrompt(int prompt,int[] params);
	public AppState onUserPrompt(int prompt, String[] params);
	
	
}
