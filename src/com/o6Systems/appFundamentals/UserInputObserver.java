package com.o6Systems.appFundamentals;

public interface UserInputObserver {
	
	public void onUserPrompt(int prompt,int[] params);
	public void onUserPrompt(String prompt);
	
}
