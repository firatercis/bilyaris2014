package com.o6Systems.uiFundementalsAndroid;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import android.content.res.AssetManager;

import com.o6Systems.appFundamentals.AppState;
import com.o6Systems.appFundamentals.UIEngine;
import com.o6Systems.appFundamentals.UserInputObserver;

public class AndroidAppUIEngine extends UIEngine implements UserInputObserver{
	
	//ArrayList<O6BaseActivity> pages;
	O6BaseActivity currentPage;
	
	@Override
	public void onStateUpdated(AppState currentState) {
		currentPage.onStateUpdated(currentState);
	}
	
	/*public void addPage(O6BaseActivity page){
		pages.add(page);
	}*/
	
	
	
	@Override
	public String readFromFile(String fileName){
		String content = "";
		if(currentPage != null){
			AssetManager assetManager =  currentPage.getAssets(); 
			InputStream is;
			try {
				is = assetManager.open(fileName);
				Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
				content+= s.next();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		}
		return content;
	}

	@Override
	public void onUserPrompt(int prompt, int[] params) {
		sendPromptAll(prompt, params);
		
	}

	@Override
	public void onUserPrompt(String prompt) {
		sendPromptAll(prompt);
	}
	
}
