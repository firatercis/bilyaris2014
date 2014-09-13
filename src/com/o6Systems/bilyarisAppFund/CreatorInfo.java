/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.o6Systems.bilyarisAppFund;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 *
 * @author Administrator
 */
public class CreatorInfo {
    
    public final static String CREATOR_INFO_FILE_NAME = "cinfo.xml";
    

    
    @Element
    public int lastQuestionID=1000;
    
    @Element
    public String serverIPAddress;
    
    @ElementList
    List<String> alCategories;
    
    public CreatorInfo(){
        alCategories=new ArrayList<String>();
       
    }
    
    public List<String> getCategories(){
        return alCategories;
    }
    
    public void addCategory(String category){
        alCategories.add(category);
    }
    
    public static CreatorInfo constructWithXMLString(String xmlString){
    	ByteArrayInputStream bais = new ByteArrayInputStream(xmlString.getBytes());
    	 Serializer serializer = new Persister();
    	 CreatorInfo cInfo = null;
    	 try {
    		 cInfo = serializer.read(CreatorInfo.class, bais);
	     } catch (Exception e) {
	             System.out.println("Config File Parse Error! Cannot load Question Pack");
	             e.printStackTrace();
	     }
	    return cInfo;
    }
    
    public static CreatorInfo load(String fileName){
        CreatorInfo info = new CreatorInfo();
        Serializer serializer = new Persister();
        File sourceFile = new File(fileName);
        try {
                info = serializer.read(CreatorInfo.class, sourceFile);
        } catch (Exception e) {
                System.out.println("Config File Parse Error!");
                e.printStackTrace();
        }
        
        
        return info;
    }
    
    public static void save(CreatorInfo info, String fileName){
        Serializer serializer = new Persister();
        File sourceFile = new File(fileName);
        try {
            serializer.write(info, sourceFile);
        } catch (Exception ex) {
            Logger.getLogger(Question.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
