/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.o6Systems.bilyarisAppFund;

import com.google.common.collect.Collections2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 *
 * @author Administrator
 */
@Root(strict=false)
public class QuestionPack {
	
	public final static String DEFAULT_AUTHOR_NAME = "Default";
	
    @ElementList
    ArrayList<Question> alQuestions;
    
    @Element(required = false)
    String packName;
    
    @Element(required = false)
    String tag;
    
    @Element(required = false)
    int baseQuestionID;
    
    @Element(required = false)
	private String author;
    
    @Element(required = false)
    private String date;
   
    public QuestionPack(){
        alQuestions = new ArrayList<Question>();
    }
    
    public String getDate(){
    	return date;
    }
    
    // Copy Constructor
    public QuestionPack(QuestionPack sample){
        alQuestions = new ArrayList<Question>();
        for(Question Q:sample.alQuestions){
            addQuestion(Q);
        }
    }
    
    public ArrayList<Question> getQuestions(){
        return alQuestions;
    }
    
    public void addQuestion(Question Q){
        alQuestions.add(Q);
    }
    public void setBaseQuestionID(int baseID){
        baseQuestionID = baseID;
    }
    
    public boolean isEmpty(){
        return alQuestions.isEmpty();
    }
    
    public void backupAllAlternatives(){
        for (Question currentQuestion : alQuestions) {
            currentQuestion.backupAlternatives();
        }
    }
    
    public Question getQuestion(int questionID){
    	return alQuestions.get(questionID);
    }
    
    public void restoreAllAlternatives(){
        for (Question currentQuestion : alQuestions) {
            currentQuestion.restoreAlternatives();
        }
    }
    
    public static String getXMLString(QuestionPack questionPack){
    	Serializer serializer = new Persister();
    	questionPack.backupAllAlternatives();
    	
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	try {
			serializer.write(questionPack,baos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	String result = new String(baos.toByteArray());
    	return result;
    }
    
    public static QuestionPack constructWithXMLString(String xmlString){
    	ByteArrayInputStream bais = new ByteArrayInputStream(xmlString.getBytes());
    	 Serializer serializer = new Persister();
    	 QuestionPack questionPack = null;
    	 try {
             questionPack = serializer.read(QuestionPack.class, bais);
     } catch (Exception e) {
             System.out.println("Config File Parse Error! Cannot load Question Pack");
             e.printStackTrace();
     }
     
     
     return questionPack;
    	
    }
    
    public static void save(QuestionPack questionPack, String fileName){
        Serializer serializer = new Persister();
        
        // Coskun fix...
        questionPack.backupAllAlternatives();
        //
        
        File sourceFile = new File(fileName);
        try {
            serializer.write(questionPack, sourceFile);
        } catch (Exception ex) {
            Logger.getLogger(Question.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Coskun fix...
        questionPack.restoreAllAlternatives();
        //
    }
    
    public static QuestionPack load(String fileName){
	    
	    QuestionPack questionPack = new QuestionPack();
	    
	    // TODO: Kaldirilacak.
	    System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
	    
	    Serializer serializer = new Persister();
	    File sourceFile = new File(fileName);
	    try {
	            questionPack = serializer.read(QuestionPack.class, sourceFile);
	    } catch (Exception e) {
	            System.out.println("Config File Parse Error! Cannot load Question Pack");
	            e.printStackTrace();
	    }
	    
	   
	    
	    return questionPack;
	}

	public QuestionPack getSubPack(String category){
        QuestionPack resultPack = new QuestionPack();
        resultPack.packName = this.packName + "_" + category;
        
        for(int i=0; i< alQuestions.size(); i++){
            Question currentQuestion = alQuestions.get(i);
            if(currentQuestion.category.equals(category)){
                resultPack.addQuestion(currentQuestion);
            }
        }
        return resultPack;
    }
    
   
    
    public ArrayList filterQuestions(QuestionPredicate predicate){
        Collection searchResults = Collections2.filter(alQuestions, predicate);
        return new ArrayList(searchResults);
    }
    
    
    public void shuffle(){
    	Collections.shuffle(alQuestions);
    }
    
    public String getAuthor() {
		if(author == null){
			author = DEFAULT_AUTHOR_NAME;
		}
		
		return author;
	}

	public void print(){
		System.out.println("<QUESTIONPACK>");
		if(author != null)
			System.out.println("Author: " + author);
		for(Question Q:alQuestions){
			Q.print();
		}
		System.out.println("</QUESTIONPACK>");
	}
    
}
