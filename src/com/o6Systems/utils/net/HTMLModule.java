package com.o6Systems.utils.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HTMLModule {
	
    private static HTMLModule instance = null;
    
    public static HTMLModule getInstance(){
        
        if(instance == null){
            instance = new HTMLModule();
        }
        
        return instance;
    }
    
    
    public String readFileFromHTTP(String url) throws IOException{
    	HttpClient client = new DefaultHttpClient();
    	HttpGet request = new HttpGet(url);
    	HttpResponse response = client.execute(request);

    	String htmlText = "";
    	InputStream in = response.getEntity().getContent();
    	BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    	StringBuilder str = new StringBuilder();
    	String line = null;
    	while((line = reader.readLine()) != null)
    	{
    	    str.append(line);
    	}
    	in.close();
    	htmlText = str.toString();
    	
    	System.out.println(htmlText);
    	
    	return htmlText;
    }
    
	
}
