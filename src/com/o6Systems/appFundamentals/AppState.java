package com.o6Systems.appFundamentals;

public abstract class AppState {
	public final static int FULL_UPDATE = 0;
	public final static int SEMI_UPDATE = 1;
	public final static int NO_UPDATE = 2;
	
	public final static int UP_R_OK = 0;
	public final static int UP_R_NOT_AVAILABLE = 1;
	
	
	// Engine related
	public int majorStateID;
	// User Prompt is accepted or not
	public int userPromptResult;
	// Can be extended
	public int updateLevel;
}
