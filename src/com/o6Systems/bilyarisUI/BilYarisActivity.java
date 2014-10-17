package com.o6Systems.bilyarisUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.o6Systems.bilyarisUI.R;
import com.o6Systems.bilyarisAppFund.BilYarisAppEngine;
import com.o6Systems.bilyarisAppFund.CreatorInfo;
import com.o6Systems.utils.net.HTTPModule;
import com.o6Systems.appFundamentals.AppEngine;
import com.o6Systems.appFundamentals.AppState;
import com.o6Systems.appFundamentals.AppStateObserver;
import com.o6Systems.appFundamentals.TimerTaskObserver;


public abstract class BilYarisActivity extends Activity implements AppStateObserver, TimerTaskObserver{

	AppEngine applicationEngine;
	BilYarisAppEngine byEngine;
	
	final static String DEFAULT_DESCRIPTION_SERVER = "http://31.210.54.136/bilyaris/";
	
	String descriptionServer;
	protected boolean descriptionsUpToDate;
	
	 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationEngine = BilYarisAppEngine.getInstance(); 
        byEngine = (BilYarisAppEngine)applicationEngine;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initViews();
        onStateUpdated(applicationEngine.currentState);
    }
	 
	public abstract void initViews();
	
	public void sendPrompt(int prompt, int[] params){
		AppState state = applicationEngine.onUserPrompt(prompt,params);
		this.onStateUpdated(state);
	}
	
	public void sendPrompt(int prompt, String[] params){
		AppState state = applicationEngine.onUserPrompt(prompt,params);
		this.onStateUpdated(state);
	}
	
	private String readDescription(String fileName, String readMode){
		String description = null;
		if(readMode.equals("assets")){
			description = readFromFile(fileName);
		}else if(readMode.equals("internal")){
			description = readFromFileInternal(fileName);
		}else if(readMode.equals("http")){
			try {
				description = HTTPModule.getInstance().readFileFromHTTP(fileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return description;
	}
	
	
	
	private boolean isInternetConnected(){
		// TODO: yazilacak.
		return true;
	}
	
	private boolean cInfoUpToDate(String cInfoLocalDesc, String cInfoNetDesc){
		boolean result = true;
		try {
			CreatorInfo localCInfo = CreatorInfo.constructWithXMLString(cInfoLocalDesc);
			CreatorInfo remoteCInfo = CreatorInfo.constructWithXMLString(cInfoNetDesc);
			Date localDate = localCInfo.getDate();
			Date remoteDate = remoteCInfo.getDate();
			if(remoteDate.after(localDate)){
				result = false;
			}
		} catch (ParseException e) {
			result = false;
			e.printStackTrace();
		}
		return result;
		
	}
	
	
	private void flushInternals(){
		File dir = getFilesDir();
		File file = new File(dir, BilYarisAppEngine.CREATOR_INFO_DEFAULT_FILENAME);
		file.delete();
		file = new File(dir, BilYarisAppEngine.QUESTION_PACK_DEFAULT_FILENAME);
		file.delete();
	}
	
	protected String[] fetchDescriptions(){
		String[] descriptions = new String[2];
		
		
		descriptionsUpToDate = true;
		String cInfoDescriptionLocal =  readDescription(BilYarisAppEngine.CREATOR_INFO_DEFAULT_FILENAME,"internal");
		String qpInfoDescription = null;
		if(cInfoDescriptionLocal == null){
			// ilk yukleme
			cInfoDescriptionLocal = readDescription(BilYarisAppEngine.CREATOR_INFO_DEFAULT_FILENAME,"assets");
			qpInfoDescription = readDescription(BilYarisAppEngine.QUESTION_PACK_DEFAULT_FILENAME,"assets");
			writeToFileInternal(BilYarisAppEngine.CREATOR_INFO_DEFAULT_FILENAME, cInfoDescriptionLocal);
			writeToFileInternal(BilYarisAppEngine.QUESTION_PACK_DEFAULT_FILENAME, qpInfoDescription);
			descriptionsUpToDate = false;
		}
		
		descriptions[0] = cInfoDescriptionLocal;
		
		CreatorInfo localCInfo = CreatorInfo.constructWithXMLString(cInfoDescriptionLocal);
		if(localCInfo.serverIPAddress != null){
			descriptionServer = localCInfo.serverIPAddress;
		}
		
		if(isInternetConnected()){
			String cInfoDescriptionNet =  readDescription(descriptionServer + "/" + BilYarisAppEngine.CREATOR_INFO_DEFAULT_FILENAME,"http");
			if (!cInfoUpToDate(cInfoDescriptionLocal,cInfoDescriptionNet)){
				descriptionsUpToDate = false;
				descriptions[0] =cInfoDescriptionNet; 
				qpInfoDescription = readDescription(descriptionServer + "/" + BilYarisAppEngine.QUESTION_PACK_DEFAULT_FILENAME,"http");
				writeToFileInternal(BilYarisAppEngine.CREATOR_INFO_DEFAULT_FILENAME, cInfoDescriptionNet);
				writeToFileInternal(BilYarisAppEngine.QUESTION_PACK_DEFAULT_FILENAME, qpInfoDescription);
			}else{
				qpInfoDescription = readDescription(BilYarisAppEngine.QUESTION_PACK_DEFAULT_FILENAME,"internal");
			}
		}else{
			qpInfoDescription = readDescription(BilYarisAppEngine.QUESTION_PACK_DEFAULT_FILENAME,"internal");
		}
		 
		descriptions[1] = qpInfoDescription;
		return descriptions;
	}
	
	
	
	public String readFromFileInternal(String fileName){
		String result = null;
		try {
			FileInputStream fin = openFileInput(fileName);
			 int c;
	         
			 Scanner s = new java.util.Scanner(fin).useDelimiter("\\A");
			 
	        result = s.next();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	public void writeToFileInternal(String fileName,String data){
		
		FileOutputStream out;
		try {
			out = new FileOutputStream(getFilesDir().getAbsolutePath() + "/" + fileName);
			out.write(data.getBytes());
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public String readFromFile(String fileName){
		String content = "";
	
		AssetManager assetManager =  this.getAssets(); 
		InputStream is;
		try {
			is = assetManager.open(fileName);
			Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
			content+= s.next();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    
		return content;
	}
	
	  
	
	public void onTimer(){
		// Do nothing in default;
	}
	
}
